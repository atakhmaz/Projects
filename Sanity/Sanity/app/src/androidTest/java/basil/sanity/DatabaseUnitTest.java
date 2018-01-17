//package basil.sanity;
//
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//
//import java.util.ArrayList;
//
//import basil.sanity.Controllers.Budget;
//import basil.sanity.Controllers.Category;
//import basil.sanity.Controllers.Transaction;
//import basil.sanity.Controllers.User;
//import basil.sanity.Database.Database;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Instrumentation test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class DatabaseUnitTest {
//    Context appContext = InstrumentationRegistry.getTargetContext();
//    Database db = new Database(appContext);
//    String username = "username";
//    String password = "$2a$10$EGI8dmp8xeqNi6tnunsKNujRJ0kMWLIY7JjuMFEbSeJBxl6ZY2sCK";
//    String categoryName = "Budget1";
//    Budget budget = new Budget();
//    ArrayList<Category> categoryList = new ArrayList<Category>();
//    static boolean  databaseReset = false;
//    @Before
//    public void init(){
//        if(databaseReset == false)
//        {
//            db.resetDatabase();
//            databaseReset = true;
//        }
//
//    }
//
//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//
//        assertEquals("basil.sanity", appContext.getPackageName());
//    }
//
////    @Test
////    public void aCheckAddUser() throws Exception {
////
////        User user1 = new User("Dummy",username, password);
////        db.addUser(user1);
////        User user2 = db.getUser(username);
////
////        assertEquals(user1, user2);
////    }
//
//    @Test
//    public void bCheckAddAndGetBudget() throws Exception {
//        db.AddBudget(budget, username);
//        assertEquals(budget, db.GetBudget(username, budget.getBudgetName()));
//
//
//    }
//    @Test
//    public void cCheckAddAndGetCategory() throws Exception {
//        Category category = new Category("Category");
//        categoryList.add(category);
//        db.AddCategory(category,budget.getBudgetName(),username);
//        assertEquals(category,  db.GetCategory(category.getCategoryName(), username,budget.getBudgetName()));
//
//    }
//    @Test
//    public void dCheckGetCategoryList() throws Exception {
//        Category category1 = new Category("Category2");
//        db.AddCategory(category1,budget.getBudgetName(), username);
//        Category category = new Category("Category");
//        categoryList.add(category);
//        ArrayList<Category> cList =  db.GetCategoryList(username,budget.getBudgetName());
//        categoryList.add(category1);
//        assertEquals(categoryList.size(),cList.size());
//
//    }
//    @Test
//    public void eAddTransaction() throws Exception {
//
//        Category category = new Category("Category");
//        Transaction transaction = new Transaction("transaction 1",10.0);
//        Transaction transaction2 = new Transaction("transaction 2",15.0);
//        ArrayList<Transaction> transactionList = new ArrayList<>();
//        transactionList.add(transaction);
//        transactionList.add(transaction2);
//        db.AddTransaction(transaction,username, budget.getBudgetName(),category.getCategoryName());
//        db.AddTransaction(transaction2,username, budget.getBudgetName(),category.getCategoryName());
//        assertEquals(transactionList,db.GetTransactionList(category.getCategoryName(),budget.getBudgetName(),username));
//
//    }
//    @Test
//    public void fRecheckCategory() throws Exception {
//
//        Category category = new Category("Category");
//        Transaction transaction = new Transaction("transaction 1",10.0);
//        Transaction transaction2 = new Transaction("transaction 2",15.0);
//
//        category.addTransaction(transaction);
//        category.addTransaction(transaction2);
//        boolean check = false;
//        if(category.equals(db.GetCategory(category.getCategoryName(), username,budget.getBudgetName()))){
//            check = true;
//        }
//        assertEquals(category, check );
//
//
//    }
////    @Test
////    public void cCheckAddTransaction() throws Exception {
////        Category category1 = new Category(categoryName);
////        Transaction transaction1 = new Transaction("Item1", 10.0);
////
////        db.addTransactionDB(transaction1, categoryName, username);
////        Transaction transaction2 = null;
////        ArrayList<Transaction> transactionList = db.getTransactionListForBudget(categoryName, username);
////
////         transaction2 = transactionList.get(0);
////
////        assertEquals(transaction1, transaction2);
////    }
////
////    @Test
////    public void dCheckGetOverallTransactionList() throws Exception {
////        Category category1 = new Category(categoryName);
////        Transaction transaction1 = new Transaction("Item1", 10.0);
////        Transaction transaction2 = new Transaction("Item2", 20.0);
////
////        db.addTransactionDB(transaction2, categoryName, username);
////        ArrayList<Transaction> transactionList1 = db.getTransactionListForBudget(categoryName, username);
////        ArrayList<Transaction> transactionList2 = new ArrayList<Transaction>();
////        transactionList2.add(transaction2);
////        transactionList2.add(transaction1);
////        boolean check = true;
////        for(int i = 0; i< transactionList1.size();i++){
////            if(!transactionList1.get(i).equals(transactionList2.get(i))){
////                check = false;
////            }
////    }
////
////        assertEquals(true, check);
////    }
////
////    @Test
////    public void eCheckWrongLogin() throws Exception {
////
////
////        assertEquals(false, db.verifyUser("username","$2a$10$EGI8dmp8xeqNi6tnunsKNujRJ0kMWLIY7JjuMFEbSeJBxl6ZY2sCKsfsfs"));
////    }
////    @Test
////    public void fCheckGetAllBudget() throws Exception {
////        Category category1 = new Category(categoryName);
////        ArrayList<Category> categoryList1 = db.getCategoryList( username);
////        ArrayList<Category> categoryList2 = new ArrayList<Category>();
////        categoryList2.add(db.getCategory(category1.getCategoryName(),username));
////        categoryList2.add(db.getCategory("Food",username));
////        categoryList2.add(db.getCategory("Other",username));
////        categoryList2.add(db.getCategory("Recreation",username));
////        categoryList2.add(db.getCategory("Rent",username));
////        categoryList2.add(db.getCategory("Transportation",username));
////        assertEquals(categoryList1, categoryList2);
////    }
////    @Test
////    public void gCheckDeleteBudget() throws Exception {
////
////        db.deleteBudget(db.getCategory(categoryName, username),username);
////        assertEquals(false, db.checkBudget(username,categoryName));
////    }
////    public void fCheckRefund() throws Exception {
////
////        Category category1 = new Category(categoryName);
////        Transaction transaction1 = new Transaction("Refund", -10.0);
////        db.addCategory(new Category("Refund"),username);
////        db.addTransactionDB(transaction1, "Refund", username);
////        Transaction transaction2 = null;
////        ArrayList<Transaction> transactionList = db.getTransactionListForBudget("Refund", username);
////        if (transactionList.size() != 0) {
////            transaction2 = transactionList.get(0);
////        }
////        assertEquals(transaction1, transaction2);
////    }
//
//}
