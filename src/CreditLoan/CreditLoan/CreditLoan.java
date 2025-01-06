package src.CreditLoan.CreditLoan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class CreditLoan {
    private double loanAmount; // Loan amount
    private double interestRate; // Interest rate
    private int months; // Loan period (months)
    private double monthlyPayment; // Monthly payment
    private double totalRepayment; // Total repayment amount
    private double amountPaid; // Amount paid so far
    private boolean isLoanPaid; // Whether the loan is paid off
    private LocalDate loanStartDate; // Loan start date
    private LocalDate nextPaymentDate; // Next payment date
    private String username;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private double remainingAmount; // The amount left to repay

    // Constructor update to initialize remainingAmount
    public CreditLoan(double loanAmount, double interestRate, int months, String username) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.months = months;
        this.username = username;
        this.amountPaid = 0;
        this.isLoanPaid = false;
        this.remainingAmount = loanAmount; // Initialize remainingAmount
        this.monthlyPayment = calculateMonthlyPayment();
        this.totalRepayment = monthlyPayment * months;
        this.loanStartDate = LocalDate.now(); // Default to current date
        this.nextPaymentDate = loanStartDate.plusDays(5); // Next payment is in 5 days
    }

    // Constructor for loaded existing loan (after repayments)
    public CreditLoan(double loanAmount, double interestRate, int months, String username, double remainingAmount,
            double amountPaid) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.months = months;
        this.username = username;
        this.amountPaid = amountPaid;
        this.remainingAmount = remainingAmount;
        this.isLoanPaid = remainingAmount <= 0;
        this.monthlyPayment = calculateMonthlyPayment();
        this.totalRepayment = monthlyPayment * months;
        this.loanStartDate = LocalDate.now(); // Default to current date or load from file
        this.nextPaymentDate = loanStartDate.plusDays(5); // Next payment is in 5 days (or load from file)
    }

    // Calculate monthly payment using loan amortization formula
    public double calculateMonthlyPayment() {
        double rate = interestRate / 100 / 12; // Monthly interest rate
        return loanAmount * rate / (1 - Math.pow(1 + rate, -months)); // Loan amortization formula
    }

    public void loan() {
        System.out.println("\n--- Loan Details ---");
        System.out.println("Loan Amount: " + df.format(loanAmount));
        System.out.println("Interest Rate: " + df.format(interestRate) + "%");
        System.out.println("Loan Period: " + months + " months");
        System.out.println("Monthly Payment: " + df.format(monthlyPayment));
        System.out.println("Total Repayment: " + df.format(totalRepayment));
        System.out.println("----------------------\n");

        // Create a LoansRecord for the new loan application
        LoansRecord loanRecord = new LoansRecord(1, getUsername(), loanAmount, interestRate, months,
                totalRepayment - amountPaid, isLoanPaid ? "Paid" : "Active", LocalDate.now().toString(), amountPaid); // Ensure
                                                                                                                      // amountPaid
                                                                                                                      // is
                                                                                                                      // included

        // Export the loan to CSV (make sure the file path uses username)
        LoansCSV.exportLoans(loanRecord, getUsername()); // Update loan information in CSV
    }

    // Repay loan
    public void repayLoan(double paymentAmount) {
        if (isLoanPaid) {
            JOptionPane.showMessageDialog(null, "Your loan is already fully paid.", "Loan Status",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        amountPaid += paymentAmount;
        if (amountPaid >= totalRepayment) {
            isLoanPaid = true;
            amountPaid = totalRepayment;
            JOptionPane.showMessageDialog(null, "Loan fully repaid!", "Loan Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Amount Paid: " + df.format(amountPaid) + "\nRemaining Amount: "
                            + df.format(totalRepayment - amountPaid),
                    "Repayment Status", JOptionPane.INFORMATION_MESSAGE);
        }

        // Update the next payment date for next month
        nextPaymentDate = nextPaymentDate.plusMonths(1);

        // Create an updated LoansRecord for the repaid loan
        LoansRecord loanRecord = new LoansRecord(
                getLoanId(),
                getUsername(),
                loanAmount,
                interestRate,
                months,
                totalRepayment - amountPaid, // Remaining amount should be the updated value
                isLoanPaid ? "Paid" : "Active",
                LocalDate.now().toString(),
                amountPaid // Make sure amountPaid is updated
        );

        // Update the loan record in the CSV file
        updateLoanCSV(loanRecord, getUsername());
    }

    // Update the loan record in CSV
    public void updateLoanCSV(LoansRecord loanRecord, String username) {
        String loanFilePath = "loans_" + username + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(loanFilePath, true))) {
            // Format loan amount and remaining amount to two decimal places
            String formattedLoanAmount = df.format(loanRecord.getLoanAmount());
            String formattedRemainingAmount = df.format(loanRecord.getRemainingAmount());

            // Construct the CSV line
            String loanData = String.join(",",
                    String.valueOf(loanRecord.getLoanId()),
                    loanRecord.getUsername(),
                    formattedLoanAmount,
                    String.valueOf(loanRecord.getInterestRate()),
                    String.valueOf(loanRecord.getMonths()),
                    formattedRemainingAmount,
                    loanRecord.getStatus(),
                    loanRecord.getLoanStartDate(),
                    df.format(loanRecord.getAmountPaid()));

            // Write the line to the CSV file
            writer.write(loanData);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing loan data to file: " + e.getMessage());
        }
    }

    // Check if the loan is fully paid
    public boolean isLoanPaid() {
        return isLoanPaid;
    }

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
        System.out.println("Amount Paid: " + df.format(amountPaid)); // Show the updated amount paid
        System.out.println("Remaining Balance: " + df.format(totalRepayment - amountPaid)); // Show remaining balance
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
    
    public static CreditLoan loadExistingLoan(String loanFilePath) {
        try {
            //System.out.println("Reading loan file: " + loanFilePath);
            List<String> lines = Files.readAllLines(Paths.get(loanFilePath));
            if (lines.size() <= 1) {
                //System.out.println("No loan data available.");
                return null;
            }
    
            String lastLine = lines.get(lines.size() - 1);
            String[] fields = lastLine.split(",");
    
            if (fields.length < 9) {
                System.out.println("Error: Expected 9 fields, but found " + fields.length);
                return null;
            }
    
            //System.out.println("Parsed fields:");
            for (int i = 0; i < fields.length; i++) {
                //System.out.println("Field " + i + ": " + fields[i]);
            }
    
            double loanAmount = Double.parseDouble(fields[2]);
            double interestRate = Double.parseDouble(fields[3]);
            int months = Integer.parseInt(fields[4]);
            double remainingAmount = Double.parseDouble(fields[5]);
            boolean isLoanPaid = fields[6].equalsIgnoreCase("Paid");
            double amountPaid = Double.parseDouble(fields[8]);
            String username = fields[1];
            // Adjust if needed to handle the absence of nextPaymentDate
            LocalDate nextPaymentDate = LocalDate.now().plusDays(5);

            //LocalDate nextPaymentDate = LocalDate.now().plusMonths(1);
    
            CreditLoan loan = new CreditLoan(loanAmount, interestRate, months, username);
            loan.setRemainingAmount(remainingAmount);
            loan.setAmountPaid(amountPaid);
            loan.setIsLoanPaid(isLoanPaid);
            loan.setNextPaymentDate(nextPaymentDate);
    
            //System.out.println("Loaded loan data successfully for user: " + username);
            return loan;
        } catch (IOException e) {
            //System.out.println("Error reading loan file: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Array index out of bounds: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Number format exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return null;
    }
    
    
    // Loan method (placeholder)
    public void applyLoan() {
        System.out.println("Loan applied successfully!");
    }

    public double getRemainingLoanAmount() {
        return totalRepayment - amountPaid;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getRepaymentPeriod() {
        return months;
    }

    // Getter for username
    public String getUsername() {
        return this.username;
    }

    public int getLoanId() {
        return (String.valueOf(loanAmount) + username).hashCode(); // Convert loanAmount to String and combine with
                                                                   // username
    }

    // Getter for months (loan period)
    public int getMonths() {
        return months;
    }

    // Getter for loan status (Active or Paid)
    public String getStatus() {
        return isLoanPaid ? "Paid" : "Active";
    }

    // Getter for loanStartDate
    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public double getAmountPaid() {
        return this.amountPaid; // Return the current amount paid
    }

    public void setRemainingAmount(double remainingAmount2) {
        this.remainingAmount = remainingAmount2;
    }

    public void setAmountPaid(double amountPaid2) {
        this.amountPaid = amountPaid2;
    }

    public void setIsLoanPaid(boolean isLoanPaid2) {
        this.isLoanPaid = isLoanPaid2;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }
}




