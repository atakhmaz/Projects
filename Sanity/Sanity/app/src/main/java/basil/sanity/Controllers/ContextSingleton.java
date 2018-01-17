package basil.sanity.Controllers;

import android.content.Context;

public class ContextSingleton {
    private static Context instance = null;

    public  ContextSingleton(Context context)
    {
        instance = context;
    }

    public static Context getInstance() throws Exception {
        if(instance == null) {
            throw new Exception("Context has not been instantiated.");
        }
        return instance;
    }
}