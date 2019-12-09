package com.israt.tourmate.PojoClass;

public class Expense {
    private String tripId,expenseId,expenseName,expenseAmount;

    public Expense() {
    }

    public Expense(String tripId, String expenseId, String expenseName, String expenseAmount) {
        this.tripId = tripId;
        this.expenseId = expenseId;
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
    }

    public String getTripId() {
        return tripId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }
}
