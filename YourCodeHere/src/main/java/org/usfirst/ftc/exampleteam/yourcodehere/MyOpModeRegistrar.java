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
 * @see TeleOp
 * @see Autonomous
 * @see OpModeRegistrar
 */
public class MyOpModeRegistrar
    {
    /**
     * Register any library OpModes that we wish to display. Change this code to suit
     * your needs.
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
        }
    }
