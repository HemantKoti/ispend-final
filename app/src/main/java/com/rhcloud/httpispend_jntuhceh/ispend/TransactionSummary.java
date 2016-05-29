package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 29-05-2016.
 */
public class TransactionSummary {
    String totalBudget, totalSpends, availableAmount;

    public TransactionSummary(String availableAmount, String totalBudget, String totalSpends) {
        this.availableAmount = availableAmount;
        this.totalBudget = totalBudget;
        this.totalSpends = totalSpends;
    }
}
