package com.disnodeteam.dogecv;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by guinea on 6/23/17.
 * This ViewDisplay displays a View over the entire screen.
 * As a singleton, you'll want to pass ActivityViewDisplay.getInstance() instead of directly instantiating it.
 */

public class ActivityViewDisplay implements ViewDisplay {
    private static ActivityViewDisplay instance;
    private static View main = null;

    private ActivityViewDisplay() {
    }

    public static ActivityViewDisplay getInstance() {
        if (instance == null) instance = new ActivityViewDisplay();
        return instance;
    }

    public void setCurrentView(final Context context, final View view) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (main == null)
                    main = activity.getCurrentFocus();
                activity.setContentView(view);
            }
        });
    }

    public void removeCurrentView(final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setContentView(main.getRootView());
            }
        });
    }
}
