//package basil.sanity;
//
//import android.support.test.espresso.UiController;
//import android.support.test.espresso.ViewAction;
//import android.support.test.espresso.matcher.ViewMatchers;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.util.Log;
//import android.view.View;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.SeekBar;
//import android.widget.Spinner;
//
//import org.hamcrest.Matcher;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Random;
//
//import basil.sanity.Controllers.Budget;
//import basil.sanity.Database.Database;
//import basil.sanity.Screens.BudgetMenuScreen;
//import basil.sanity.Screens.SettingsScreen;
//import basil.sanity.Util.RandomString;
//
//import static android.support.test.espresso.Espresso.closeSoftKeyboard;
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.scrollTo;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static org.hamcrest.Matchers.anything;
//import static org.junit.Assert.assertEquals;
//
///**
// * Created by Andre Takhmazyan on 10/27/2017.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class SettingsScreenTest {
//
//    //Variables for signing up
//    String username;
//    String password;
//    String securityQuestion;
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
//        username = "a";
//        password = "a";
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
//        login();
//
//        //Get the budget manager
//        budget = Budget.getInstance();
//
//
//        //Get the list view
//        listview = (ListView) BudgetMenuScreen.mainMenuView.findViewById(android.R.id.list);
//        //Get the size of the list view
//        listSize = listview.getCount();
//    }
//
//    @Test
//    public void testOverallBudgetSettings(){
//        testSettings(-1);
//    }
//
//    @Test
//    public void testBudgetSettings(){
//        for(int i = 0; i < listSize; ++i){
//            testSettings(i);
//        }
//    }
//
////    @Test
////    public void testChangeOverallSettings(){
////        changeSettings(-1);
////    }
////
////    @Test
////    public void testChangeSettings(){
////        for(int i = 0; i < listSize; ++i){
////            changeSettings(i);
////        }
////    }
////
////    public void changeSettings(int position){
////
////    }
//
//    public void testSettings(int position){
//        if(position == -1){
//            onView(withId(R.id.overall_settings_button)).perform(new ViewAction() {
//                @Override
//                public Matcher<View> getConstraints() {
//                    return ViewMatchers.isEnabled(); // no constraints, they are checked above
//                }
//
//                @Override
//                public String getDescription() {
//                    return "Clicked add budget button";
//                }
//
//                @Override
//                public void perform(UiController uiController, View view) {
//                    view.performClick();
//                }
//            });
//        }
//        else{
//            onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(position).
//                    onChildView(withId(R.id.settings_button)).perform(click());
//        }
//
//        closeSoftKeyboard();
//
//        try {
//            Thread.sleep(1000);
//        }
//        catch(Exception e){}
//
//        EditText categoryName = (EditText) SettingsScreen.settingsScreenView.findViewById(R.id.settings_screen_budget_name);
//        String categoryNameString = categoryName.getText().toString();
//        if(position == -1){
//            assertEquals("Overall Category", categoryNameString);
//        }
//        else{
//            assertEquals(budget.getCategoryName(position), categoryNameString);
//        }
//
//        Date overallLimitStartDate;
//        Date overallLimitEndDate;
//
//        if(position == -1){
//            overallLimitStartDate = budget.getStartDate();
//            overallLimitEndDate = budget.getEndDate();
//        }
//        else{
//            overallLimitStartDate = budget.getStartDate(position);
//            overallLimitEndDate = budget.getEndDate(position);
//        }
//
//        Calendar cal = Calendar.getInstance();
//
//        DatePicker startDatePicker = (DatePicker) SettingsScreen.settingsScreenView.findViewById(R.id.settings_screen_budget_start_date);
//        DatePicker endDatePicker = (DatePicker) SettingsScreen.settingsScreenView.findViewById(R.id.settings_screen_budget_end_date);
//
//        cal.set(Calendar.YEAR, startDatePicker.getYear());
//        cal.set(Calendar.MONTH, startDatePicker.getMonth());
//        cal.set(Calendar.DAY_OF_MONTH, startDatePicker.getDayOfMonth());
//
//        Date settingsScreenStartDate = cal.getTime();
//
//        cal.set(Calendar.YEAR, endDatePicker.getYear());
//        cal.set(Calendar.MONTH, endDatePicker.getMonth());
//        cal.set(Calendar.DAY_OF_MONTH, endDatePicker.getDayOfMonth());
//
//        Date settingsScreenEndDate = cal.getTime();
//
//        assertEquals(overallLimitStartDate.getTime(), settingsScreenStartDate.getTime(), 60000);
//        assertEquals(overallLimitEndDate.getTime(), settingsScreenEndDate.getTime(), 60000);
//
//        SeekBar seekBar = (SeekBar) SettingsScreen.settingsScreenView.findViewById(R.id.settings_screen_threshold_seekbar);
//        int seekBarProgress = seekBar.getProgress() + 1;
//        if(position == -1){
//            assertEquals((int)(budget.getThreshold()*100.0), seekBarProgress);
//        }
//        else{
//            assertEquals((int)(budget.getThreshold(position)*100.0), seekBarProgress);
//        }
//
//        EditText frequencyValue = (EditText) SettingsScreen.settingsScreenView.findViewById(R.id.frequency_number);
//        int frequencyValueInt = Integer.parseInt(frequencyValue.getText().toString());
//        if(position == -1){
//            assertEquals(budget.getBudgetFrequencyValue(), frequencyValueInt);
//        }
//        else{
//            assertEquals(budget.getCategoryFrequencyValue(position), frequencyValueInt);
//        }
//
//        //Spinner frequencyTypeSpinner = (Spinner) SettingsScreen.settingsScreenView.findViewById(R.id.frequency_choice);
//        //String frequencyTypeSpinnerString = frequencyTypeSpinner.getSelectedItem().toString();
////        if(position == -1){
////            assertEquals(budget.getBudgetFrequencyType(), frequencyTypeSpinnerString);
////        }
////        else{
////            assertEquals(budget.getCategoryFrequencyType(position), frequencyTypeSpinnerString);
////        }
//
//        EditText budgetLimit = (EditText) SettingsScreen.settingsScreenView.findViewById(R.id.settings_screen_budget_limit);
//        double budgetLimitDouble = Double.parseDouble(budgetLimit.getText().toString());
//        if(position == -1){
//            assertEquals(budget.getBudgetLimit(), budgetLimitDouble, 0.0);
//        }
//        else{
//            assertEquals(budget.getCategoryLimit(position), budgetLimitDouble, 0.0);
//        }
//        onView(withId(R.id.settings_screen_confirm_button)).perform(scrollTo(), click());
//    }
//
//    public void login(){
//        //Clear that database
//        clearDB();
//        //Enter username
//        onView(withId(R.id.username_edit)).perform(typeText(username));
//        //Enter password
//        onView(withId(R.id.password_edit)).perform(typeText(password));
//        //Click log in
//        onView(withId(R.id.login_button)).perform(click());
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
