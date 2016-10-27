import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id")
    private User user;

    private String description;

    public Transaction() {}
    public Transaction(User user, String description) {
        this.user = user;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return user.getName() + ": " + description;
    }

    public static void getAllTransactions(EntityManager em, User u) {
        Query query = em.createQuery("SELECT t FROM Transaction t ", Transaction.class);
        List<Transaction> list = query.getResultList();
        for (Transaction t : list) {
            t.toString();
        }
    }
}
