package Transaction;

import java.time.LocalDate;
import java.util.Scanner;

public class Debit {
    private double debit;
    private String description;
    private static double balance; // Shared balance between debit and credit

    Scanner scanner = new Scanner(System.in);

    public Debit() {
        this.debit = 0;
        this.description = "";
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static double getBalance() {
        return balance;
    }

    public static void setBalance(double balance) {
        Debit.balance = balance;
    }

    // Method to record a debit transaction
    public Transaction recordDebit() {
        System.out.println("Enter debit amount: ");
        debit = scanner.nextDouble();
        if (debit <= 0) {
            System.out.println("Debit amount must be positive!");
            return null;
        }

        System.out.println("Enter description: ");
        scanner.nextLine(); // Clear buffer
        description = scanner.nextLine();

        balance += debit;

        System.out.println("Debit Successfully Recorded!");
        return new Transaction(LocalDate.now(), description, debit, 0, balance);
    }
}

