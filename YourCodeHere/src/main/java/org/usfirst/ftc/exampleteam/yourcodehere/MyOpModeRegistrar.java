package org.usfirst.ftc.exampleteam.yourcodehere;

import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.examples.*;

/**
 * {@link MyOpModeRegistrar} can be used to register OpModes for display in the
 * driver station menu. It is particularly useful for registering OpModes that
 * are found in libraries in which you are unable to or would prefer not to
 * modify the source code. For your own OpModes, though you could register them
 * here, it is preferable to annotate them in their own source with {@link TeleOp}
 * or {@link Autonomous} annotations, as appropriate.
 *
 * <p>NOTE: To make this work, for the moment at least you need to add your library module
 * 'YourCodeHere' to the 'build.gradle' file in the FtcRobotController library. That build.gradle
 * (be aware: each Android Studio module has its own) is found in $PROJECT_DIR$\FtcRobotController\build.gradle,
 * where $PROJECT_DIR$ is the directory which contains SwerveRoboticsLibrary and YourCodeHere in addition to
 * FtcRobotController. In that 'build.gradle', make sure the 'dependencies' section has a line
 * compile project(':YourCodeHere') in addition to compile project(':SwerveRoboticsLibrary')
 * and other things.</p>
 *
 * <p>For the YourCodeHere module specifically, this has already been done for you. If you
 * create your own sibling modules, you'll have to do that for yourself. We hope in the future
 * to obviate the need for this step.</p>
 *
 * <p>UPDATE: at the moment, this is slightly broken, in that you cannot here reference
 * the OpModes in the FtcRobotController module written by FTC HQ. Until that is fixed (we're
 * working on it), if you want to use any of those OpModes you'll have to go back to the old
 * way of modifying FtcOpModeRegister.register() directly. We apologize for the inconvenience.</p>
 *
 * @see TeleOp
 * @see Autonomous
 * @see OpModeRegistrar
 */
public class MyOpModeRegistrar
    {
    /**
     * Register any library OpModes that we wish to display. Change this code to suit
     * your needs: the specific OpModes that are registered as this code comes from
     * the factory are probably not what you want.
     *
     * @param manager   the object through which registrations are effected
     */
    @OpModeRegistrar
    public static void Register(IOpModeManager manager)
        {
        // As an example, we here register some examples from the Swerve library.
        // You'll probably want to change that.

        manager.register(SynchTeleOp.class);
        manager.register(SynchTelemetryOp.class);
        // manager.register("HQ NxtTeleoP", com.qualcomm.ftcrobotcontroller.opmodes.NxtTeleOp); // need to make this work!
        }
    }
