package CreditLoan;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class CreditLoan {
    private double loanAmount;    // Loan amount
    private double interestRate;  // Interest rate
    private int months;           // Loan period (months)
    private double monthlyPayment; // Monthly payment
    private double totalRepayment; // Total repayment amount
    private double amountPaid;    // Amount paid so far
    private boolean isLoanPaid;   // Whether the loan is paid off
    private LocalDate loanStartDate; // Loan start date
    private LocalDate nextPaymentDate; // Next payment date

    // Decimal format for two decimal places
    private static final DecimalFormat df = new DecimalFormat("0.00");

    // Constructor
    public CreditLoan(double loanAmount, double interestRate, int months) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.months = months;
        this.amountPaid = 0;
        this.isLoanPaid = false;
        this.monthlyPayment = calculateMonthlyPayment();
        this.totalRepayment = monthlyPayment * months;
        this.loanStartDate = LocalDate.now(); // Default to current date
        this.nextPaymentDate = loanStartDate.plusMonths(1); // Next payment is one month after loan start date
        //this.nextPaymentDate = LocalDate.now(); // Today(testing purpose)
    }

    // Calculate monthly payment using loan amortization formula
    public double calculateMonthlyPayment() {
        double rate = interestRate / 100 / 12; // Monthly interest rate
        return loanAmount * rate / (1 - Math.pow(1 + rate, -months));  // Loan amortization formula
    }

    // Apply for a loan (display loan details)
    public void loan() {
        System.out.println("\n--- Loan Details ---");
        System.out.println("Loan Amount: " + df.format(loanAmount));
        System.out.println("Interest Rate: " + df.format(interestRate) + "%");
        System.out.println("Loan Period: " + months + " months");
        System.out.println("Monthly Payment: " + df.format(monthlyPayment));
        System.out.println("Total Repayment: " + df.format(totalRepayment));
        System.out.println("----------------------\n");
    }

    // Repay loan
    public void repayLoan(double paymentAmount) {
        if (isLoanPaid) {
            JOptionPane.showMessageDialog(null, "Your loan is already fully paid.", "Loan Status", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        amountPaid += paymentAmount;
        if (amountPaid >= totalRepayment) {
            isLoanPaid = true;
            amountPaid = totalRepayment;
            JOptionPane.showMessageDialog(null, "Loan fully repaid!", "Loan Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Amount Paid: " + df.format(amountPaid) + "\nRemaining Amount: " + df.format(totalRepayment - amountPaid), "Repayment Status", JOptionPane.INFORMATION_MESSAGE);
        }

        // Update the next payment date for next month
        nextPaymentDate = nextPaymentDate.plusMonths(1);
    }


    // Check if the loan is fully paid
    public boolean isLoanPaid() {
        return isLoanPaid;
    }

    // Display loan details
    public void displayLoanDetails() {
        if (isLoanPaid) {
            System.out.println("\n--- Loan Paid Off ---");
        } else {
            System.out.println("\n--- Loan Details ---");
        }
        
        System.out.println("Loan Amount: " + df.format(loanAmount));
        System.out.println("Interest Rate: " + df.format(interestRate) + "%");
        System.out.println("Loan Period: " + months + " months");
        System.out.println("Monthly Payment: " + df.format(monthlyPayment));
        System.out.println("Total Repayment: " + df.format(totalRepayment));
        System.out.println("Amount Paid: " + df.format(amountPaid));
        System.out.println("Remaining Balance: " + df.format(totalRepayment - amountPaid));
        System.out.println("----------------------\n");
    }
    public void displayRepaymentReminder() {
        if (!isLoanPaid) {
            long daysUntilRepayment = ChronoUnit.DAYS.between(LocalDate.now(), nextPaymentDate);
            if (daysUntilRepayment <= 7 && daysUntilRepayment >= 0) {
                String reminderMessage = "Reminder: Your loan repayment is due in " + daysUntilRepayment + " days!\n"
                        + "Next payment date: " + nextPaymentDate;
                JOptionPane.showMessageDialog(null, reminderMessage, "Loan Repayment Reminder",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Loan method (placeholder)
    public void applyLoan() {
        System.out.println("Loan applied successfully!");
    }
}

