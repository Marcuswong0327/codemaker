package DepositInterestPredictor;

public class Bank {
    private int bankId;
    private String bankName;
    private double interestRate;

    public Bank(int bankId, String bankName, double interestRate) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.interestRate = interestRate;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
