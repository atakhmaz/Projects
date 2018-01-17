//package basil.sanity;
//
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.util.Log;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//
//import basil.sanity.Database.Database;
//import basil.sanity.Screens.LoginScreen;
//
//import static android.support.test.espresso.Espresso.closeSoftKeyboard;
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static java.util.EnumSet.allOf;
//import static org.hamcrest.CoreMatchers.instanceOf;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.anything;
//import static org.junit.Assert.assertEquals;
//import static org.hamcrest.Matchers.not;
//import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//
//
///**
// * Created by Alan on 10/25/17.
// */
//
//@RunWith(AndroidJUnit4.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class SignUpScreenTest {
//    String username;
//    String password;
//    String securityAns;
//
//    @Rule
//    public ActivityTestRule<LoginScreen> mActivityRule =
//            new ActivityTestRule(LoginScreen.class);
//
//    @Before
//    public void init(){
//        username = "alan";
//        password = "aaaaavvv123";
//        securityAns = "a";
//    }
//
//    @Test
//    //Not a real test, Clear database before tests
//    public void AA_clearDB(){
//        clearDB();
//    }
//
//    @Test
//    // successfully add a new user to system
//    public void A_addUser() {
//        Context targetContext = InstrumentationRegistry.getTargetContext();
//        String question = targetContext.getResources().getStringArray(R.array.questions)[1];
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
//        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
//        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
//        onView(withId(R.id.question_spinner)).perform(click());
//        onData(anything()).atPosition(1).perform(click());
//        onView(withId(R.id.enter_security_answer)).perform(typeText(securityAns));
//        closeSoftKeyboard();
//        onView(withId(R.id.finish_signup_button)).perform(click());
//        //Logout
//        onView(withId(R.id.logout_button)).perform(click());
//        //Try to reset the password
//        onView(withId(R.id.username_edit)).perform(typeText(username));
//        closeSoftKeyboard();
//        onView(withId(R.id.forgot_password_button)).perform(click());
//        //Verify that question is the one that was picked
//        onView(withId(R.id.security_question)).check(matches(withText(question)));
//
//    }
//    @Test
//    // There is already a user with that username
//    public void B_usernameUnavailable() {
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
//        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
//        closeSoftKeyboard();
//        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
//        onView(withId(R.id.enter_security_answer)).perform(typeText(securityAns));
//        closeSoftKeyboard();
//        onView(withId(R.id.finish_signup_button)).perform(click());
//        onView(withText(R.string.username_unavailable)).inRoot(withDecorView(not
//                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
//
//    }
//
//    @Test
//    //Password and re-entered password don't match
//    public void C_mismatchedPasswords() {
//        username = "jessie";
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
//        closeSoftKeyboard();
//        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
//        onView(withId(R.id.reeneter_password_edit)).perform(typeText(securityAns));
//        onView(withId(R.id.enter_security_answer)).perform(typeText(securityAns));
//        closeSoftKeyboard();
//        onView(withId(R.id.finish_signup_button)).perform(click());
//        onView(withText(R.string.mismatched_passwords)).inRoot(withDecorView(not
//                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
//    }
//
//    @Test
//    //Password was fewer than 5 characters
//    public void D_passwordTooShort() {
//        username = "unique";
//        password = "1234";
//        onView(withId(R.id.signup_button)).perform(click());
//        closeSoftKeyboard();
//        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
//        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
//        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
//        onView(withId(R.id.enter_security_answer)).perform(typeText(securityAns));
//        closeSoftKeyboard();
//        onView(withId(R.id.finish_signup_button)).perform(click());
//        onView(withText(R.string.password_too_short)).inRoot(withDecorView(not
//                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
//
//    }
//    @Test
//    // User did not fill in a security answer
//    public void E_missingSecurityAnswer() {
//        username = "jessie";
//        closeSoftKeyboard();
//        onView(withId(R.id.signup_button)).perform(click());
//        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
//        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
//        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
//        closeSoftKeyboard();
//        onView(withId(R.id.finish_signup_button)).perform(click());
//        onView(withText(R.string.no_answer_label)).inRoot(withDecorView(not
//                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
//
//    }
//
//
//
//
//
//    @Test
//    //Not a real test, Clear database after finishing tests
//    public void Z_clearDB(){
//        clearDB();
//    }
//    //Function to clear the database
//    private void clearDB() {
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
//
//}
