

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPATest");
        EntityManager em = emf.createEntityManager();

        try {
            User.removeAllUsers(em);
            User user = new User("Andrey", "Aleks");
            user.setCount(new Count());
            user.getCount().setUser(user);
            user.getCount().addUAH(em, 100);
            user.getCount().addUSD(em, 200);
            user.addFundsByCurrency(em, 140, "EUR");
            user.getCount().addUSD(em, 1100);
            user.getCount().transferCurrency(em, "USD", 50, "UAH");
            Rate rate = new Rate();
            em.persist(rate);
            System.out.println("_________________________________");

            em.getTransaction().begin();
            try {
                em.merge(user);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
                throw e;
            }

            System.out.println("Before result_________________________________");

            Query query = em.createQuery("SELECT u FROM User u");
            List<User> list = query.getResultList();
            if (list.size() != 0) {
                for (User u : list) {
                    System.out.println(u.toString());
                    System.out.println(u.getCount().toString());
                    System.out.println(u.getCount().getAllMoneyInUAH());
                    for (Transaction t : u.getTransactions()) {
                        System.out.println(t.toString());
                    }
                }
            }
            //Transaction.getAllTransactions(em, user);
            System.out.println("After result________________________");
        } finally {
            em.close();
            emf.close();
        }

    }
}




/*
 Создать базу данных «Банк» с таблицами «Пользователи»,
        «Транзакции», «Счета» и «Курсы валют». Счет бывает 3-х видов:
        USD, EUR, UAH.
  +      - Написать запросы для пополнения счета в нужной валюте
  +      - перевода средств с одного счета на другой
  +      - конвертации валюты по курсу в рамках счетов одного пользователя.
  +      - Написать запрос для получения суммарных средств на счету одного пользователя в UAH (расчет по курсу).*/
