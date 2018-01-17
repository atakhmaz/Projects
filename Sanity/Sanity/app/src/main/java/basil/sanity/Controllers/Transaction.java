package basil.sanity.Controllers;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by kevinnguyen on 10/2/17.
 */

// Compare by dateAdded
public class Transaction implements Comparable{

    private String itemName;
    private Date transactionDate;
    private double price;
    private String memo;
    private String budgetName;
    private String categoryName;

    public Transaction(String itemName, Date transactionDate, Double price, String memo, String budgetName, String categoryName) {
        this.itemName = itemName;
        this.transactionDate = transactionDate;
        this.price = price;
        this.memo = memo;
        this.budgetName = budgetName;
        this.categoryName = categoryName;
    }

    // allow sorting by date, newest date first
    public int compareTo(Object anotherTransaction) throws ClassCastException {
        if (!(anotherTransaction instanceof Transaction))
            throw new ClassCastException("A Transaction object expected.");
        Date anotherDate = ((Transaction) anotherTransaction).getTransactionDate();
        // negate compareTo so that newest date appears first when sorted
        return -this.transactionDate.compareTo(anotherDate);
    }

    public String getItemName() {
        return itemName;
    }

    public String getBudgetName() { return budgetName; }

    public String getCategoryName() { return categoryName; }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date dateAdded) {
        this.transactionDate = dateAdded;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public boolean equals(Object o) {
        boolean dateCheck = false;

      if((transactionDate.getTime()-((Transaction)o).transactionDate.getTime())<100&&(transactionDate.getTime()-((Transaction)o).transactionDate.getTime())>-100){
            dateCheck = true;
        }

        return itemName.equals(((Transaction)o).itemName) &&
               dateCheck &&
                price  == ((Transaction)o).price &&
                memo.equals(((Transaction)o).memo);
    }

}
