package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {
              //Developed-By.Brandon Johnson
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args)
    {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running)
        {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    public static void loadTransactions(String fileName) {
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        try {
            BufferedReader BR = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = BR.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                    String description = parts[2];
                    String vendorName = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    Transaction transaction = new Transaction(date, time, description, vendorName, amount);
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR! reading file");
        }

    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        System.out.println("Please; Enter the date,time,description, vendor, and amount of a deposit.");
        System.out.println("Notice the format of your Date,Time should look like this yyyy-MM-dd HH:mm:ss,:  ");
        System.out.println("The Deposit should be positive!");

        System.out.println("Enter localDate:\n");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Enter localTime:\n");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Enter the description of your item:\n ");
        String description = scanner.nextLine();

        System.out.println("Enter your vendorName:\n ");
        String vendor = scanner.nextLine();

        System.out.println("Enter amount of your deposit:\n ");
        double addDeposit = scanner.nextDouble();
        if (addDeposit <= 0) {
            System.err.println("Invalid input ");
            return;
        }
        transactions.add(new Transaction(date, time, description, vendor, addDeposit));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String output = String.format("%s|%s|%s|%s|%.2f%n", date.format(DATE_FORMATTER), time.format(TIME_FORMATTER), description, vendor, addDeposit);
            writer.write(output);
            System.out.println("Notice! Deposit was successful!");
            scanner.nextLine();
        } catch (IOException e) {
            System.err.println("Error! adding Deposit");
        }
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number than transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        System.out.println("Enter information provided to add a payment. ");

        System.out.println("Enter localDate:\n");
        LocalDate lDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Enter localTime:\n");
        LocalTime lTime = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Enter the description of your item:\n ");
        String txt = scanner.nextLine();

        System.out.println("Enter your vendorName:\n ");
        String vn = scanner.nextLine();

        System.out.println("Enter amount of your payment:\n ");
        double payment = scanner.nextDouble();

        if (payment > 0) {
            System.out.println("Great your payment has now been updated.");
        }
        double addPay = payment * -1;
        System.out.println(" This amount " + addPay + " has been deducted from your account.");

        transactions.add(new Transaction(lDate, lTime, txt, vn, addPay));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String output = String.format("%s|%s|%s|%s|%.2f%n", lDate.format(DATE_FORMATTER), lTime.format(TIME_FORMATTER), txt, vn, addPay);
            writer.write(output);
            System.out.println("Notice! Payment successful");
            scanner.nextLine();
        } catch (IOException e) {
            System.err.println("ERROR! adding payment");
        }

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.err.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate startD = LocalDate.now();
                    LocalDate cMonth = LocalDate.now().withDayOfMonth(1);
                    filterTransactionsByDate(cMonth, startD);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate endD = LocalDate.now();
                    LocalDate pMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    filterTransactionsByDate(pMonth, endD);
                    break;

                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate startY = LocalDate.now();
                    LocalDate cYear = LocalDate.now().withDayOfYear(1);
                    filterTransactionsByDate(cYear, startY);
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate endY = LocalDate.now();
                    LocalDate pYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    filterTransactionsByDate(pYear, endY);
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                    System.out.println("Enter vendor name: ");
                    String vendorName = scanner.nextLine();
                    filterTransactionsByVendor(vendorName);
                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.

        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            // Check if the transaction date is within the range
            if ((transactionDate.isAfter(startDate) || (transactionDate.isEqual(startDate)) &&
                    (transactionDate.isBefore(endDate) || transactionDate.isEqual(endDate)))) {
                System.out.println(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found within the specified date range.");
        }

    }

    private static void filterTransactionsByVendor(String vendorName) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        boolean exactName = false;
        for (Transaction transaction : transactions) {
            if (transaction.getVendorName().equalsIgnoreCase(vendorName)) {
                System.out.println(transaction);
                exactName = true;
            }

        }
    }
}