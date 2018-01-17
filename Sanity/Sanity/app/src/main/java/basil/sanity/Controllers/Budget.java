package basil.sanity.Controllers;

import java.util.ArrayList;
import java.util.Date;

import basil.sanity.Database.Database;

/**
 * Created by kevinnguyen on 10/2/17.
 */

public class Budget {

   // private static Budget instance = null;

    private ArrayList<Category> categories;
    private double overallLimit;
    private double overallSpent;
    //private Settings settings;
    private String username;
    private String budgetName;

    //This gets called if first time logging in on new user
    public Budget(String budgetName, String username) {
        categories = new ArrayList<>();
        overallSpent = 0.0;
        //settings = new Settings();
        this.username = username;
        //Default name is My Budget
        this.budgetName = budgetName;
        Category food = new Category("Food", 350.0, 0.0, new Settings(), budgetName);
        categories.add(food);
        Category rent = new Category("Rent", 1000.0, 0.0, new Settings(), budgetName);
        categories.add(rent);
        Category transportation = new Category("Transportation", 100, 0.0, new Settings(), budgetName);
        categories.add(transportation);
        Category recreation = new Category("Recreation", 150, 0.0, new Settings(), budgetName);
        categories.add(recreation);
        Category other = new Category("Other", 100, 0.0, new Settings(), budgetName);
        categories.add(other);
        overallLimit = 1700.0;
    }


    public Budget(String budgetName, ArrayList<Category> categories, double overallLimit,
                  double overallSpent,  String username) {
        this.categories = categories;
        this.overallLimit = overallLimit;
        this.overallSpent = overallSpent;
        //this.settings = settings;
        this.username = username;
        //instance = this;
        this.budgetName = budgetName;
    }
    public Budget(ArrayList<Category> categories, double overallLimit,
                  double overallSpent, String username,String budgetName) {
        this.categories = categories;
        this.overallLimit = overallLimit;
        this.overallSpent = overallSpent;
        //this.settings = settings;
        this.username = username;
        this.budgetName = budgetName;
        //instance = this;
        this.budgetName = budgetName;
    }
    public String getBudgetName(){
        return budgetName;
    }

    public void setBudgetName(String name) {
        budgetName = name;
    }

