package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;

/**
 * A very simple example of a synchronous op mode.
 *
 * Note that this example is still in a very primitive state of development
 */
public class TeleOp extends SynchronousOpMode
    {
    // All hardware variables can only be initialized inside the main() function,
    // not here at their member variable declarations.
    DcMotor motorLeft = null;
    DcMotor motorRight = null;

    @Override protected void main()
        {
        // Initialize our hardware variables
        this.motorLeft = this.hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = this.hardwareMap.dcMotor.get("motorRight");

        for (;;)
            {
            if (this.gamePadInputAvailable())
                {
                // There is (likely) new gamepad input available
                }
            else
                {
                // There is no new gamepad input available
                }
            }
        }
    }
