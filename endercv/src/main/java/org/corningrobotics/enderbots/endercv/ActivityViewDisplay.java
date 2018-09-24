package org.corningrobotics.enderbots.endercv;

import android.app.Activity;
import android.content.Context;
import android.view.View;

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
