package src.CreditLoan.CreditLoan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoansRecord {
    private int loanId;
    private String username;
    private double loanAmount;
    private double interestRate;
    private int months;
    private double remainingAmount;
    private String status;
    private String loanStartDate;
    private double amountPaid;

    // Constructor
    public LoansRecord(int loanId, String username, double loanAmount, double interestRate, int months,
            double remainingAmount, String status, String loanStartDate, double amountPaid) {
        this.loanId = loanId;
        this.username = username;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.months = months;
        this.remainingAmount = remainingAmount;
        this.status = status;
        this.loanStartDate = loanStartDate;
        this.amountPaid = amountPaid;
    }

    // Method to convert loan record to CSV format
    public String toCSVString() {
        return loanId + "," + username + "," + loanAmount + "," + interestRate + "," + months + ","
                + remainingAmount + "," + status + "," + loanStartDate + "," + amountPaid;
    }



    // Getters
    public int getLoanId() {
        return loanId;
    }

    public String getUsername() {
        return username;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getMonths() {
        return months;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getLoanStartDate() {
        return loanStartDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    // Setters
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to calculate total repayment
    public double getTotalRepayment() {
        return loanAmount * (1 + (interestRate / 100) * months);
    }

    public static LoansRecord loadAndUpdateLoanDetails(String username) {
        String filePath = "loans_" + username + ".csv";
        List<LoansRecord> records = new ArrayList<>();
        LoansRecord latestLoan = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("LoanId"))
                    continue; // Skip header
                String[] data = line.split(",");
                int loanId = Integer.parseInt(data[0]);
                String userId = data[1];
                double loanAmount = Double.parseDouble(data[2]);
                double interestRate = Double.parseDouble(data[3]);
                int months = Integer.parseInt(data[4]);
                double remainingAmount = Double.parseDouble(data[5]);
                String status = data[6];
                String loanStartDate = data[7];
                double amountPaid = Double.parseDouble(data[8]);

                latestLoan = new LoansRecord(loanId, userId, loanAmount, interestRate, months, remainingAmount, status,
                        loanStartDate, amountPaid);
                records.add(latestLoan);
            }

            // Aggregate repayments for the latest loan
            if (latestLoan != null) {
                double totalPaid = records.stream().mapToDouble(LoansRecord::getAmountPaid).sum();
                latestLoan.setAmountPaid(totalPaid);
                latestLoan.setRemainingAmount(latestLoan.getTotalRepayment() - totalPaid);
                latestLoan.setStatus(totalPaid >= latestLoan.getTotalRepayment() ? "Paid" : "Active");

                // Overwrite the file with updated data
                overwriteLoanCSV(filePath, records);
            }

        } catch (IOException e) {
            System.out.println("Error loading loans: " + e.getMessage());
        }

        return latestLoan;
    }

    private static void overwriteLoanCSV(String filePath, List<LoansRecord> records) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(
                    "LoanId,UserId,LoanAmount,InterestRate,Months,RemainingAmount,Status,LoanStartDate,AmountPaid\n");
            for (LoansRecord record : records) {
                writer.write(String.format("%d,%s,%.2f,%.2f,%d,%.2f,%s,%s,%.2f\n",
                        record.getLoanId(),
                        record.getUsername(),
                        record.getLoanAmount(),
                        record.getInterestRate(),
                        record.getMonths(),
                        record.getRemainingAmount(),
                        record.getStatus(),
                        record.getLoanStartDate(),
                        record.getAmountPaid()));
            }
        } catch (IOException e) {
            System.out.println("Error overwriting loan CSV: " + e.getMessage());
        }
    }

}
