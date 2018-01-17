package basil.sanity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import basil.sanity.Database.Database;
import basil.sanity.Screens.LoginScreen;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by Alan on 10/27/17.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResetScreenTest {
    String username;
    String password;
    String securityAns;

    @Rule
    public ActivityTestRule<LoginScreen> mActivityRule =
            new ActivityTestRule(LoginScreen.class);

    @Before
    public void init(){
        username = "carter";
        password = "aaaaa";
        securityAns = "a";
    }

    @Test
    //Not a real test, Clear database before tests
    public void AA_clearDB(){
        clearDB();
    }

    @Test
    //If there is no such username in database, you cannot reset password
    public void A_nonexistentUser() {
        onView(withId(R.id.username_edit)).perform(typeText(username));
        closeSoftKeyboard();
        onView(withId(R.id.forgot_password_button)).perform(click());
        onView(withText(R.string.cannot_find_username)).inRoot(withDecorView(not
                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    //Not a test, need to load a user into database
    public void B_Signup() {
        onView(withId(R.id.signup_button)).perform(click());
        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
        onView(withId(R.id.enter_security_answer)).perform(typeText(securityAns));
        closeSoftKeyboard();
        onView(withId(R.id.finish_signup_button)).perform(click());
    }

    @Test
    //Give the wrong answer to the security question prevents reset
    public void C_wrongAnswer() {
        securityAns = "b";
        onView(withId(R.id.username_edit)).perform(typeText(username));
        closeSoftKeyboard();
        onView(withId(R.id.forgot_password_button)).perform(click());
        onView(withId(R.id.new_password_edit)).perform(typeText(password));
        onView(withId(R.id.repeat_password_edit)).perform(typeText(password));
        onView(withId(R.id.security_question_edit)).perform(typeText(securityAns));
        onView(withId(R.id.reset_password_button)).perform(click());
        onView(withText(R.string.wrong_answer)).inRoot(withDecorView(not
                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    //perform a successful password reset
    // logout and login again to show that new password works
    public void D_reset_success() {
        password = "newPassword";
        onView(withId(R.id.username_edit)).perform(typeText(username));
        closeSoftKeyboard();
        onView(withId(R.id.forgot_password_button)).perform(click());
        onView(withId(R.id.new_password_edit)).perform(typeText(password));
        onView(withId(R.id.repeat_password_edit)).perform(typeText(password));
        onView(withId(R.id.security_question_edit)).perform(typeText(securityAns));
        onView(withId(R.id.reset_password_button)).perform(click());
        onView(withId(R.id.logout_button)).perform(click());
        onView(withId(R.id.username_edit)).perform(typeText(username));
        onView(withId(R.id.password_edit)).perform(typeText(password));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.analytics_button)).perform(click());


    }
    @Test
    //Password was fewer than 5 characters
    public void E_passwordTooShort() {
        password = "1234";
        onView(withId(R.id.username_edit)).perform(typeText(username));
        closeSoftKeyboard();
        onView(withId(R.id.forgot_password_button)).perform(click());
        onView(withId(R.id.new_password_edit)).perform(typeText(password));
        onView(withId(R.id.repeat_password_edit)).perform(typeText(password));
        onView(withId(R.id.security_question_edit)).perform(typeText(securityAns));
        onView(withId(R.id.reset_password_button)).perform(click());
        onView(withText(R.string.password_too_short)).inRoot(withDecorView(not
                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }

    @Test
    //Password and re-entered password don't match
    public void F_mismatchedPasswords() {
        onView(withId(R.id.username_edit)).perform(typeText(username));
        closeSoftKeyboard();
        onView(withId(R.id.forgot_password_button)).perform(click());
        onView(withId(R.id.new_password_edit)).perform(typeText(password));
        onView(withId(R.id.repeat_password_edit)).perform(typeText("notPassword"));
        onView(withId(R.id.security_question_edit)).perform(typeText(securityAns));
        onView(withId(R.id.reset_password_button)).perform(click());
        onView(withText(R.string.mismatched_passwords)).inRoot(withDecorView(not
                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    //Not a real test, Clear database after finishing tests
    public void Z_clearDB(){
        clearDB();
    }
    //Function to clear the database
    private void clearDB() {
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

}
