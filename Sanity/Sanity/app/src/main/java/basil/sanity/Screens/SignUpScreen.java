package basil.sanity.Screens;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import basil.sanity.Controllers.ContextSingleton;
import basil.sanity.Controllers.MyApplication;
import basil.sanity.Controllers.User;
import basil.sanity.Crypt.BCrypt;
import basil.sanity.Database.Database;
import basil.sanity.R;

public class SignUpScreen extends AppCompatActivity {
    EditText emailEdit;
    EditText usernameEdit;
    EditText passwordEdit;
    EditText reenterPasswordEdit;
    EditText answerEdit;
    Spinner questionSpinner;
    Button finishButton;

    // Data
    String email="";
    String username="";
    String password ="";

    // Password and reenter should have different default values
    String reenterPassword="";
    String securityAnswer="";
    int questionInt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        ContextSingleton contextSingleton = new ContextSingleton(getBaseContext());

        // Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        emailEdit = (EditText) findViewById(R.id.username_edit_email);
        usernameEdit = (EditText) findViewById(R.id.username_edit_signup);
        passwordEdit = (EditText) findViewById(R.id.password_edit_signup);
        reenterPasswordEdit = (EditText) findViewById(R.id.reeneter_password_edit);
        answerEdit = (EditText) findViewById(R.id.enter_security_answer);
        questionSpinner = (Spinner) findViewById(R.id.question_spinner);

        finishButton = (Button) findViewById(R.id.finish_signup_button);

        // Finish listener
        // Checks for valid new username and password, goes to main menu
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            TextInputLayout tilEmail = (TextInputLayout) findViewById(R.id.til_signup_email);
            TextInputLayout tilUsername = (TextInputLayout) findViewById(R.id.til_signup_username);
            TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.til_signup_password);
            TextInputLayout tilRePassword = (TextInputLayout) findViewById(R.id.til_reenter_password);
            TextInputLayout tilSecurityAnswer = (TextInputLayout) findViewById(R.id.til_security_answer);
            tilEmail.setError(null);
            tilUsername.setError(null);
            tilPassword.setError(null);
            tilRePassword.setError(null);
            tilSecurityAnswer.setError(null);

            Database db = new Database(getBaseContext());


                boolean errorExists = false;
            // Check if email address is valid
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);

            if (email.equals("")) {
                tilEmail.setError(getResources().getString(R.string.sign_up_no_email));
                errorExists = true;
                // TODO remove second half of if check
            } else if (!matcher.matches() && !email.equals("a")) {
                tilEmail.setError(getResources().getString(R.string.email_not_valid));
                errorExists = true;
            // Check if email address is already in use
            } else if (db.checkEmail(email)) {
                tilEmail.setError(getResources().getString(R.string.email_already_taken));
                errorExists = true;
            }
            // Make sure the password is 5 or more characters long
            if (username.length() < 1) {
                // Username empty
                tilUsername.setError(getResources().getString(R.string.username_empty));
                errorExists = true;
            }
            // If username is already taken, show it is unavailable
            if (db.checkUser(username)) {
                // Username is already taken
                tilUsername.setError(getResources().getString(R.string.username_unavailable));
                errorExists = true;
            }
            // Make sure the password is 5 or more characters long
            if (password.length() < 5) {
                // Password too short
                tilPassword.setError(getResources().getString(R.string.password_too_short));
                errorExists = true;
            }
            // Check that passwords match
            if (password.length() < 5 || !password.equals(reenterPassword)) {
                // Error mismatched passwords
                tilRePassword.setError(getResources().getString(R.string.mismatched_passwords));
                errorExists = true;
            }
            if (securityAnswer.equals("")) {
                // No security answer provided
                tilSecurityAnswer.setError(getResources().getString(R.string.no_answer_label));
                errorExists = true;
            }
            if(!errorExists) {
                // Add the user to the database
                // Hash the password and security answer
                String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String hashAnswer = BCrypt.hashpw(securityAnswer, BCrypt.gensalt());

                db.addUser(new User(email, username, hashPassword, questionInt, hashAnswer));
                db.GetBudgetManager(username);

                //Login to BudgetMenuScreen after updating database
                Intent i = new Intent(SignUpScreen.this, MainMenuScreen.class);

                //Update global login value
                ((MyApplication)getApplication()).setLoginStatus(true);
                // Update global username
                ((MyApplication)getApplication()).setUsername(username);

                //Save the username to extra
                i.putExtra(BudgetMenuScreen.EXTRA_USER, username);
                //Close signup screen
                finish();
                // Close login screen
                LoginScreen.getInstance().finish();
                // Go to mainmenu
                startActivity(i);
            }
            }
        });

        // Listener for email
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Copy the email address from the input field
                email = emailEdit.getText().toString();
            }
        });

        // Listener for username
        usernameEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                // Copy the username from input
                username = usernameEdit.getText().toString();
            }
        });

        // Listener for password
        passwordEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                // Copy the username from input
                password = passwordEdit.getText().toString();
            }

        });
        // Listener for re-entered password
        reenterPasswordEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                // Copy the username from input
                reenterPassword = reenterPasswordEdit.getText().toString();
            }

        });
        // Listener for security answer
        answerEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                // Copy the username from input
                securityAnswer = answerEdit.getText().toString();
            }

        });

        questionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorTextLight));
                questionInt = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
