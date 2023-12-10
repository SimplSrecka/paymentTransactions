package si.fri.rso.simplsrecka.transaction.lib;

import java.time.Instant;

public class Transaction {

    private Integer id;
    private Integer userId;
    private Double amount;
    private String paidCombination;
    private String type;
    private Instant transactionDate;

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

    public String getPaidCombination() {
        return paidCombination;
    }

    public void setPaidCombination(String paidCombination) {
        this.paidCombination = paidCombination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

}