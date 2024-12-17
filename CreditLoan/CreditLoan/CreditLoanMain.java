package CreditLoan;

import java.util.Scanner;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class CreditLoanMain {
    private static CreditLoan userLoan = null; // Current user's loan

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Credit Loan System");
            System.out.println("1. Apply for Credit Loan");
            System.out.println("2. Repay Loan");
            System.out.println("3. View Loan Details");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                applyForLoan(scanner);
            } else if (choice == 2) {
                repayLoan(scanner);
            } else if (choice == 3) {
                viewLoanDetails();
            } else if (choice == 4) {
                System.out.println("Goodbye!");
                break;
            }
        }
        scanner.close();
    }

    //Apply
    public static void applyForLoan(Scanner scanner) {
        if (userLoan != null && !userLoan.isLoanPaid()) {
            System.out.println("You already have an active loan. Repay your loan before applying for a new one.");
            return;
        }
        
        System.out.print("Enter loan amount: ");
        double loanAmount = scanner.nextDouble();
        System.out.print("Enter interest rate (%): ");
        double interestRate = scanner.nextDouble();
        System.out.print("Enter repayment period (months): ");
        int months = scanner.nextInt();

        // Create a new CreditLoan object
        userLoan = new CreditLoan(loanAmount, interestRate, months);
        userLoan.loan();  // Call loan method instead of applyLoan
    }

    //Repay
    public static void repayLoan(Scanner scanner) {
        if (userLoan == null) {
            System.out.println("No loan to repay.");
            return;
        }
        if (userLoan.isLoanPaid()) {
            System.out.println("Your loan is already fully paid.");
            return;
        }
        
        System.out.print("Enter repayment amount: ");
        double paymentAmount = scanner.nextDouble();
        userLoan.repayLoan(paymentAmount);
    }

    public static void viewLoanDetails() {
        if (userLoan == null) {
            System.out.println("No loan details available.");
        } else {
            userLoan.displayLoanDetails();
            userLoan.displayRepaymentReminder();
        }
    }
}