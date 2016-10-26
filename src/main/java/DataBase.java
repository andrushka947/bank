import javax.persistence.EntityManager;
import java.util.Scanner;

public class DataBase {
    public void addClient(EntityManager em, Scanner sc) {
        User user = new User("Sub", "Zero");
        user.setCount(new Count(152, 0 , 4892));
        em.persist(user);
    }



}
