//package basil.sanity;
//
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import basil.sanity.Controllers.Budget;
//import basil.sanity.Controllers.Category;
//import basil.sanity.Controllers.CategoryPeriod;
//import basil.sanity.Controllers.Settings;
//import basil.sanity.Controllers.Transaction;
//import basil.sanity.Database.Database;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by Tri on 10/25/2017.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class CategoryManagerUnitTest {
//    Budget manager;
//    Database db = null;
//    Context appContext = InstrumentationRegistry.getTargetContext();
//
//    @Before
//    public void init() throws Exception {
//        manager = Budget.getInstance();
//        db = new Database(appContext);
//    }
//
//    @Test
//    public void testAddBudget() throws Exception {
//        Category category = new Category("Category", 100.0, 0.0, new Settings());
//        manager.addCategory(category);
//        assertEquals(1, manager.getCategories().size());
//        assertEquals(manager.getCategories().get(0), category);
//    }
//
//    @Test
//    public void testAddMultipleBudgets() throws Exception {
//        Category category = new Category("Category", 100.0, 0.0, new Settings());
//        manager.addCategory(category);
//        category = new Category("Category 2", 200.0, 0.0, new Settings());
//        manager.addCategory(category);
//        assertEquals(2, manager.getCategories().size());
//        assertEquals(manager.getCategories().get(1), category);
//    }
//
//    @Test
//    public void changeBudgetSettings() throws Exception {
//        Category category = new Category("Category", 100.0, 0.0, new Settings());
//        manager.addCategory(category);
//        Category categoryB = new Category("Budget2", 200.0, 0.0, new Settings());
//        manager.addCategory(category);
//        Settings newSettings = new Settings(0.60, new CategoryPeriod(), "Week", 1);
//        manager.editCategorySetting(0, newSettings);
//        assertEquals(manager.getCategory(0).getSettings(), newSettings);
//    }
//
////    @Test
////    public void addRefund() throws Exception {
////        Category category = new Category("Category", 100.0, 75.0, new Settings());
////        manager.addCategory(category);
////        Transaction transaction = new Transaction("Test", 50.0, "This is a test case");
////        manager.addRefund(transaction, 0);
////        assert(manager.getCategory(0).getAmountSpent() == 25.0);
////    }
//
//    @After
//    public void cleanup() throws Exception {
//        manager.clearInstance();
//    }
//}
