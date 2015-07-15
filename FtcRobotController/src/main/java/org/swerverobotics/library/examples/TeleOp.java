package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;

/**
 * A very simple example of a synchronous op mode.
 *
 * Note that this is in its very primitive state
 */
public class TeleOp extends SynchronousOpMode
    {
    // Note: all hardware variables can only be initialized inside the main() function,
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
            if (this.inputAvailable.tryAcquire())
                {
                // There is (likely) new joystick material available
                }
            else
                {

                }
            }
        }
    }
