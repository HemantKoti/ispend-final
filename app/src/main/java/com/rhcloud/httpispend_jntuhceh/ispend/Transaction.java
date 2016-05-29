package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 10-03-2016.
 */
public class Transaction {
    String email, transactionType, transactionDate, transactionCategory, transactionAmount, transactionDescription;

    public Transaction(String email, String transactionAmount, String transactionCategory, String transactionDate, String transactionDescription, String transactionType) {
        this.email = email;
        this.transactionAmount = transactionAmount;
        this.transactionCategory = transactionCategory;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.transactionType = transactionType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
