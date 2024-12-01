package Transaction;

import java.util.Scanner;

public class Credit {
    private double credit = 0;
    private String description = "";

    Scanner k = new Scanner(System.in);

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

    public void creditDetails() {
        System.out.println("Enter amount: ");
        credit = k.nextDouble(); // The credit needs to be a positive amount
        if (credit <= 0) {
            System.out.println("Credit amount must be positive!");
            return;
        }
        if (credit > Debit.getBalance()) { // Ensure sufficient balance
            System.out.println("Insufficient balance! Current balance: " + Debit.getBalance());
            return;
        }
        Debit.setBalance(Debit.getBalance() - credit); // Deduct credit amount from shared balance
        System.out.println("Enter description: ");
        k.nextLine(); // Consume the leftover newline
        description = k.nextLine();

        System.out.println("Credit Successfully Recorded!!!");
        System.out.println("New Balance: " + Debit.getBalance());
    }
}


