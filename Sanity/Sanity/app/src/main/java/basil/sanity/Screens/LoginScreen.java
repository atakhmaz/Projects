package basil.sanity.Screens;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.ContextSingleton;
import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.MyApplication;
import basil.sanity.Database.Database;
import basil.sanity.R;

public class LoginScreen extends AppCompatActivity {
    private String username = null;
    private String password = null;

    //Used to finish loginScreen from different activity
    static LoginScreen myLoginScreen;

    //Views
    EditText usernameEdit;
    EditText passwordEdit;

    Button loginButton;
    TextView signupLink;
    TextView forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Create a LoginScreen so ResetScreen and SignupScreen can finish it
        myLoginScreen = this;

        //Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ContextSingleton contextSingleton = new ContextSingleton(getBaseContext());
        Database db = new Database(this.getBaseContext());

        // Initialize login button
        loginButton = (Button) findViewById(R.id.login_button);
        signupLink = (TextView) findViewById(R.id.signup_link);

        // Initialize forgot password button
        forgotPasswordLink = (TextView) findViewById(R.id.forgot_password_link);

        usernameEdit = (EditText) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);



        // Listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasError = false;

                TextInputLayout tilUsername = (TextInputLayout) findViewById(R.id.til_login_username);
                TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.til_login_password);
                tilUsername.setError(null);
                tilPassword.setError(null);

                if(username == null || username.isEmpty()){
                    tilUsername.setError(getResources().getString(R.string.username_empty));
                    hasError = true;
                }
                if(password == null || password.isEmpty()){
                    tilPassword.setError(getResources().getString(R.string.password_empty));
                    hasError = true;
                }
                //verify with database username and password
                //Create a new intent to go to mainMenu when login button is clicked
                if(!hasError) {
                    Database db = new Database(getBaseContext());
                    if (db.verifyUser(username, password)) {
                        // Init BudgetManager
                        db.GetBudgetManager(username);

                        Intent i = new Intent(LoginScreen.this, MainMenuScreen.class);
                        //Update global login value
                        ((MyApplication) getApplication()).setLoginStatus(true);
                        //Update global username
                        ((MyApplication) getApplication()).setUsername(username);
                        i.putExtra(BudgetMenuScreen.EXTRA_USER, username);

                        finish();
                        startActivity(i);
                    } else {
                        //Username or password invalid
                        tilPassword.setError(getResources().getString(R.string.login_username_or_password_wrong));
                    }
                }
            }
        });

        // Listener for forgot password button
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Segue to reset password screen
                Database db = new Database(getBaseContext());
                if (username != null && db.checkUser(username)) {
                    Intent i = new Intent(LoginScreen.this, ResetScreen.class);
                    i.putExtra(ResetScreen.USERNAME, username);
                    startActivity(i);
                } else {
                    TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.til_login_password);
                    tilPassword.setError(null);

                    TextInputLayout tilUsername = (TextInputLayout) findViewById(R.id.til_login_username);
                    tilUsername.setError(getResources().getString(R.string.login_username_already_exists));
                }

            }
        });

        // Listener for signup
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to signup activity
                Intent i = new Intent(LoginScreen.this, SignUpScreen.class);
                //finish();
                startActivity(i);
            }
        });

        //Listener for username
        usernameEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                //Copy the username from input
                username = usernameEdit.getText().toString();
            }

        });
        //Listener for input
        passwordEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                //Copy the password from input
                password = passwordEdit.getText().toString();
            }

        });
    }

    //Used in Reset/Signup to finish login
    public static LoginScreen getInstance(){
        return myLoginScreen;
    }
}
