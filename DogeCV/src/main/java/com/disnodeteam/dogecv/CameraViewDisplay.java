package com.disnodeteam.dogecv;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;


/**
 * Created by guinea on 6/23/17.
 * -------------------------------------------------------------------------------------
 * Copyright (c) 2018 FTC Team 5484 Enderbots
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * 
 * By downloading, copying, installing or using the software you agree to this license.
 * If you do not agree to this license, do not download, install,
 * copy or use the software.
 * -------------------------------------------------------------------------------------
 * Nicely adds/removes a View from the main body of the robot controller app screen.
 * As a singleton, you'll want to pass CameraViewDisplay.getInstance() instead of directly instantiating it.
 */

public class CameraViewDisplay implements ViewDisplay {
    private static CameraViewDisplay instance;

    View view;
    private CameraViewDisplay() {
    }

    public static CameraViewDisplay getInstance() {
        if (instance == null) instance = new CameraViewDisplay();
        return instance;
    }

    public void setCurrentView(final Context context, View newView) {
        // finding the resID dynamically allows this class to exist outside of the TeamCode module
        final int resID = context.getResources().getIdentifier("RelativeLayout", "id", context.getPackageName());
        final Activity activity = (Activity) context;
        final View queuedView = newView;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup l = (ViewGroup) activity.findViewById(resID); //R.id.RelativeLayout);
                if (view != null) {
                    l.removeView(view);
                }
                l.addView(new ScrollView(context));
                l.addView(queuedView);
                view = queuedView;
            }
        });
    }

    public void removeCurrentView(Context context) {
        final int resID = context.getResources().getIdentifier("RelativeLayout", "id", context.getPackageName());
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //cameraMonitorViewId
                ViewGroup l = (ViewGroup) activity.findViewById(resID); // .id.RelativeLayout);
                if (view != null) {
                    l.removeView(view);
                }
                view = null;
            }
        });
    }
}
