import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Transaction.Credit;
import Transaction.Debit;
import Transaction.Transaction;
import Transaction.TransactionCSV;

public class Main {
    public static void main(String[] args) {
        boolean loop = true;
        Scanner k = new Scanner(System.in);
        String key = "";

        List<Transaction> transactions = new ArrayList<>(); // Store all transactions

        do {
            
            System.out.println("== Transaction ==");
            System.out.println("1.Debit");
            System.out.println("2.Credit");
            System.out.println("3.History");
            System.out.println("4.Savings");
            System.out.println("5.Credit loan");
            System.out.println("6.Deposit Interest Predictor");
            System.out.println("7.Logout");

            System.out.println();
            System.out.print(">");
            key = k.next();

            // use switch control
            switch (key) {
                case "1":
                    System.out.println("== Debit ==");
                    Debit debit = new Debit();
                    Transaction debitTransaction = debit.recordDebit(); // Record debit and get transaction
                    if (debitTransaction != null) {
                        transactions.add(debitTransaction); // Add to list if not null
                    }
                    break;
                case "2":
                    System.out.println("== Credit ==");
                    Credit credit = new Credit(); // Create an object of Credit.java
                    Transaction creditTransaction = credit.recordCredit(); // Record credit and get transaction
                    if (creditTransaction != null) {
                        transactions.add(creditTransaction); // Add to list if not null
                    }
                    break;
                case "3":
                    System.out.println("== Savings ==");
                    break; 
                case "4":
                    System.out.println("== History ==");
                    break; 
                case "5":
                    System.out.println("== Credit loan ==");
                    break;
                case "6":
                    System.out.println("== Deposit Interest Predictor ==");
                    break;
                case "7":
                    System.out.println("Thank you for using \" Ledger System AlgoNauts \"");
                    loop = false;
                    break;      
                default:
                    System.out.println("Error. Please select again.");
                    break;
            }

        } while (loop);
        
        k.close();
    }
}

