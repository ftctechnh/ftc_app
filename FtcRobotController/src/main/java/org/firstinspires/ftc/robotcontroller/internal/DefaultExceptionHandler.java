package org.firstinspires.ftc.robotcontroller.internal;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * <p>
 * This custom class is used to handle an uncaught internal exception within the robot controller app.
 * When hooked into the FtcRobotControllerActivity, it automatically restarts it in order to remain
 * functional on the field.
 * </p>
 *
 * To set this up, place this class in the folder:
 * <p>
 *     (ftc_app folder)/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/internal
 * </p>
 *
 * and add this line of code to the FtcRobotControllerActivity.java on a new line just below line 256:
 *
 *     Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, context));
 *
 * (credit to Chintan Rathod of http://www.chintanrathod.com for the base version of this class, which was adapted into this form)
 *
 * - researched and tested by guineawheek of ftc team 5484 enderbots
 */
public class DefaultExceptionHandler implements UncaughtExceptionHandler {

    private Activity activity;
    private Context context;

    public DefaultExceptionHandler(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // Log the exception so we know what happened
        Log.e("FtcRobotController", "Unhandled fatal exception - restarting app", ex);
        try {
            // set up the Intent to restart the app
            Intent intent = new Intent(activity, FtcRobotControllerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            // schedule the restart in two seconds
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pendingIntent);
            // finish the activity and close the dead app
            activity.finish();
            System.exit(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}