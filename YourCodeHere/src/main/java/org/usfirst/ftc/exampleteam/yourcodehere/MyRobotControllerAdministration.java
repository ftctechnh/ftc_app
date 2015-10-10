package org.usfirst.ftc.exampleteam.yourcodehere;

import android.content.Context;
import android.media.MediaPlayer;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.examples.*;
import com.qualcomm.ftcrobotcontroller.opmodes.*;

/**
 * MyRobotControllerAdministration is a container for administrative methods that intereract
 * with the Swerve library. You don't <em>have to</em> put your adminstrative methods in a separate
 * class like this, but it does help keep them neat and tidy.
 *
 * <p>NOTE: if you previously cloned YourCodeHere for your team and per the then-current
 * instructions edited FtcRobotController\build.gradle to mention your project, you should
 * now remove that: FtcRobotController\build.gradle should now always be the original, verbatim
 * version as currently found here. Then, in your own project's build.gradle make necessary
 * changes to match the now-current YourCodeHere\build.gradle. Specifically, ensure that:
 * <ol>
 *     <li>it uses <code>apply plugin: 'com.android.<em>application</em>'</code> not <code>...android.<em>module</em>'</code></li>
 *     <li>its <code>defaultConfig</code> section contains the following</li>
 *         <ol>
 *         <li>the verbatim line <code>applicationId 'com.qualcomm.ftcrobotcontroller'</code></li>
 *         <li>a <code>versionCode</code> line with the version three or greater: <code>versionCode 3</code></li>
 *         </ol>
 *     <li>its <code>dependencies</code> section contains the line <code>compile project(':FtcRobotController')</code></li>
 * </ol></p>
 *
 * <p>If you're new to us here, these updates have already been done for you.</p>
 *
 * @see TeleOp
 * @see Autonomous
 * @see OpModeRegistrar
 * @see OnRobotRunning
 * @see SynchTeleOp
 */
public abstract class MyRobotControllerAdministration
    {
    /**
     * Registers any library OpModes that you wish to display. Change this code to suit
     * your needs: the specific OpModes that are registered as this code comes from
     * the factory are probably not what you want.
     *
     * Annotating a public static method with @OpModeRegistrary like this can be used to register
     * OpModes for display in the driver station menu. It is particularly useful for
     * registering OpModes that are found in libraries in which you are unable to or would prefer not to
     * modify the source code. For your own OpModes, though you could register them
     * here, it is preferable to annotate them in their own source with {@link TeleOp}
     * or {@link Autonomous} annotations, as appropriate
     *
     * @param context   the application context of the robot controller application. Not often
     *                  actually used in OpMode registrar functions.
     * @param manager   the object through which registrations are effected
     */
    @OpModeRegistrar
    public static void RegisterMyOpModes(Context context, IOpModeManager manager)
        {
        // As an example, we here register some examples from the Swerve library
        // and one of the FTC HQ example opmodes. You'll probably want to change that.

        manager.register(SynchTeleOp.class);
        manager.register(SynchTelemetryOp.class);
        manager.register("FTC HQ NxtTeleOp", NxtTeleOp.class);
        }

    /**
     * Any public static method annotated with {@link OnRobotRunning} is invoked when the robot
     * object in the robot controller application enters the running state following an initial
     * boot or a 'restart robot'.
     *
     * @param context   the application context of the robot controller application. Useful for
     *                  interacting with other parts of the Android system, such creating a
     *                  MediaPlayer.
     */
    @OnRobotRunning
    public static void PlaySoundOnRobotRunning(Context context)
        {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.nxtstartup);
        mediaPlayer.start();
        }
    }
