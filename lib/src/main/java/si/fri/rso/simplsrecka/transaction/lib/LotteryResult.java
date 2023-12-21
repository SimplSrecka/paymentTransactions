package si.fri.rso.simplsrecka.transaction.lib;

public class LotteryResult {

    private String drawingDate;
    private int id;
    private String lotteryCategory;
    private int ticketId;
    private int totalPrize;
    private String winningCombination;

    public String getDrawingDate() {
        return drawingDate;
    }

    public void setDrawingDate(String drawingDate) {
        this.drawingDate = drawingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLotteryCategory() {
        return lotteryCategory;
    }

    public void setLotteryCategory(String lotteryCategory) {
        this.lotteryCategory = lotteryCategory;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(int totalPrize) {
        this.totalPrize = totalPrize;
    }

    public String getWinningCombination() {
        return winningCombination;
    }

    public void setWinningCombination(String winningCombination) {
        this.winningCombination = winningCombination;
    }
}
