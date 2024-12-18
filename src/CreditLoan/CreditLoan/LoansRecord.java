package src.CreditLoan.CreditLoan;

public class LoansRecord {
    private int loanId;
    private String userId;
    private double principalAmount;
    private double interestRate;
    private int repaymentPeriod;
    private double outstandingBalance;
    private String status;
    private String createdAt;

    public LoansRecord(int loanId, String userId, double principalAmount, double interestRate,
                      int repaymentPeriod, double outstandingBalance, String status, String createdAt) {
        this.loanId = loanId;
        this.userId = userId;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.repaymentPeriod = repaymentPeriod;
        this.outstandingBalance = outstandingBalance;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getUserId() {
        return userId;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
