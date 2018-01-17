//package basil.sanity;
//
//import android.support.test.espresso.contrib.PickerActions;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.util.Log;
//import android.widget.DatePicker;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import org.hamcrest.Matchers;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.Random;
//import java.util.StringTokenizer;
//
//import basil.sanity.Controllers.Budget;
//import basil.sanity.Database.Database;
//import basil.sanity.Screens.BudgetMenuScreen;
//import basil.sanity.Screens.TransactionsScreen;
//import basil.sanity.Util.RandomDate;
//import basil.sanity.Util.RandomString;
//
//import static android.support.test.espresso.Espresso.closeSoftKeyboard;
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.Matchers.anything;
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by Andre Takhmazyan on 10/27/2017.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class CategoryScreenTest {
//    //Number of test cases to run
//    private static int numberOfTransactions = 10;
//
//    //Variables for signing up
//    String username;
//    String password;
//    String securityQuestion;
//
//    //Variables for adding transactions
//    String transactionName;
//    double transactionAmountDouble;
//    String transactionAmount;
//    String transactionMemo;
//    boolean transactionOrRefund;
//
//    //Variables for the budget screen
//    ListView listview;
//    int listSize;
//
//    //Util
//    private static int lengthOfStrings = 8;
//    Random rnd;
//    RandomString gen;
//    DecimalFormat df;
//    Budget budget;
//    Calendar today;
//    int currentYear;
//
//    @Rule
//    public ActivityTestRule<BudgetMenuScreen> mActivityRule =
//            new ActivityTestRule(BudgetMenuScreen.class);
//
//    @Before
//    public void init(){
//        //Initialize some values we will need for the tests
//        username = "test";
//        password = "tests";
//        securityQuestion = "test";
//        //A seeded random number generator
//        rnd = new Random(System.currentTimeMillis());
//        //A random string generator that generated string of the size of lengthOfStrings
//        gen = new RandomString(lengthOfStrings, rnd);
//        //Formatter to format money inputs to 2 decimal places
//        df = new DecimalFormat("####0.00");
//        //Get today's date
//        today = Calendar.getInstance();
//        //Get today's year
//        currentYear = today.get(Calendar.YEAR);
//
//        createUser();
//
//        //Get the budget manager
//       // budget = Budget.getInstance();
//
//        addTransactions();
//
//    }
//
//    @Test
//    public void checkTransactionList(){
//        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).perform(click());
//        //Get the list view
//        listview = (ListView) BudgetMenuScreen.mainMenuView.findViewById(android.R.id.list);
//        //Get the size of the list view
//        listSize = listview.getCount();
//        TextView topText = (TextView) TransactionsScreen.budgetScreenView.findViewById(R.id.overall_summary);
//        String topTextString = topText.getText().toString();
//        StringTokenizer st = new StringTokenizer(topTextString, " ");
//        String firstAmount = st.nextToken();
//        double overallSpent = Double.parseDouble(firstAmount.substring(1));
//        st.nextToken();
//        st.nextToken();
//        st.nextToken();
//        String secondAmount = st.nextToken();
//        double overallLimit = Double.parseDouble(secondAmount.substring(1));
//        assertEquals(budget.getCategorySpent(0),overallSpent, 0.001);
//        assertEquals(budget.getCategoryLimit(0),overallLimit, 0.001);
//        assertEquals(budget.getCategoryListSize(), listSize);
//    }
//
//    public void createUser(){
//        //Clear the database so we can start fresh
//        clearDB();
//        //Click the sign up button
//        onView(withId(R.id.signup_button)).perform(click());
//        //Enter the username
//        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
//        //Enter the password
//        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
//        //Re-enter the password
//        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
//        closeSoftKeyboard();
//        //Enter the answer to the security question
//        onView(withId(R.id.enter_security_answer)).perform(typeText(securityQuestion));
//        closeSoftKeyboard();
//        //Click sign up
//        onView(withId(R.id.finish_signup_button)).perform(click());
//    }
//
//    public void addTransactions(){
//        //Loop to add a bunch of transactions
//        for(int i = 0; i < numberOfTransactions; ++i) {
//            //Generate a random string for the transaction name
//            transactionName = gen.nextString();
//            //Generate a random double (0.0 - 100.0) for the transaction amount
//            transactionAmountDouble = rnd.nextDouble()*100;
//            //Format the transaction amount to 2 decimal places
//            transactionAmount = df.format(transactionAmountDouble);
//            //Generate a random string for the memo
//            transactionMemo = gen.nextString();
//            //Generate a random boolean to click on refund
//            transactionOrRefund = rnd.nextBoolean();
//            //Click on the add transaction button for the first budget
//            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).
//                    onChildView(withId(R.id.transaction_button)).perform(click());
//            //Input the transaction name
//            onView(withId(R.id.transaction_item_name)).perform(typeText(transactionName));
//            //Click on refund if true and we wont get a negative or zero value
//            if(transactionOrRefund && (budget.getCategory(0).getAmountSpent()- transactionAmountDouble ) > 0.0){
//                onView(withId(R.id.radio_refund)).perform(click());
//            }
//            //Enter the transaction amount
//            onView(withId(R.id.transaction_amount)).perform(typeText(transactionAmount));
//            closeSoftKeyboard();
//            //Generate a random date
//            GregorianCalendar newDate = RandomDate.nextDate(currentYear - 20, currentYear - 1);
//            int year = newDate.get(Calendar.YEAR);
//            int month = newDate.get(Calendar.MONTH);
//            int day = newDate.get(Calendar.DAY_OF_MONTH);
//            //Set the transaction date
//            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
//                    .perform(PickerActions.setDate(year, month, day));
//            //Input the memo
//            onView(withId(R.id.transaction_memo)).perform(typeText(transactionMemo));
//            closeSoftKeyboard();
//            //Click the button to complete the transaction
//            onView(withId(R.id.complete_action_button)).perform(click());
//        }
//    }
//
//    //Function to clear the database
//    private void clearDB(){
//        Database db = null;
//        try {
//            db = new Database();
//        }
//        catch (Exception e){
//            Log.d("Database error occurred", e.getMessage());
//        }
//        Log.d("Clearing database ", "now...");
//        db.resetDatabase();
//    }
//}
