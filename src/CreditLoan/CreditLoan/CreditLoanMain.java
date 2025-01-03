package src.CreditLoan.CreditLoan;

import java.util.List;
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

    public static void applyForLoan(Scanner scanner) {
        // Collect user input
        System.out.print("Enter your username: ");
        String username = scanner.next(); // Getting the username

        // Load existing loans for the user from CSV
        List<LoansRecord> loanRecords = LoansCSV.loadLoans(username);

        // Check if there is any active loan
        for (LoansRecord record : loanRecords) {
            if (record.getStatus().equalsIgnoreCase("Active")) {
                System.out.println("You already have an active loan. Repay your loan before applying for a new one.");
                return; // Exit the method if an active loan exists
            }
        }

        // If no active loans, proceed with loan application
        System.out.print("Enter loan amount: ");
        double loanAmount = scanner.nextDouble();

        System.out.print("Enter interest rate (%): ");
        double interestRate = scanner.nextDouble();

        System.out.print("Enter repayment period (months): ");
        int months = scanner.nextInt();

        // Create a new CreditLoan object
        userLoan = new CreditLoan(loanAmount, interestRate, months, username);

        // Calculate the loan details (you may want to use a method like 'loan()' to
        // calculate repayment)
        userLoan.loan();

        // Create a new loan record with the initial amount paid as 0
        LoansRecord updatedLoanRecord = new LoansRecord(
                loanRecords.size() + 1, // Loan ID (auto-increment based on the size of existing records)
                username,
                userLoan.getLoanAmount(),
                userLoan.getInterestRate(),
                userLoan.getRepaymentPeriod(),
                userLoan.getRemainingLoanAmount(),
                "Active", // The loan is now active
                LocalDate.now().toString(),
                0 // Initial amount paid is 0
        );

        // Export the loan record to CSV
        LoansCSV.exportLoans(updatedLoanRecord, username);

        // Inform the user that the loan was successfully applied
        System.out.println("Loan successfully applied!");
    }

    // Repay the loan
    public static void repayLoan(Scanner scanner) {
        if (userLoan == null) {
            System.out.println("No loan to repay.");
            return;
        }
        if (userLoan.isLoanPaid()) {
            System.out.println("Your loan is already fully paid.");
            return;
        }

        // Collect repayment amount from the user
        System.out.print("Enter repayment amount: ");
        double paymentAmount = scanner.nextDouble();

        // Process the loan repayment
        userLoan.repayLoan(paymentAmount);

        // Create an updated LoansRecord with the new loan details
        LoansRecord updatedLoanRecord = new LoansRecord(
                userLoan.getLoanId(), // Use the correct loan ID from the userLoan object
                userLoan.getUsername(),
                userLoan.getLoanAmount(),
                userLoan.getInterestRate(),
                userLoan.getRepaymentPeriod(),
                userLoan.getRemainingLoanAmount(),
                userLoan.isLoanPaid() ? "Paid" : "Active",
                LocalDate.now().toString(),
                userLoan.getAmountPaid() // Include the updated amountPaid
        );

        // Export the updated loan record to CSV
        LoansCSV.exportLoans(updatedLoanRecord, userLoan.getUsername());
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