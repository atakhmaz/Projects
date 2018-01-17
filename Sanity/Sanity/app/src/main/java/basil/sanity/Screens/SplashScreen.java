package basil.sanity.Screens;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.MyApplication;
import basil.sanity.Database.Database;

public class SplashScreen extends AppCompatActivity {
    //Extra for login
    public static final String EXTRA_LOGINFLAG = "SplashScreen.login";

    //Constant for sharedPref
    public final static String LOGINSAVE = "BudgetMenuScreen.loginSave";
    //Flag for login, default false so it will go to LoginScreen
    private boolean isLoggedIn;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_screen);
        //Check if previously minimized after logging in
//        if(savedInstanceState != null) {
//            isLoggedIn = savedInstanceState.getBoolean(LOGINSAVE);
//        }


               /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                final Intent i;
                String username;
                isLoggedIn = ((MyApplication)getApplication()).getLoginStatus();
                if (!isLoggedIn) {
                    //Set to Login
                    i = new Intent(SplashScreen.this, LoginScreen.class);

                } else {
                    //Set to MainMenu
                    i = new Intent(SplashScreen.this, MainMenuScreen.class);
                    Database db = new Database(getBaseContext());
                    //Get the user who logged in
                    username = ((MyApplication)getApplication()).getUsername();
                    db.GetBudgetManager(username);
                    //Let main know who is logging in
                    i.putExtra(BudgetMenuScreen.EXTRA_USER, username);
                }
                // Go to destination
                //Disable transition animation
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
