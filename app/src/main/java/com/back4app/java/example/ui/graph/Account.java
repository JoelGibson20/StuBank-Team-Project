package com.back4app.java.example.ui.graph;

import java.util.Date;

public class Account {
    private String ID;
    private String accountOwner;
    private String sortCode;
    private String accountNumber;
    private String accountName;
    private Date createdAt;
    private String balance;

    public Account(String ID, String accountOwner, String sortCode, String accountNumber, String accountName, Date createdAt, String balance) {
        this.ID = ID;
        this.accountOwner = accountOwner;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.createdAt = createdAt;
        this.balance = balance;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString(){
        return accountName;
    }
}
