package Transaction;

import java.time.LocalDate;
import java.util.Scanner;

public class Credit {
    private double credit;
    private String description;
    private static double balance; // Shared balance between debit and credit

    Scanner scanner = new Scanner(System.in);

    public Credit() {
        this.credit = 0;
        this.description = "";
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
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
        Credit.balance = balance;
    }

    // Method to record a credit transaction
    public Transaction recordCredit() {
        System.out.println("Enter credit amount: ");
        credit = scanner.nextDouble();
        if (credit <= 0) {
            System.out.println("Credit amount must be positive!");
            return null;
        }

        if (credit > balance) {
            System.out.println("Insufficient balance for this credit transaction!");
            return null;
        }

        System.out.println("Enter description: ");
        scanner.nextLine(); // Clear buffer
        description = scanner.nextLine();

        balance -= credit;

        System.out.println("Credit Successfully Recorded!");
        return new Transaction(LocalDate.now(), description, 0, credit, balance);
    }
}



