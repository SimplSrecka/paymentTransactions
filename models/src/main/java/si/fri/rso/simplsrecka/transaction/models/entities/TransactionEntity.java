package si.fri.rso.simplsrecka.transaction.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "_transaction")
@NamedQueries(value =
        {
                @NamedQuery(name = "TransactionEntity.getAll",
                        query = "SELECT t FROM TransactionEntity t"),
                @NamedQuery(name = "TransactionEntity.getByUserId",
                        query = "SELECT t FROM TransactionEntity t WHERE t.userId = :userId")
        })
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "paid_combination")
    private String paidCombination;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Column(name = "transaction_date")
    private Instant transactionDate;


    public enum TransactionType {
        DEPOSIT_COMBINATION, DEPOSIT, WITHDRAWAL
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

    public String getPaidCombination() {
        return paidCombination;
    }

    public void setPaidCombination(String paidCombination) {
        this.paidCombination = paidCombination;
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

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }
}