package basil.sanity;

import android.content.ComponentName;
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
public class LoginScreenTest {
    String username;
    String password;
    String securityAns;



    @Rule
    public ActivityTestRule<LoginScreen> mActivityRule =
            new ActivityTestRule(LoginScreen.class);

    @Before
    public void init(){
        username = "bob";
        password = "aaaaa";
        securityAns = "a";
    }

    @Test
    //Not a real test, Clear database before tests
    public void AA_clearDB(){
        clearDB();
    }

    @Test
    public void A_nonexistentUsername() {
        onView(withId(R.id.username_edit)).perform(typeText(username));
        onView(withId(R.id.password_edit)).perform(typeText(password));
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.login_error)).inRoot(withDecorView(not
                (mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void B_SignupAndLogin() {
        onView(withId(R.id.signup_button)).perform(click());
        onView(withId(R.id.username_edit_signup)).perform(typeText(username));
        onView(withId(R.id.password_edit_signup)).perform(typeText(password));
        onView(withId(R.id.reeneter_password_edit)).perform(typeText(password));
        onView(withId(R.id.enter_security_answer)).perform(typeText(securityAns));
        closeSoftKeyboard();
        onView(withId(R.id.finish_signup_button)).perform(click());
        onView(withId(R.id.logout_button)).perform(click());
        onView(withId(R.id.username_edit)).perform(typeText(username));
        onView(withId(R.id.password_edit)).perform(typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.analytics_button)).perform(click());

    }

    @Test
    public void C_loginFromClosed(){
        onView(withId(R.id.username_edit)).perform(typeText(username));
        onView(withId(R.id.password_edit)).perform(typeText(password));
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.analytics_button)).perform(click());

    }
    @Test
    public void D_wrongPassword() {
        password = "bbbbb";
        onView(withId(R.id.username_edit)).perform(typeText(username));
        onView(withId(R.id.password_edit)).perform(typeText(password));
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.login_error)).inRoot(withDecorView(not
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
