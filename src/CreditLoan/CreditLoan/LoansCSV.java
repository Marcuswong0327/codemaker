package src.CreditLoan.CreditLoan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoansCSV {

    public static void exportLoans(LoansRecord loanRecord, String username) {
        // Dynamically create the file path based on the username
        String filePath = "loans_" + username + ".csv";
    
        try (FileWriter writer = new FileWriter(filePath, true)) { // Open in append mode
            // If the file is new, write the header
            boolean isNewFile = new java.io.File(filePath).length() == 0;
            if (isNewFile) {
                writer.write("LoanId,Username,LoanAmount,InterestRate,Months,RemainingAmount,Status,LoanStartDate,AmountPaid\n");
            }
    
            // Append the new loan record including the amountPaid field
            String recordLine = String.format("%d,%s,%.2f,%.2f,%d,%.2f,%s,%s,%.2f\n",
                    loanRecord.getLoanId(),
                    loanRecord.getUsername(),
                    loanRecord.getLoanAmount(),
                    loanRecord.getInterestRate(),
                    loanRecord.getMonths(),
                    loanRecord.getRemainingAmount(),
                    loanRecord.getStatus(),
                    loanRecord.getLoanStartDate(),
                    loanRecord.getAmountPaid());  // Include amountPaid
    
            writer.write(recordLine);
    
            System.out.println("Loan record added successfully to " + filePath);
    
        } catch (IOException e) {
            System.out.println("Error exporting loans: " + e.getMessage());
        }
    }
    

    public static List<LoansRecord> loadLoans(String username) {
        List<LoansRecord> loanRecords = new ArrayList<>();
        String filePath = "loans_" + username + ".csv"; // Assuming each user has a separate loan file
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header if necessary
    
            while ((line = br.readLine()) != null) {
                String[] loanDetails = line.split(",");
                if (loanDetails.length >= 8) { // Adjusted for the additional amountPaid field
                    LoansRecord record = new LoansRecord(
                        Integer.parseInt(loanDetails[0]), // Loan ID
                        loanDetails[1], // Username
                        Double.parseDouble(loanDetails[2]), // Loan amount
                        Double.parseDouble(loanDetails[3]), // Interest rate
                        Integer.parseInt(loanDetails[4]), // Repayment period
                        Double.parseDouble(loanDetails[5]), // Remaining loan amount
                        loanDetails[6], // Status
                        loanDetails[7], // Date
                        Double.parseDouble(loanDetails[8]) // Amount paid
                    );
                    loanRecords.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return loanRecords;
    }
    
    public static void appendRepaymentRecord(LoansRecord loanRecord, String username) {
        String filePath = "loans_" + username + ".csv";
    
        try (FileWriter writer = new FileWriter(filePath, true)) { // Append mode
            String recordLine = String.format("%d,%s,%.2f,%.2f,%d,%.2f,%s,%s,%.2f\n",
                    loanRecord.getLoanId(),
                    loanRecord.getUsername(),
                    loanRecord.getLoanAmount(),
                    loanRecord.getInterestRate(),
                    loanRecord.getMonths(),
                    loanRecord.getRemainingAmount(),
                    loanRecord.getStatus(),
                    loanRecord.getLoanStartDate(),
                    loanRecord.getAmountPaid());
            writer.write(recordLine);
        } catch (IOException e) {
            System.out.println("Error appending repayment record: " + e.getMessage());
        }
    }
    
}
