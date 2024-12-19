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
import java.util.HashMap;
import java.util.Map;

public class DataVisualization {

    public static void main(String[] args) {
        try {
            // Paths to CSV files
            String savingsPath = "savings.csv";
            String loansPath = "loans.csv";
            String transactionsPath = "transaction.csv";

            // Read and visualize savings data
            DefaultCategoryDataset savingsDataset = readSavingsData(savingsPath);
            createBarChart("Savings Growth", "User ID", "Percentage", savingsDataset);

            // Read and visualize loan data
            DefaultPieDataset loanDataset = readLoanData(loansPath);
            createPieChart("Loan Status Distribution", loanDataset);

            // Read and visualize transaction data
            DefaultCategoryDataset transactionDataset = readTransactionData(transactionsPath);
            createLineChart("Spending Trends", "Date", "Balance", transactionDataset);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to read savings data
    private static DefaultCategoryDataset readSavingsData(String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine(); // Skip header

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            String userId = fields[1];
            double percentage = Double.parseDouble(fields[3]);
            dataset.addValue(percentage, "Savings", userId);
        }

        reader.close();
        return dataset;
    }

    // Method to read loan data
    private static DefaultPieDataset readLoanData(String filePath) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine(); // Skip header

        Map<String, Integer> loanStatusCount = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            String status = fields[6];
            loanStatusCount.put(status, loanStatusCount.getOrDefault(status, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : loanStatusCount.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        reader.close();
        return dataset;
    }

    // Method to read transaction data
    private static DefaultCategoryDataset readTransactionData(String filePath) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine(); // Skip header

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            String date = fields[0];
            double balance = Double.parseDouble(fields[4]);
            dataset.addValue(balance, "Balance", date);
        }

        reader.close();
        return dataset;
    }

    // Method to create a bar chart
    private static void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, DefaultCategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

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

    // Method to create a line chart
    private static void createLineChart(String title, String categoryAxisLabel, String valueAxisLabel, DefaultCategoryDataset dataset) {
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        displayChart(lineChart);
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
