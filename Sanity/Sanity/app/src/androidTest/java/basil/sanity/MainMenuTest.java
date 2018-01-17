package basil.sanity;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SeekBar;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import basil.sanity.Controllers.Budget;
import basil.sanity.Database.Database;
import basil.sanity.Screens.BudgetMenuScreen;
import basil.sanity.Util.RandomDate;
import basil.sanity.Util.RandomString;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Andre Takhmazyan on 10/25/2017.
 *
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainMenuTest {
    //Number of test cases to run
    private static int numberOfBudgets = 5;
    private static int numberOfTransactions = 10;

    //Variables for logging in
    String username;
    String password;
    String securityQuestion;

    //Variables for adding budgets
    String categoryName;
    double budgetLimitDouble;
    String budgetLimit;
    int frequencyNum;
    String[] frequencyType;
    double overallLimitAmountFromAddingBudgets;

    //Variables for adding transactions
    int budgetSelected;
    String transactionName;
    double transactionAmountDouble;
    String transactionAmount;
    String transactionMemo;
    boolean transactionOrRefund;
    double overallLimitSpentFromAddingTransactions;

    //Util
    private static boolean firstTime = true;
    private static int lengthOfStrings = 8;
    Random rnd;
    RandomString gen;
    DecimalFormat df;
    ListView listview;
    int listSize;
    Budget budget;
    Calendar today;
    int currentYear;

    @Rule
    public ActivityTestRule<BudgetMenuScreen> mActivityRule =
            new ActivityTestRule(BudgetMenuScreen.class);

    @Before
    public void init(){
        //Initialize some values we will need for the tests
        username = "test";
        password = "tests";
        securityQuestion = "a";
        frequencyType = new String[] {"Day", "Week"};
        //A seeded random number generator
        rnd = new Random(System.currentTimeMillis());
        //A random string generator that generated string of the size of lengthOfStrings
        gen = new RandomString(lengthOfStrings, rnd);
        //Formatter to format money inputs to 2 decimal places
        df = new DecimalFormat("####0.00");
        //Get today's date
        today = Calendar.getInstance();
        //Get today's year
        currentYear = today.get(Calendar.YEAR);
        overallLimitAmountFromAddingBudgets = 0.0;
        overallLimitSpentFromAddingTransactions = 0.0;

        //If it is the first time we are running the tests
        if(firstTime) {
            //Clear the database so we can start fresh
            clearDB();
            //Click the sign up button
            onView(withId(R.id.signup_button)).perform(click());
            //Enter the username
            onView(withId(R.id.username_edit_signup)).perform(typeText(username));
            //Enter the password
            onView(withId(R.id.password_edit_signup)).perform(typeText(password));
            //Re-enter the password
            onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
            closeSoftKeyboard();
            //Enter the answer to the security question
            onView(withId(R.id.enter_security_answer)).perform(typeText(securityQuestion));
            closeSoftKeyboard();
            //Click sign up
            onView(withId(R.id.finish_signup_button)).perform(click());
        }
        else{
            //Else if it isn't the first time, we will login with the account we created
            //Enter username
            onView(withId(R.id.username_edit)).perform(typeText(username));
            //Enter password
            onView(withId(R.id.password_edit)).perform(typeText(password));
            //Click log in
            onView(withId(R.id.login_button)).perform(click());
        }
        //Get the list view
        listview = (ListView) BudgetMenuScreen.mainMenuView.findViewById(android.R.id.list);
        //Get the size of the list view
        listSize = listview.getCount();
        //Get the budget manager
        budget = Budget.getInstance();
        //It is no longer the first time
        firstTime = false;
    }

//    @Test
//    public void aDeleteAllBudgetsTest(){
//        //A loop to delete all the budgets
//        for(int i = 0; i < listSize;){
//            //Choose a budget to select
//            budgetSelected = rnd.nextInt(listSize);
//            //Click on the delete button of the specific budget
//            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(budgetSelected).
//                    onChildView(withId(R.id.delete_button)).perform(click());
//            //Click confirm on the popup
//            onView(withId(R.id.complete_action_button)).perform(click());
//            //Update the list view size
//            listSize = listview.getCount();
//        }
//        //Make sure the overall budget amounts are updated properly
//        assertEquals(0.0, budget.getBudgetLimit());
//        assertEquals(0.0, budget.getBudgetSpent());
//    }

    @Test
    public void bAddBudgetTest(){
        //Loop to add a bunch of budgets
        for(int i = 0; i < numberOfBudgets; ++i) {
            //Generate a random string for the budget name
            categoryName = gen.nextString();
            //Generate a random int (1 - 30) for the frequency
            frequencyNum = rnd.nextInt(30)+1;
            //Generate a random double (0.0 - 1000.0) for the budget limit
            budgetLimitDouble = rnd.nextDouble()*1000;
            //Format the limit to 2 decimal places
            budgetLimit = df.format(budgetLimitDouble);
            //Add the amount to a variable so we can check it at the end
            overallLimitAmountFromAddingBudgets += Double.parseDouble(budgetLimit);
            //Click on the add budget button
            onView(withId(R.id.add_budget_button)).perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return ViewMatchers.isEnabled(); // no constraints, they are checked above
                }

                @Override
                public String getDescription() {
                    return "Clicked add budget button";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    view.performClick();
                }
            });
            //Input the budget name
            onView(withId(R.id.add_budget_name)).perform(typeText(categoryName));
            closeSoftKeyboard();
            //Generate a random date (2018 - 2037)
            GregorianCalendar newDate = RandomDate.nextDate(currentYear + 1, currentYear + 20);
            int year = newDate.get(Calendar.YEAR);
            int month = newDate.get(Calendar.MONTH);
            int day = newDate.get(Calendar.DAY_OF_MONTH);
            Date date = new Date(year, month - 1, day, 0, 0);

            //Set the date to the random date
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                    .perform(PickerActions.setDate(year, month, day));
            //Change the seek bar position
            onView(withId(R.id.threshold_seekbar)).perform(clickSeekBar(rnd.nextInt(99)+1));
            //Input the frequency
            onView(withId(R.id.add_budget_frequency_number)).perform(typeText(Integer.toString(frequencyNum)));
            closeSoftKeyboard();
            //Input the frequency type (Day, Week)
            //onView(withId(R.id.add_budget_frequency_choice)).perform(click());
            onData(allOf(is(instanceOf(String.class)), is(frequencyType[rnd.nextInt(2)]))).perform(click());
            //Input the budget limit
            onView(withId(R.id.add_budget_limit)).perform(typeText(budgetLimit));
            closeSoftKeyboard();
            //Click confirm to add the budget
            onView(withId(R.id.complete_action_button)).perform(click());
        }
        //Check to make sure the overall budget limit is correct
        assertEquals(overallLimitAmountFromAddingBudgets, budget.getBudgetLimit(), 0.001);
    }

    @Test
    public void cAddTransactionTest(){
        //Loop to add a bunch of transactions
        for(int i = 0; i < numberOfTransactions; ++i) {
            //Select a random budget
            budgetSelected = rnd.nextInt(listSize);
            //Generate a random string for the transaction name
            transactionName = gen.nextString();
            //Generate a random double (0.0 - 100.0) for the transaction amount
            transactionAmountDouble = rnd.nextDouble()*100;
            //Format the transaction amount to 2 decimal places
            transactionAmount = df.format(transactionAmountDouble);
            //Add the transaction amount to a variable so we can check it at the end
            overallLimitSpentFromAddingTransactions += Double.parseDouble(transactionAmount);
            //Generate a random string for the memo
            transactionMemo = gen.nextString();
            //Generate a random boolean to click on refund
            transactionOrRefund = rnd.nextBoolean();
            //Click on the add transaction button for the specific budget
            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(budgetSelected).
                    onChildView(withId(R.id.transaction_button)).perform(click());
            //Input the transaction name
            onView(withId(R.id.transaction_item_name)).perform(typeText(transactionName));
            //Click on refund if true and we wont get a negative or zero value
            if(transactionOrRefund && (budget.getCategory(budgetSelected).getAmountSpent()- transactionAmountDouble ) > 0.0){
                onView(withId(R.id.radio_refund)).perform(click());
                //If it is a refund, we need to update the overall budget accordingly
                overallLimitSpentFromAddingTransactions += -2.0 * Double.parseDouble(transactionAmount);
            }
            //Enter the transaction amount
            onView(withId(R.id.transaction_amount)).perform(typeText(transactionAmount));
            closeSoftKeyboard();
            //Generate a random date
            GregorianCalendar newDate = RandomDate.nextDate(currentYear - 20, currentYear - 1);
            int year = newDate.get(Calendar.YEAR);
            int month = newDate.get(Calendar.MONTH);
            int day = newDate.get(Calendar.DAY_OF_MONTH);
            //Set the transaction date
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                    .perform(PickerActions.setDate(year, month, day));
            //Input the memo
            onView(withId(R.id.transaction_memo)).perform(typeText(transactionMemo));
            closeSoftKeyboard();
            //Click the button to complete the transaction
            onView(withId(R.id.complete_action_button)).perform(click());
        }
        //Check to make sure the overall budget spent is correct
        assertEquals(overallLimitSpentFromAddingTransactions, budget.getBudgetSpent(), 0.001);
    }

    @Test
    public void dDeleteAllBudgetsTest(){
        //A loop to delete all the budgets
        for(int i = 0; i < listSize;){
            //Choose a budget to select
            budgetSelected = rnd.nextInt(listSize);
            //Click on the delete button of the specific budget
//            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(budgetSelected).
//                    onChildView(withId(R.id.delete_button)).perform(click());
            //Click confirm on the popup
            onView(withId(R.id.complete_action_button)).perform(click());
            //Update the list view size
            listSize = listview.getCount();
        }
    }

    @Test
    public void eCheckOverallBudgetLimit(){
        //Close the app and log back in and make sure the overall budget is correct
        assertEquals(0.0, budget.getBudgetLimit());
        assertEquals(0.0, budget.getBudgetSpent());
    }

    //Function to clear the database
    private void clearDB(){
        Database db = null;
        try {
            db = new Database();
        }
        catch (Exception e){
            Log.d("Database error occurred", e.getMessage());
        }
        Log.d("Clearing database ", "now...");
        db.resetDatabase();
    }

    //A function to change the seek bar position
    public static ViewAction clickSeekBar(final int pos){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        SeekBar seekBar = (SeekBar) view;
                        final int[] screenPos = new int[2];
                        seekBar.getLocationOnScreen(screenPos);

                        // get the width of the actual bar area
                        // by removing padding
                        int trueWidth = seekBar.getWidth()
                                - seekBar.getPaddingLeft() - seekBar.getPaddingRight();

                        // what is the position on a 0-1 scale
                        //  add 0.3f to avoid round-off to the next smaller position
                        float relativePos = (0.3f + pos)/(float) seekBar.getMax();
                        if ( relativePos > 1.0f )
                            relativePos = 1.0f;

                        // determine where to click
                        final float screenX = trueWidth*relativePos + screenPos[0]
                                + seekBar.getPaddingLeft();
                        final float screenY = seekBar.getHeight()/2f + screenPos[1];
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
}