package basil.sanity.Controllers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import basil.sanity.Database.Database;

/**
 * Created by Andre Takhmazyan on 11/10/2017.
 */

public class BudgetManager {

    private static BudgetManager instance;

    private String username;
    private ArrayList<Budget> budgets;

    public BudgetManager(ArrayList<Budget> budgetList, String username)
    {
        budgets = budgetList;
        this.instance = this;
        this.username = username;
    }

//    public BudgetManager(String username){
//        this.username = username;
//        budgets = new ArrayList<>();
//        //Should add 1 budget with name "My Budget"
//        budgets.add(new Budget("My Budget", username));
//    }

    public static BudgetManager getInstance() {
        return instance;
    }

    public static boolean isInitialized() {
        return !(instance == null);
    }

    public Budget get(int index){
        return budgets.get(index);
    }

    public int getBudgetListSize() { return budgets.size(); }

    public String getBudgetName(int position) { return budgets.get(position).getBudgetName(); }

    public double getBudgetPercentageSpent(int position) {
        return budgets.get(position).getBudgetPercentageSpent();
    }

    public void addBudget(Budget myBudget) {
        budgets.add(myBudget);
    }

    //This rolls over the budget so that any amount that was over the limit is added to the next
    //Category period and also added in the database
    public void rollover(String budgetName, String categoryName) {
        budgets.get(findBudget(budgetName)).rollover(categoryName);
    }

    public void addBudget(String budgetName) {
        Budget budget = new Budget(budgetName, username);
        budgets.add(budget);
        Database db = null;
        try{
            db = new Database();
        }
        catch (Exception e){

        }
        Log.d("Adding budget username", username);
        db.AddBudget(budget, username);
    }

    public void removeBudget(int position) {
        budgets.remove(position);
    }

    public void removeBudget(String budgetName) {
        Log.d("Deleting budget", budgetName);
        int position = findBudget(budgetName);
        Log.d("Position", Integer.toString(position));
        if (position != -1) {
            Database db = null;
            try{
                db = new Database();
            }
            catch (Exception e){

            }
            Log.d("Deleting", budgets.get(position).getBudgetName());
            db.DeleteBudget(budgets.get(position), username);
            removeBudget(position);
        }
    }

    public int findBudget(String budgetName) {
        for (int i = 0; i < budgets.size(); ++i) {
            if (budgets.get(i).getBudgetName().toLowerCase().equals(budgetName.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    public Budget getBudgetByName(String budgetName) {
        return budgets.get(findBudget(budgetName));
    }

    public String getUsername() { return username; }

    public boolean isUnique(String budgetName){
        return findBudget(budgetName) == -1;
    }
}
