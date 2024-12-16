package Transaction;

import java.time.LocalDate;
import java.util.Scanner;

public class Credit {

    private double credit;
    private String description;
    private Scanner scanner = new Scanner(System.in);

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

    // Method to record a credit transaction
    public Transaction recordCredit() {
        System.out.print("Enter credit amount: ");
        credit = scanner.nextDouble();
        if (credit <= 0) {
            System.out.println("Credit amount must be positive!");
            return null;
        }

        System.out.print("Enter description: ");
        scanner.nextLine(); // Clear buffer
        description = scanner.nextLine();

        return new Transaction(LocalDate.now(), description, credit, Transaction.TransactionType.CREDIT);
    }
}




