package org.usfirst.ftc.exampleteam.yourcodehere;

import android.content.Context;
import android.media.MediaPlayer;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.examples.*;
import com.qualcomm.ftcrobotcontroller.opmodes.*;

/**
 * MyRobotControllerAdministration is a container for 'administrative' methods that interact
 * with the Swerve library. You don't <em>have to</em> put your administrative methods in a separate
 * class as we do here, but it does help keep them neat and tidy. Administrative methods are
 * each tagged with a Java annotation that connotes and bestows their significance; see the
 * individual example methods for details. Note that administrative methods don't reside in any
 * given OpMode, but rather are used and invoked outside of the OpMode life cycle.
 *
 * <p>NOTE: if you previously cloned YourCodeHere for your team and per the then-current
 * instructions edited FtcRobotController\build.gradle to mention your project, you should
 * now remove that: FtcRobotController\build.gradle should now always be the original, verbatim
 * version as currently found here. Then, in your own project's build.gradle make necessary
 * changes to match the now-current YourCodeHere\build.gradle. Specifically, ensure that:</p>
 * <ol>
 *     <li>it uses <code>apply plugin: 'com.android.<em>application</em>'</code> not <code>...android.<em>module</em>'</code></li>
 *     <li>its <code>defaultConfig</code> section contains the following</li>
 *         <ol>
 *         <li>the verbatim line <code>applicationId 'com.qualcomm.ftcrobotcontroller'</code></li>
 *         <li>a <code>versionCode</code> line with the version three or greater: <code>versionCode 3</code></li>
 *         </ol>
 *     <li>its <code>dependencies</code> section contains the line <code>compile project(':FtcRobotController')</code></li>
 * </ol>
 *
 * <p>If you're new to us here, these updates have already been done for you.</p>
 *
 * @see TeleOp
 * @see Autonomous
 * @see OpModeRegistrar
 * @see OnRobotRunning
 * @see SynchTeleOp
 */
public class MyRobotControllerAdministration
    {
    /**
     * Registers any library OpModes that you wish to display. Change this code to suit
     * your needs: the specific OpModes that are registered as this code comes from
     * the factory are probably not what you want. For your own OpModes, though you could register them
     * here, it is preferable to annotate them in their own source with {@link TeleOp}
     * or {@link Autonomous} annotations, as appropriate.
     *
     * @param context   the application context of the robot controller application. Not often
     *                  actually used in OpMode registrar functions.
     * @param manager   the object through which registrations are effected
     */
    @OpModeRegistrar
    public static void registerMyOpModes(Context context, IOpModeManager manager)
        {
        // As an example, we here register some examples from the Swerve library
        // and one of the FTC HQ example opmodes. You'll probably want to change that.

        manager.register(SynchTeleOp.class);
        manager.register(SynchTelemetryOp.class);
        manager.register(LinearAutonomousPolygon.class);
        manager.register("FTC HQ NxtTeleOp", NxtTeleOp.class);
        }

    /**
     * Any public static method annotated with {@link OnRobotRunning} is invoked when the robot
     * object in the robot controller application enters the running state following an initial
     * boot or a 'restart robot'. One thing useful to do here is to play a sound of some sort
     * to provide an audible indicator that the robot is ready for use with the driver station,
     * but you could do whatever you like.
     *
     * @param context   the application context of the robot controller application. Useful for
     *                  interacting with other parts of the Android system, such creating a
     *                  MediaPlayer.
     * @see OnRobotRunning
     * @see OnRobotStartupFailure
     * @see #playSoundOnRobotStartupFailure(Context)
     */
    @OnRobotRunning
    public static void playSoundOnRobotRunning(Context context)
        {
        playSound(context, R.raw.nxtstartup);
        }

    /**
     * Any public static method annotated with {@link OnRobotRunning} is invoked when the robot
     * object in the robot controller application fails to enter the running state during
     * an attempt to do so. A common cause of such failures is a mismatch between the robot
     * configuration file and the devices currently attached to the robot.
     *
     * @param context   the application context of the robot controller application. Useful for
     *                  interacting with other parts of the Android system, such creating a
     *                  MediaPlayer.
     * @see #playSoundOnRobotRunning(Context)
     */
    @OnRobotStartupFailure
    public static void playSoundOnRobotStartupFailure(Context context)
        {
        playSound(context, R.raw.chord);
        }

    /** Plays a sound given the sounds identity as a (raw) resource. */
    static void playSound(Context context, int resource)
        {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resource);
        mediaPlayer.start();
        while (mediaPlayer.isPlaying())
            Thread.yield();
        mediaPlayer.release();
        }
    }
