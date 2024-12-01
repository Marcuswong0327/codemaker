package Transaction;

import java.util.Scanner;

public class Debit {
    private static double balance = 0; // Shared balance variable
    private double debit = 0;
    private String description = "";

    Scanner k = new Scanner(System.in);

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public static double getBalance() { // Static method to access shared balance
        return balance;
    }

    public static void setBalance(double balance) { // Static method to modify shared balance
        Debit.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void debitDetail() {
        System.out.println("Enter amount: ");
        debit = k.nextDouble(); // The debit needs to be a positive amount
        if (debit <= 0) {
            System.out.println("Debit amount must be positive!");
            return;
        }
        balance += debit; // Update shared balance
        System.out.println("Enter description: ");
        k.nextLine(); // Consume the leftover newline
        description = k.nextLine();

        System.out.println("Debit Successfully Recorded!!!");
        System.out.println("New Balance: " + balance);
    }
}
