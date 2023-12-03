package si.fri.rso.simplsrecka.transaction.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "_transaction")
@NamedQueries(value =
        {
                @NamedQuery(name = "TransactionEntity.getAll",
                        query = "SELECT t FROM TransactionEntity t")
        })
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    // Enum za tip transakcije
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }

    // Enum za status transakcije
    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }
}