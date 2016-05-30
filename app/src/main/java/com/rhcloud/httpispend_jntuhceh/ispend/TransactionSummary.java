package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 22-03-2016.
 */
public class TransactionSummary {
    String transactionCategory, totalAmount;

    public TransactionSummary(String transactionCategory, String totalAmount) {
        this.transactionCategory = transactionCategory;
        this.totalAmount = totalAmount;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
