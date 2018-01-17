package basil.sanity.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import basil.sanity.Database.Database;

/**
 * Created by kevinnguyen on 10/2/17.
 */

public class Category {

    private String budgetName;
    private String categoryName;
    private double categoryLimit;
    private double amountSpent;
    private Settings settings;
    private ArrayList<Transaction> transactionList;

    public Category(String categoryName, double categoryLimit, Settings settings, String budgetName) {
        this.categoryName = categoryName;
        this.categoryLimit = categoryLimit;
        this.amountSpent = 0.0;
        this.settings = settings;
        this.transactionList = new ArrayList<>();
        this.budgetName = budgetName;
    }

    //For testing purposes
    public Category(String categoryName, double categoryLimit, double amountSpent, Settings settings,
                    String budgetName) {
        this.categoryName = categoryName;
        this.categoryLimit = categoryLimit;
        this.amountSpent = amountSpent;
        this.settings = settings;
        this.transactionList = new ArrayList<>();
        this.budgetName = budgetName;
    }

    public Category(String categoryName, double categoryLimit, double amountSpent, Settings settings,
                    ArrayList<Transaction> transactionList, String budgetName) {
        this.categoryName = categoryName;
        this.categoryLimit = categoryLimit;
        this.amountSpent = amountSpent;
        this.settings = settings;
        this.transactionList = transactionList;
        this.budgetName = budgetName;
    }

    public String getBudgetName() { return  budgetName; }

    public String getCategoryName() {
        return categoryName;
    }

    public double getCategoryLimit() {
        return categoryLimit;
    }

    public void setCategoryLimit(double categoryLimit) {
        this.categoryLimit = categoryLimit;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Date getStartDate() { return settings.getCategoryPeriod().getStartDate(); }

    public Date getEndDate() { return settings.getCategoryPeriod().getEndDate(); }

    public double getThreshold() { return settings.getThreshold(); }

    public int getFrequencyValue() { return settings.getFrequencyValue(); }

    public String getGetFrequencyType() { return settings.getFrequencyType(); }

    public ArrayList<Transaction> getTransactionList() {
        return transactionList;
    }

    //Returns true upon first time budget limit exceeded
    public boolean addTransaction(Transaction transaction) {
        boolean wasOverBudget = isOverThreshold();
        transactionList.add(transaction);
        Collections.sort(transactionList);
        amountSpent += transaction.getPrice();
        //Check if first
        if (isOverThreshold() && !wasOverBudget) {
            return true;
        }
        return false;
    }

    public double getPercentageSpent(){
        return amountSpent/categoryLimit;
    }

    public boolean isOverThreshold() {
        return amountSpent > (categoryLimit*settings.getThreshold());
    }

    public void rollover(String username) {
        double rolloverAmount = amountSpent - categoryLimit;
        amountSpent = 0.0;
        transactionList.clear();
        settings.rollover();
        if(rolloverAmount > 0.0){
            amountSpent = rolloverAmount;
            Transaction transaction = new Transaction("Rollover From Last Period",
                    new Date(),
                    rolloverAmount,
                    "This transaction was added because you went over the limit in the last period.",
                    budgetName,
                    categoryName);
            transactionList.add(transaction);
            Database db = null;
            try {
                db = new Database();
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.AddTransaction(transaction, username, budgetName, categoryName);
        }
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        return categoryName.equals(((Category)o).categoryName) &&
                categoryLimit == ((Category)o).categoryLimit &&
                amountSpent == ((Category)o).amountSpent &&
                transactionList.equals(((Category)o).transactionList) &&
                settings.equals(((Category)o).settings);
    }
}
