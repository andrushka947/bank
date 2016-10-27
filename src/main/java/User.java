import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String surname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "count_id")
    private Count count;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, targetEntity = Transaction.class)  //mappedBy - a field's name from the other side(in this case it's - user in Transaction.class)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    public User() {}
    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void setTransactions(List<Transaction> transactionList) {
        this.transactions = transactionList;
    }

    public String toString() {
        return "Name: " + name + " " + surname;
    }

    ///other methods///////////////////////////////////////////////////////////////

    public void addTransaction(Transaction transaction) {
        transaction.setUser(this);
        transactions.add(transaction);
    }

    public void addFundsByCurrency(EntityManager em, int amount, String currency, String name) {
        User u = (User) em.find(User.class, getIdByName(em, name));
        if (currency.equals("USD")) {
            u.getCount().addUSD(em, amount);
        } else if (currency.equals("UAH")) {
            u.getCount().addUAH(em, amount);
        } else if (currency.equals("EUR")) {
            u.getCount().addEUR(em, amount);
        }
        em.persist(u);
    }

    public void addFundsByCurrency(EntityManager em, int amount, String currency) {
        if (currency.equals("USD")) {
            this.count.addUSD(em, amount);
        } else if (currency.equals("UAH")) {
            this.count.addUAH(em, amount);
        } else if (currency.equals("EUR")) {
            this.count.addEUR(em, amount);
        }
        em.merge(this);
    }

    public static long getIdByName(EntityManager em, String name) {
        long res = 0;
        Query query = em.createQuery("SELECT u FROM User u ");
        List<User> users = query.getResultList();
        for (User u : users) {
            if (u.getName().equals(name)) {
                res = u.getId();
            }
        }
        return res;
    }

    public static void removeAllUsers(EntityManager em) {
        Query query = em.createQuery("SELECT u FROM User u");
        List<User> list = query.getResultList();
        em.getTransaction().begin();
        try {
            for (User u : list) {
                em.remove(u);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
}
