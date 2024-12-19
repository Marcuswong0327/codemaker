package Visualize;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DataVisualization {

    public static void main(String[] args) {
        try {
            // Paths to CSV files
            String savingsPath = "savings.csv";
            String loansPath = "loans.csv";
            String transactionsPath = "transaction_history.csv";

            // Read and visualize savings data
            DefaultCategoryDataset savingsDataset = readSavingsData(savingsPath);
            createBarChart("Savings Growth", "Savings", "Percentage (%)", savingsDataset);

            // Read and visualize loan repayment data
            DefaultCategoryDataset loanRepaymentDataset = readLoanRepaymentData(loansPath);
            createBarChart("Loan Repayment Over Time", "Date", "Remaining Loan ($)", loanRepaymentDataset);

            // Read and visualize transaction data for spending distribution
            DefaultPieDataset transactionDataset = readTransactionData(transactionsPath);
            createPieChart("Spending Distribution (Debit vs Credit)", transactionDataset);

            // Read and visualize total credit per day
            DefaultCategoryDataset dailyCreditDataset = readDailyCreditData(transactionsPath);
            createBarChart("Total Credit Per Day", "Date", "Total Credit ($)", dailyCreditDataset);

        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numeric values in the CSV file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Method to read savings data and label them sequentially
    private static DefaultCategoryDataset readSavingsData(String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip the header

            int savingsCounter = 1; // Counter for Savings labels (Savings 1, Savings 2, etc.)
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 4) {
                    System.out.println("Skipping invalid line in savings file: " + line);
                    continue;
                }
                double percentage = Double.parseDouble(fields[3]);
                String savingsLabel = "Savings " + savingsCounter; // Label for each savings record
                dataset.addValue(percentage, "Savings", savingsLabel);
                savingsCounter++; // Increment the savings label counter
            }
        }
        return dataset;
    }

    // Method to read loan repayment data (remaining loan amount over time)
    private static DefaultCategoryDataset readLoanRepaymentData(String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Assuming date format is YYYY-MM-DD
        TreeMap<Date, Double> loanRepaymentMap = new TreeMap<>(); // TreeMap for chronological order

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 8) {  // Ensure we have at least 8 columns
                    System.out.println("Skipping invalid line in loans file (not enough columns): " + line);
                    continue;
                }
                String loanStartDate = fields[7]; // Assuming loan start date is in column 8 (0-based index 7)
                double remainingAmount = 0;

                try {
                    remainingAmount = Double.parseDouble(fields[4]); // Assuming remaining amount is in column 5 (0-based index 4)
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid remaining amount in line: " + line);
                    continue;
                }

                // Try to parse the date
                try {
                    Date startDate = sdf.parse(loanStartDate);
                    loanRepaymentMap.put(startDate, remainingAmount);
                } catch (ParseException e) {
                    System.out.println("Skipping line with unparseable date: " + line);
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing loan repayment data: " + e.getMessage());
        }

        // Add loan repayment data to the dataset (Date vs Remaining Amount)
        for (Map.Entry<Date, Double> entry : loanRepaymentMap.entrySet()) {
            // Format the date for display
            String formattedDate = sdf.format(entry.getKey());
            dataset.addValue(entry.getValue(), "Remaining Amount", formattedDate);
        }

        return dataset;
    }

    // Method to read transaction data and calculate debit and credit totals
    private static DefaultPieDataset readTransactionData(String filePath) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        double totalDebit = 0.0;
        double totalCredit = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5) {
                    System.out.println("Skipping invalid line in transactions file: " + line);
                    continue;
                }
                double debit = Double.parseDouble(fields[2]);
                double credit = Double.parseDouble(fields[3]);

                totalDebit += debit;
                totalCredit += credit;
            }
        }

        // Calculate the total amount for both debit and credit
        double total = totalDebit + totalCredit;

        // Add debit and credit amounts to the dataset with percentage calculations
        dataset.setValue("Debit (" + String.format("%.2f", (totalDebit / total) * 100) + "%)", totalDebit);
        dataset.setValue("Credit (" + String.format("%.2f", (totalCredit / total) * 100) + "%)", totalCredit);

        return dataset;
    }

    // Method to read transaction data and calculate total credit per day
    private static DefaultCategoryDataset readDailyCreditData(String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> dailyCredit = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 5) {
                    System.out.println("Skipping invalid line in transactions file: " + line);
                    continue;
                }
                String date = fields[0];
                double credit = Double.parseDouble(fields[3]);

                // Add credit to the corresponding date
                dailyCredit.put(date, dailyCredit.getOrDefault(date, 0.0) + credit);
            }
        }

        // Add each date and total credit to the dataset
        for (Map.Entry<String, Double> entry : dailyCredit.entrySet()) {
            dataset.addValue(entry.getValue(), "Credit", entry.getKey());
        }
        return dataset;
    }

    // Method to create a bar chart
    private static void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, DefaultCategoryDataset dataset) {
    // Create the bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Customize the bar chart's appearance
        // Set the color of the bars
        barChart.getCategoryPlot().getRenderer().setSeriesPaint(0, new java.awt.Color(0, 102, 204));  // A clear blue color
    
        // Display the chart
        displayChart(barChart);
    }


    // Method to create a pie chart
    private static void createPieChart(String title, DefaultPieDataset dataset) {
        JFreeChart pieChart = ChartFactory.createPieChart(
                title,
                dataset,
                true, true, false);

        displayChart(pieChart);
    }

    // Method to display a chart
    private static void displayChart(JFreeChart chart) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(chart.getTitle().getText());
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}





