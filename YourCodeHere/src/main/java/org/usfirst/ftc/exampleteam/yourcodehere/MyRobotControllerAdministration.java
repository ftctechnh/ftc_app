package org.usfirst.ftc.exampleteam.yourcodehere;

import android.content.Context;
import android.media.MediaPlayer;

import org.swerverobotics.library.SwerveUtil;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.examples.*;
import com.qualcomm.ftcrobotcontroller.opmodes.*;

/**
 * MyRobotControllerAdministration is a container for 'administrative' methods that interact
 * with the Swerve Library. You don't <em>have to</em> put your administrative methods in a separate
 * class as we do here, but it does help keep them neat and tidy. Administrative methods are
 * each tagged with a Java annotation that connotes and bestows their significance; see the
 * individual example methods below for details. Note that administrative methods don't reside in any
 * given OpMode, but rather are used and invoked outside of the OpMode life cycle. Neither the
 * name of the administrative methods nor the name of the class are of significance; the only
 * important thing is the annotations with which they are decorated.
 *
 * As we've written things here, the code resides in an Android module named 'YourCodeHere' which
 * contains a package named 'org.usfirst.ftc.exampleteam.yourcodehere'. If you wish to change that,
 * or to create multiple such modules with different names, that's pretty straightforward: you can
 * begin by copying the entire YourCodeHere folder, then renaming the src\main\java\org\...\yourcodehere
 * path to reflect your new package name, whatever that might be. That new package name will also
 * of course need to appear in your copied .java files (including package-info.java), so those will
 * need their 'package' statement at the top adjusted accordingly. Perhaps less obviously,
 * the src\main\AndroidManifest.xml, which also contains the package name, will need to have the
 * 'package' attribute of the 'manifest' element adjusted. Finally, the new modules should be added
 * to the 'settings.gradle' file so they show up in Android Studio.
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
        SwerveUtil.playSound(context, R.raw.nxtstartup);
        }

    /**
     * Any public static method annotated with {@link OnRobotStartupFailure} is invoked when the robot
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
        SwerveUtil.playSound(context, R.raw.chord);
        }

    }
