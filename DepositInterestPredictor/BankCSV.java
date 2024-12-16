package DepositInterestPredictor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BankCSV {

    private static final String FILE_PATH = "banks.csv";

    public static void exportBanks(List<Bank> banks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("bank_id,bank_name,interest_rate\n");

            for (Bank bank : banks) {
                String record = String.format("%d,%s,%.2f\n",
                        bank.getBankId(),
                        bank.getBankName(),
                        bank.getInterestRate());
                writer.write(record);
            }

            System.out.println("File Exported!");
        } catch (IOException e) {
            System.out.println("Error exporting banks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Bank> banks = List.of(
                new Bank(1, "RHB", 2.6),
                new Bank(2, "Maybank", 2.5),
                new Bank(3, "Hong Leong", 2.3),
                new Bank(4, "Alliance", 2.85),
                new Bank(5, "AmBank", 2.55),
                new Bank(6, "Standard Chartered", 2.65)
        );

        exportBanks(banks);
    }
}