package si.fri.rso.simplsrecka.transaction.lib;

public class CombinedTransactionLotteryResult {

    // TransactionEntity
    private Double amount;
    private String drawDate;
    private Integer id;
    private String paidCombination;
    private Integer ticketId;
    private String transactionDate;
    private String type;
    private Integer userId;

    // LotteryResult
    private String lotteryDrawingDate;
    private Integer lotteryId;
    private String lotteryCategory;
    private Integer totalPrize;
    private String winningCombination;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaidCombination() {
        return paidCombination;
    }

    public void setPaidCombination(String paidCombination) {
        this.paidCombination = paidCombination;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLotteryDrawingDate() {
        return lotteryDrawingDate;
    }

    public void setLotteryDrawingDate(String lotteryDrawingDate) {
        this.lotteryDrawingDate = lotteryDrawingDate;
    }

    public Integer getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Integer lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryCategory() {
        return lotteryCategory;
    }

    public void setLotteryCategory(String lotteryCategory) {
        this.lotteryCategory = lotteryCategory;
    }

    public Integer getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(Integer totalPrize) {
        this.totalPrize = totalPrize;
    }

    public String getWinningCombination() {
        return winningCombination;
    }

    public void setWinningCombination(String winningCombination) {
        this.winningCombination = winningCombination;
    }
}