    public boolean addCategory(Category category) {
        if (category != null) {
            categories.add(category);
            overallSpent += category.getAmountSpent();
            overallLimit += category.getCategoryLimit();

            Database db = null;
            try {
                db = new Database();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (db != null) {
                return db.AddCategory(category, budgetName, username);
            }
            return true;
        }
        return false;
    }

    public boolean addTransaction(Transaction transaction, int index) {
        boolean isFirstTimeOver = false;
        overallSpent += transaction.getPrice();
        Category category = categories.get(index);
        Database db = null;
        try {
            db = new Database();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null) {
//            db.addTransactionDB(transaction, categories.get(index).getCategoryName(), username);
            // No need to get category by index again since it's already saved above
            db.AddTransaction(transaction, username, budgetName, category.getCategoryName());
            //Check if first time over category
            isFirstTimeOver = category.addTransaction(transaction);

            if(isFirstTimeOver){
                Notification nc = new Notification();
                try {
                    nc.overBudgetNotification(ContextSingleton.getInstance(),username, category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return isFirstTimeOver;
    }

    public void addTransactionNotDB(Transaction transaction, int index) {
        overallSpent += transaction.getPrice();
        Category category = categories.get(index);
        category.addTransaction(transaction);
    }

    public void rollover(String categoryName) {
        Category cat = categories.get(findCategory(categoryName));
        overallSpent -= cat.getAmountSpent();
        double rolloverAmount = cat.getAmountSpent() - cat.getCategoryLimit();
        cat.rollover(username);
        if(rolloverAmount > 0){
            overallSpent += rolloverAmount;
        }
    }

    public boolean removeCategory(int index) {
        if (index < 0 || index > categories.size() - 1) {
            return false;
        }
        overallSpent -= categories.get(index).getAmountSpent();
        overallLimit -= categories.get(index).getCategoryLimit();
        Database db = null;
        try {
            db = new Database();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.DeleteCategory(categories.get(index), budgetName, username);
        categories.remove(index);
        return true;
    }

    public boolean removeCategory(String categoryName) {
        return removeCategory(findCategory(categoryName));
    }

//    public boolean checkBudget(int index) {
//        return categories.get(index).isOverThreshold();
//    }

//    public boolean checkBudget(Category category) {
//        return category.isOverThreshold();
//    }

    public double getBudgetPercentageSpent(){
        return overallSpent/overallLimit;
    }

    public String getCategoryName(int index) { return categories.get(index).getCategoryName(); }

    public double getPercentageSpent (int index) {
        return categories.get(index).getAmountSpent()/ categories.get(index).getCategoryLimit();
    }

    public int getCategoryListSize() { return categories.size(); }

//    public Date getStartDate () { return settings.getCategoryPeriod().getStartDate(); }
    public Date getStartDate (int index) { return categories.get(index).getSettings().getCategoryPeriod().getStartDate(); }

//    public Date getEndDate () { return settings.getCategoryPeriod().getEndDate(); }
    public Date getEndDate (int index) { return categories.get(index).getSettings().getCategoryPeriod().getEndDate(); }

//    public double getThreshold () { return settings.getThreshold(); }
    public double getThreshold (int index) { return categories.get(index).getSettings().getThreshold(); }

    public double getBudgetLimit () { return overallLimit; }
    public double getBudgetSpent () { return overallSpent; }

    public double getCategoryLimit (int index) { return categories.get(index).getCategoryLimit(); }
    public double getCategorySpent (int index) { return categories.get(index).getAmountSpent(); }

    public ArrayList<Category> getCategories() { return categories; }

    public Category getCategory (int index) { return categories.get(index); }

    public String getUsername () { return username;}

    //public void editBudgetSetting(Settings settings) {
//       this.settings = settings;
//    }

    public void editCategorySetting(int index, Settings setting) {
        categories.get(index).setSettings(setting);
    }

    public void editBudgetLimit(double limit){
        overallLimit = limit;
    }

    public void editCategoryLimit(int index, double limit){
        categories.get(index).setCategoryLimit(limit);
    }

    public int findCategory(String categoryName){
        for(int i = 0; i < categories.size(); ++i){
            if(categories.get(i).getCategoryName().toLowerCase().equals(categoryName.toLowerCase())){
                return i;
            }
        }

        return -1;
    }

    public int findCategory(Category category){
        for(int i = 0; i < categories.size(); ++i){
            if(categories.get(i).getCategoryName().toLowerCase().equals(category.getCategoryName().toLowerCase())){
                return i;
            }
        }
        return -1;
    }

    public boolean checkIfCategoryExists(String categoryName){
        return findCategory(categoryName) != -1;
    }

//    public int getBudgetFrequencyValue(){
//        return settings.getFrequencyValue();
//    }
//    public String getBudgetFrequencyType(){
//        return settings.getFrequencyType();
//    }
    public int getCategoryFrequencyValue(int index){
        return categories.get(index).getSettings().getFrequencyValue();
    }
    public String getCategoryFrequencyType(int index){
        return categories.get(index).getSettings().getFrequencyType();
    }
//    public Settings getSettings(){
//        return settings;
//    }
    public void setOverallSpent(double amount){
       this.overallSpent = amount;
    }
    @Override
    public boolean equals(Object o) {
        return budgetName.equals(((Budget)o).budgetName) &&
                overallLimit == ((Budget)o).overallLimit &&
                overallSpent == ((Budget)o).overallSpent &&
                categories.equals(((Budget)o).categories);// &&
                //settings.equals(((Budget)o).settings);
    }

}
