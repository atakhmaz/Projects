package basil.sanity.Controllers;

import android.app.Application;

/**
 * Created by Alan on 11/3/17.
 */

//Class to store global variables
public class MyApplication extends Application {
    private boolean loginStatus;
    private String username;

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        loginStatus = false;
        username = "";
    }

}

