package basil.sanity.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import basil.sanity.Controllers.ContextSingleton;
import basil.sanity.Controllers.MyApplication;
import basil.sanity.Database.Database;
import basil.sanity.R;

/**
 * Created by kevinnguyen on 10/15/17.
 */

public class ResetScreen extends AppCompatActivity {
    public static final String USERNAME = "ResetScreen.username";

    private String username;
    private String newPassword;
    private String repeatedPassword;
    private String answer;

    // Edit texts
    EditText newPasswordEdit;
    EditText repeatedPasswordEdit;
    EditText answerEdit;

    // Reset password button
    Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextSingleton contextSingleton = new ContextSingleton(getBaseContext());

        //Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Load the reset password screen
        setContentView(R.layout.activity_reset_password_screen);

        //Initialize variables
        newPassword = "";
        repeatedPassword = "";
        answer = "";

        newPasswordEdit = (EditText) findViewById(R.id.new_password_edit);
        repeatedPasswordEdit = (EditText) findViewById(R.id.repeat_password_edit);
        answerEdit = (EditText) findViewById(R.id.security_question_edit);

        resetPasswordButton = (Button) findViewById(R.id.reset_password_button);

        // Grab username from login screen
        username = getIntent().getStringExtra(USERNAME);

        Database db = new Database(this.getBaseContext());

        // Grab the security question by username
        final int questionIndex = db.getSecurityQuestion(username);
        if (questionIndex != -1) {
            String[] questions = getResources().getStringArray(R.array.questions);
            final String question = questions[questionIndex];
            ((TextView) findViewById(R.id.security_question)).setText(question);
        }

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database(getBaseContext());

                // Check user answer to security question
                TextInputLayout tilNewPassword = (TextInputLayout) findViewById(R.id.til_new_password);
                TextInputLayout tilNewPasswordRepeat = (TextInputLayout) findViewById(R.id.til_new_password_repeat);
                TextInputLayout tilAnswer = (TextInputLayout) findViewById(R.id.til_answer);
                tilNewPassword.setError(null);
                tilNewPasswordRepeat.setError(null);
                tilAnswer.setError(null);

                boolean hasError = false;

                if (newPassword.length() < 5) {
                    tilNewPassword.setError(getResources().getString(R.string.password_too_short));
                    hasError = true;
                }
                if (newPassword.length() < 5 || !newPassword.equals(repeatedPassword)) {
                    tilNewPasswordRepeat.setError(getResources().getString(R.string.mismatched_passwords));
                    hasError = true;
                }
                if (answer == null || answer.isEmpty() || !db.checkSecurityAnswer(username, answer)) {
                    tilAnswer.setError(getResources().getString(R.string.wrong_answer));
                    hasError = true;
                }
                if(!hasError) {
                    // Change the password
                    db.updatePassword(username, newPassword);
                    Intent i = new Intent(ResetScreen.this, MainMenuScreen.class);

                    //Update global login value
                    ((MyApplication) getApplication()).setLoginStatus(true);
                    //Update global username
                    ((MyApplication) getApplication()).setUsername(username);
                    i.putExtra(BudgetMenuScreen.EXTRA_USER, username);
                    finish();
                    //Close login screen
                    LoginScreen.getInstance().finish();
                    startActivity(i);
                }
            }
        });

        // Listener for new password
        newPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPassword = newPasswordEdit.getText().toString();
            }
        });

        // Listener for repeated password
        repeatedPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repeatedPassword = repeatedPasswordEdit.getText().toString();
            }
        });

        // Listener for security question answer
        answerEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                answer = answerEdit.getText().toString();
            }
        });
    }
}
