package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendorName;
    private double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendorName, double amount)
    {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendorName = vendorName;
        this.amount = amount;
    }

    public Transaction()
    {
        getDate();
        getTime();
        getDescription();
        getVendorName();
        getAmount();
    }



    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return "Transaction{" +
                "date=" + date +
                ", time=" + time +
                ", description='" + description + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
