package Transaction;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private LocalDate date;
    private String description;
    private double debit;
    private double credit;
    private double balance;

    public Transaction(LocalDate date, String description, double debit, double credit, double balance) {
        this.date = date;
        this.description = description;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getDebit() {
        return debit;
    }

    public double getCredit() {
        return credit;
    }

    public double getBalance() {
        return balance;
    }
}


