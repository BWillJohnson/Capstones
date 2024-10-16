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
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

    }

    private static void displayDeposits() {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {
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
                    LocalDate startD = LocalDate.now().withDayOfMonth(1);
                    LocalDate cMonth = LocalDate.now();
                    filterTransactionsByDate(cMonth, startD);
                    break;
                case "2":
                    LocalDate startMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate endMonth = LocalDate.now().withDayOfMonth(1).minusDays(1);
                    filterTransactionsByDate(startMonth, endMonth);
                    break;

                case "3":
                    LocalDate startY = LocalDate.now().withDayOfYear(1);
                    LocalDate cYear = LocalDate.now();
                    filterTransactionsByDate(cYear, startY);
                    break;
                case "4":
                    LocalDate rN = LocalDate.now();
                    LocalDate startOfPreYear = rN.minusYears(1).withDayOfYear(1);
                    LocalDate endOfPreYear = rN.minusYears(1).withMonth(12).withDayOfMonth(31);
                    filterTransactionsByDate(startOfPreYear,endOfPreYear);
                    break;
                case "5":
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
        boolean exactDate = false;

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            // Check if the transaction date is within the range
            if (((transactionDate.isBefore(startDate) || (transactionDate.isEqual(startDate))) &&
                    (transactionDate.isAfter(endDate) || transactionDate.isEqual(endDate)))) {
                System.out.println(transaction);
              exactDate = true;
            }
            if (!transactionDate.isBefore(startDate)&& !transactionDate.isAfter(endDate)){
                System.out.println(transaction);
                exactDate = true;
            }

        }

        if (!exactDate) {
            System.out.println("No transactions found within the specified date range.");
        }

    }


    private static void filterTransactionsByVendor(String vendorName) {
        boolean exactName = false;
        for (Transaction transaction : transactions) {
            if (transaction.getVendorName().equalsIgnoreCase(vendorName)) {
                System.out.println(transaction);
                exactName = true;
            }

        }
    }
}