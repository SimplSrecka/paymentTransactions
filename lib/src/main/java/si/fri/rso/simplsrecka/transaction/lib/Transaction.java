package si.fri.rso.simplsrecka.transaction.lib;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Transaction {

    private Integer id;
    private Integer userId;
    private Integer ticketId;
    private Double amount;
    private String paidCombination;
    private String type;
    private Instant transactionDate;
    private LocalDate drawDate;

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

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getTicketId() {
        return ticketId;
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

    public LocalDate getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(LocalDate drawDate) {
        this.drawDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
    }

}