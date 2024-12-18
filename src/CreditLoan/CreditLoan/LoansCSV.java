package src.CreditLoan.CreditLoan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LoansCSV {

    private static final String filePath = "C:\\Users\\oikay\\OneDrive\\Documents\\FOP\\Assignment\\codemaker\\src\\CreditLoan\\CreditLoan\\Loans.csv";

    public static void exportLoans(List<LoansRecord> loans) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Loan ID,User ID,Principal Amount,Interest Rate,Repayment Period,Outstanding Balance,Status,Created At\n");

            for (LoansRecord loan : loans) {
                System.out.println("Exporting Loan: " + loan.getLoanId() + " | User: " + loan.getUserId());
                
                String record = String.format("%d,%s,%.2f,%.2f,%d,%.2f,%s,%s\n",
                        loan.getLoanId(),
                        loan.getUserId(),
                        loan.getPrincipalAmount(),
                        loan.getInterestRate(),
                        loan.getRepaymentPeriod(),
                        loan.getOutstandingBalance(),
                        loan.getStatus(),
                        loan.getCreatedAt());

                writer.write(record);
            }

            System.out.println("Loan records exported successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting loans: " + e.getMessage());
        }
    }
}

