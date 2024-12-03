package CreditLoan;
import java.text.DecimalFormat;

public class CreditLoan {
    private double loanAmount;    // Loan amount
    private double interestRate;  // Interest rate
    private int months;           // Loan period (months)
    private double monthlyPayment; // Monthly payment
    private double totalRepayment; // Total repayment amount
    private double amountPaid;    // Amount paid so far
    private boolean isLoanPaid;   // Whether the loan is paid off

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
            System.out.println("\nLoan has already been paid in full.");
            return;
        }
        amountPaid += paymentAmount;
        if (amountPaid >= totalRepayment) {
            isLoanPaid = true;
            System.out.println("\nLoan fully repaid!");
        }
        System.out.println("\nAmount Paid: " + df.format(amountPaid));
        System.out.println("Remaining Amount: " + df.format(totalRepayment - amountPaid));
        System.out.println("----------------------\n");
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
}