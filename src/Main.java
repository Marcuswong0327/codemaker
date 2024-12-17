package src;

import java.util.Scanner;

import src.Transaction.Credit;
import src.Transaction.Debit;
import src.Transaction.Transaction;
import src.Savings.savings.SavingsSettings;
import src.TransactionHistory;
import src.CreditLoan.CreditLoan.CreditLoan;

public class Main {
    public static void main(String[] args) {
        boolean loop = true;
        Scanner k = new Scanner(System.in);
        String key = "";

        // Create an instance of TransactionHistory to manage transactions
        TransactionHistory transactionHistory = new TransactionHistory(0.00); // Initialize with a balance
        // Create an instance of SavingsSettings to handle savings features
        SavingsSettings savingsSettings = new SavingsSettings(transactionHistory);

        // CreditLoan system
        CreditLoan creditLoan = null;  // Reference to track user's credit loan

        System.out.print("Enter your user name: ");
        String userName = k.next();
        // Display user account welcome screen
        System.out.println("\n== Welcome, " + userName + " ==");
        displayAccountSummary(transactionHistory, savingsSettings, creditLoan);

        do {
            System.out.println("\n== Transaction Menu ==");
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.println("3. View Transaction History");
            System.out.println("4. Savings");
            System.out.println("5. Credit Loan");
            System.out.println("6. Deposit Interest Predictor");
            System.out.println("7. Logout");

            System.out.print("\n> ");
            key = k.next();

            switch (key) {
                case "1":
                    System.out.println("\n== Debit ==");
                    Debit debit = new Debit(savingsSettings);
                    Transaction debitTransaction = debit.recordDebit();
                    if (debitTransaction != null) {
                        transactionHistory.addTransaction(debitTransaction);
                        System.out.println("Debit successfully recorded!");
                    } else {
                        System.out.println("Debit transaction failed.");
                    }
                    break;

                case "2":
                    System.out.println("\n== Credit ==");
                    Credit credit = new Credit();
                    Transaction creditTransaction = credit.recordCredit();
                    if (creditTransaction != null) {
                        transactionHistory.addTransaction(creditTransaction);
                        System.out.println("Credit successfully recorded!");
                    } else {
                        System.out.println("Credit transaction failed.");
                    }
                    break;

                case "3":
                    transactionHistory.viewTransactionHistory();
                    break;

                case "4":
                    System.out.println("\n== Savings ==");
                    savingsSettings.activateSavingsFlow();
                    break;

                case "5":
                    System.out.println("\n== Credit Loan ==");
                    if (creditLoan == null) {
                        creditLoan = handleCreditLoanFlow(k);
                    } else {
                        handleCreditLoanOptions(k, creditLoan);
                    }
                    break;

                case "6":
                    System.out.println("\n== Deposit Interest Predictor ==");
                    DepositInterestPredictor.main(new String[]{}); // Call the Deposit Interest Predictor
                    break;

                case "7":
                    System.out.println("\nThank you for using \"Ledger System AlgoNauts\".");
                    loop = false;
                    break;

                default:
                    System.out.println("\nError. Invalid selection, please choose again.");
                    break;
            }

            transactionHistory.saveTransactionHistory(); // Save transaction history after every operation
        } while (loop);

        k.close();
    }

    // Handles credit loan application flow
    public static CreditLoan handleCreditLoanFlow(Scanner scanner) {
        System.out.println("You are applying for a new Credit Loan.");
        System.out.print("Enter loan amount: ");
        double loanAmount = scanner.nextDouble();
        System.out.print("Enter interest rate (%): ");
        double interestRate = scanner.nextDouble();
        System.out.print("Enter repayment period (months): ");
        int months = scanner.nextInt();

        CreditLoan newLoan = new CreditLoan(loanAmount, interestRate, months);
        newLoan.loan();
        return newLoan;
    }

    // Handles existing loan repayment and viewing loan details
    public static void handleCreditLoanOptions(Scanner scanner, CreditLoan loan) {
        while (true) {
            System.out.println("\n== Loan Options ==");
            System.out.println("1. Repay Loan");
            System.out.println("2. View Loan Details");
            System.out.println("3. Exit Loan Options");
            System.out.print("> ");

            int loanChoice = scanner.nextInt();
            if (loanChoice == 1) {
                System.out.print("Enter repayment amount: ");
                double repayment = scanner.nextDouble();
                loan.repayLoan(repayment);
            } else if (loanChoice == 2) {
                loan.displayLoanDetails();
            } else if (loanChoice == 3) {
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Display the account balance summary
    public static void displayAccountSummary(TransactionHistory transactionHistory, SavingsSettings savings, CreditLoan loan) {
        System.out.println("\nBalance: " + String.format("%.2f", transactionHistory.getCurrentBalance()));
        System.out.println("Savings: " + String.format("%.2f", savings.getSavingsBalance()));
        if (loan != null) {
            System.out.println("Loan: " + String.format("%.2f", loan.getRemainingLoanAmount()));
        } else {
            System.out.println("Loan: 0.00");
        }
    }
}






