package rest.o.gram.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import rest.o.gram.activities.PersonalActivity;
import rest.o.gram.authentication.IAuthenticationProvider;
import rest.o.gram.client.RestogramClient;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Itay
 * Date: 20/04/13
 */
public class RestogramApplication extends Application implements IRestogramApplication, Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize activities set
        activities = new HashSet<>();

        // Register callbacks
        registerActivityLifecycleCallbacks(this);

        // Initialize
        initialize();
    }

    @Override
    public void onTerminate() { // Not guaranteed to be called from android
        super.onTerminate();

        // Dispose client
        RestogramClient.getInstance().dispose();

        // Clear activities map
        activities.clear();

        if(RestogramClient.getInstance().isDebuggable()) {
            Log.d("REST-O-GRAM", "Application terminated");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        try {
            if(!activities.contains(activity))
                activities.add(activity);
        }
        catch(Exception e) {
            // TODO
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(activity.getClass() == PersonalActivity.class) {
            IAuthenticationProvider provider = RestogramClient.getInstance().getAuthenticationProvider();
            if(!provider.isUserLoggedIn())
                activity.finish();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(activity.getClass() == PersonalActivity.class) {
            IAuthenticationProvider provider = RestogramClient.getInstance().getAuthenticationProvider();
            if(!provider.isUserLoggedIn())
                activity.finish();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // Empty
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // Empty
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Empty
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            activities.remove(activity);

            if(activities.size() == 0) {
                if(RestogramClient.getInstance().isDebuggable()) {
                    Log.d("REST-O-GRAM", "Activity stack empty");
                }
                onTerminate();
            }
        }
        catch(Exception e) {
            // TODO
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if(RestogramClient.getInstance().isDebuggable()) {
            Log.d("REST-O-GRAM", "Low memory");
        }
    }

    @Override
    public boolean isInLastActivity() {
        if(activities.size() == 1)
            return true;

        if(activities.size() == 2)
        {
            for(Activity activity : activities) {
                if(activity.isFinishing())
                    return true;
            }
        }

        return false;
    }

    @Override
    public void restart() {
        // Initialize
        initialize();
    }

    @Override
    public void shutdown() {
        try {
            // Finish all activities
            for(Activity activity : activities) {
                activity.finish();
            }
        }
        catch(Exception e) {
            // Empty
        }
    }

    /**
     * Initializes client
     */
    private void initialize() {
        // Initialize client
        RestogramClient.getInstance().initialize(getApplicationContext(), this);
    }

    private Set<Activity> activities;
}
