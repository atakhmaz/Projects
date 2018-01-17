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
//import basil.sanity.Controllers.User;
//import basil.sanity.Crypt.BCrypt;
//import basil.sanity.Database.Database;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by kevinnguyen on 10/25/17.
// */
//@RunWith(AndroidJUnit4.class)
//public class AuthenticationUnitTest {
//    Context appContext = InstrumentationRegistry.getTargetContext();
//    Database db = null;
//
//    private static final String USERNAME = "username";
//    private static final String WRONG_USERNAME = "user";
//    private static final String PASSWORD = "password";
//    private static final String NEW_PASSWORD = "qwerty";
//    private static final String WRONG_PASSWORD = "wrong";
//    private static final String REPEATED_PASSWORD = "password";
//    private static final String SECURITY_ANSWER = "answer";
//    private static final String WRONG_SECURITY_ANSWER = "question";
//    private static final int QUESTION_INDEX = 0;
//
//    String hashedPW = null;
//    String hashedAns = null;
//
//    User user = null;
//
//    @Before
//    public void init() {
//        db = new Database(appContext);
//
//        hashedPW = BCrypt.hashpw(PASSWORD, BCrypt.gensalt());
//        hashedAns = BCrypt.hashpw(SECURITY_ANSWER, BCrypt.gensalt());
//
//        user = new User(USERNAME, hashedPW, QUESTION_INDEX, hashedAns);
//        db.addUser(user);
//    }
//
//    @After
//    public void reset() {
//        db.removeUser(USERNAME);
//    }
//
//    @Test
//    public void signUpUsernameAlreadyExists() {
//        String localHash = BCrypt.hashpw(NEW_PASSWORD, BCrypt.gensalt());
//        User user = new User(USERNAME, localHash, QUESTION_INDEX, hashedAns);
//        assertFalse(db.addUser(user));
//    }
//
//
//    @Test
//    public void loginWrongUsername() {
//        assertFalse(db.verifyUser(WRONG_USERNAME, PASSWORD));
//    }
//
//    @Test
//    public void loginWrongPassword() {
//        assertFalse(db.verifyUser(USERNAME, WRONG_PASSWORD));
//    }
//
//    @Test
//    public void forgotPasswordWrongUsername() {
//        assertFalse(db.checkSecurityAnswer(WRONG_USERNAME, SECURITY_ANSWER));
//    }
//
//    @Test
//    public void forgotPasswordCorrectAnswer() {
//        assertTrue(db.checkSecurityAnswer(USERNAME, SECURITY_ANSWER));
//    }
//
//    @Test
//    public void forgotPasswordWrongAnswer() {
//        assertFalse(db.checkSecurityAnswer(USERNAME, WRONG_SECURITY_ANSWER));
//    }
//
//    @Test
//    public void checkSecurityQuestionReturned() {
//        int index = db.getSecurityQuestion(USERNAME);
//        assertEquals(QUESTION_INDEX, index);
//    }
//
//    @Test
//    public void checkChangePasswordWorked() {
//        // Get the user and check if current passwords match
//        User user = db.getUser(USERNAME);
//        assertTrue(BCrypt.checkpw(PASSWORD, user.getHash()));
//
//        // Update the password and recheck
//        db.updatePassword(USERNAME, NEW_PASSWORD);
//        user = db.getUser(USERNAME);
//        assertTrue(BCrypt.checkpw(NEW_PASSWORD, user.getHash()));
//    }
//}
