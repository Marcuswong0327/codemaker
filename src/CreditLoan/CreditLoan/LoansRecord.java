package src.CreditLoan.CreditLoan;

import java.time.LocalDate;

public class LoansRecord {
    private int loanId;
    private String userId;
    private double loanAmount;
    private double interestRate;
    private int months;
    private double remainingAmount;
    private String status; // "Active" or "Paid"
    private String loanStartDate;

    // Constructor
    public LoansRecord(int loanId, String userId, double loanAmount, double interestRate, int months, double remainingAmount, String status, String loanStartDate) {
        this.loanId = loanId;
        this.userId = userId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.months = months;
        this.remainingAmount = remainingAmount;
        this.status = status;
        this.loanStartDate = loanStartDate;
    }

    // Getters for the fields
    public int getLoanId() {
        return loanId;
    }

    public String getUserId() {
        return userId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getMonths() {
        return months;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getLoanStartDate() {
        return loanStartDate;
    }
}

