import org.hibernate.annotations.Table;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

@Entity
@javax.persistence.Table(name = "Counts")
public class Count {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double USD;
    private double EUR;
    private double UAH;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "count")
    private User user;

    public Count() {}
    public Count(double usd, double eur, double uah) {
        this.USD = usd;
        this.EUR = eur;
        this.UAH = uah;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getEUR() {
        return EUR;
    }

    public void setEUR(double EUR) {
        this.EUR = EUR;
    }

    public double getUAH() {
        return UAH;
    }

    public void setUAH(double UAH) {
        this.UAH = UAH;
    }

    public void addUAH(EntityManager em, double amount) {
        UAH += amount;
        Transaction transaction = new Transaction(this.user, this.user.getName() + " added " + amount + " to UAH");
        transaction.setUser(this.getUser());
        em.persist(transaction);
    }

    public void addEUR(EntityManager em, double amount) {
        EUR += amount;
        Transaction transaction = new Transaction(this.getUser(), this.getUser().getName() + " added " + amount + " to EUR");
        transaction.setUser(this.getUser());
        em.persist(transaction);
    }

    public void addUSD(EntityManager em, double amount) {
        USD += amount;
        Transaction transaction = new Transaction(this.user, this.user.getName() + " added " + amount + " to USD");
        transaction.setUser(this.getUser());
        em.persist(transaction);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void transferCurrency(EntityManager em, String fromCurrency, double amountFrom, String toCurrency) throws InvocationTargetException, IllegalAccessException {
        if (fromCurrency.equals("UAH")) {
            this.UAH -= amountFrom;
        } else if (fromCurrency.equals("EUR")) {
            this.EUR -= amountFrom;
        } else if (fromCurrency.equals("USD")) {
            this.USD -= amountFrom;
        }
        String fieldName = "" + fromCurrency + "_TO_" + toCurrency;
        double rate = 0;
        Rate r = new Rate();
        final Class<?> cls = Rate.class;                                //reflection piece for using right method
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isPrivate(f.getModifiers())) {
                f.setAccessible(true);
            }
            if (f.getName().equals(fieldName)) {
                rate = f.getDouble(r);                                     //getting
            }
        }
        if (toCurrency.equals("UAH")) {
            this.UAH += amountFrom * rate;
        } else if (toCurrency.equals("EUR")) {
            this.EUR += amountFrom * rate;
        } else if (toCurrency.equals("USD")) {
            this.USD += amountFrom * rate;
        }
        Transaction transaction = new Transaction(this.getUser(), this.getUser().getName() + " transferred " + amountFrom + fromCurrency + " to " + toCurrency);
        transaction.setUser(this.getUser());
        em.persist(transaction);
    }

    public String toString() {
        return "UAH:" + UAH + ", USD:" + USD + ", EUR:" + EUR;
    }

    public double getAllMoneyInUAH() throws IllegalAccessException {
        double res = this.getUAH();

        Rate r = new Rate();
        final Class<?> cls = Rate.class;                                //reflection piece for using right method
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isPrivate(f.getModifiers())) {
                f.setAccessible(true);
            }
            if (f.getName().equals("USD_TO_UAH")) {
                res += f.getDouble(r) * this.getUSD();                                     //getting
            }
            if (f.getName().equals("EUR_TO_UAH")) {
                res += f.getDouble(r) * this.getEUR();
            }
        }

        return res;
    }
}


/*
 Создать базу данных «Банк» с таблицами «Пользователи»,
        «Транзакции», «Счета» и «Курсы валют». Счет бывает 3-х видов:
        USD, EUR, UAH.
        - Написать запросы для пополнения счета в нужной валюте
        - перевода средств с одного счета на другой, конвертации
        - валюты по курсу в рамках счетов одного пользователя.
        - Написать запрос для получения суммарных средств на счету одного
        пользователя в UAH (расчет по курсу).*/
