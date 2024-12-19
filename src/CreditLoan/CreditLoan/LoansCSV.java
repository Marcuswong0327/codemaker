package src.CreditLoan.CreditLoan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LoansCSV {

    private static final String filePath = "loans.csv";

    public static void exportLoans(List<LoansRecord> loanRecords) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("LoanId,UserId,LoanAmount,InterestRate,Months,RemainingAmount,Status,LoanStartDate\n");

            for (LoansRecord record : loanRecords) {
                String recordLine = String.format("%d,%s,%.2f,%.2f,%d,%.2f,%s,%s\n", 
                        record.getLoanId(),
                        record.getUserId(),
                        record.getLoanAmount(),
                        record.getInterestRate(),
                        record.getMonths(),
                        record.getRemainingAmount(),
                        record.getStatus(),
                        record.getLoanStartDate());
                writer.write(recordLine);
            }

            System.out.println("Loans data exported successfully!");

        } catch (IOException e) {
            System.out.println("Error exporting loans: " + e.getMessage());
        }
    }
}


