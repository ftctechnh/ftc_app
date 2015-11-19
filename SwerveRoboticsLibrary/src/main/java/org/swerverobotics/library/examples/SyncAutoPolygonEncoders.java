package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * This is similar to LinearAutonomousPolygon, but uses a 'run to position'
 * approach rather than a timed delay approach for determining when events
 * should end.
 *
 * This opmode works with both legacy and modern motor controllers, and
 * expects two motors, named "motorLeft" and "motorRight".
 */
@Autonomous(name="Auto Polygon (Sync, encoders)", group="Swerve Examples")
@Disabled
public class SyncAutoPolygonEncoders extends SynchronousOpMode
    {
    DcMotor motorRight;
    DcMotor motorLeft;

    // The number of encoder ticks per motor shaft revolution. 1440 is correct
    // for HiTechnic motors. Andy Mark motors are 1120 ticks per revolution.
    final int    encRotation = 1440;

    // Just for simple demonstration, we have very short encoder increments
    // that we use for straight ahead and for turning motion. A real robot
    // would use longer values (or, better, would use a gyro to measure actual
    // turning angle).
    final int    dencStraight = encRotation * 4;
    final int    dencTurn = encRotation * 2;

    // We apply a certain power level (currently full power) when we move
    final double drivePower  = 1.0;

    // We draw four sides of a polygon
    final int    numSides    = 4;

    public @Override void main() throws InterruptedException
        {
        motorLeft   = hardwareMap.dcMotor.get("motorLeft");
        motorRight  = hardwareMap.dcMotor.get("motorRight");

        // One of the motors should go in the reverse direction
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        // Zero out the encoders
        setModes(DcMotorController.RunMode.RESET_ENCODERS);

        // Indicate the power level we want the motors to run at. Nothing
        // will move right now, as the motors are currently in RESET_ENCODERS mode
        motorLeft.setPower(drivePower);
        motorRight.setPower(drivePower);

        // Set the target positions as the same as the current positions, then
        // arm the world by switching to RUN_TO_POSITION mode. Again, nothing will
        // move, since the motors are already at the target position. But from here
        // on out, all we need to do is change motor target positions and the motors
        // will respond and move there.
        setPositionDelta(0, 0);
        setModes(DcMotorController.RunMode.RUN_TO_POSITION);

        // Pause until the start button is pressed
        this.waitForStart();

        // Draw our polygon
        for (int i = 0; i < numSides; i++)
            {
            goStraight();
            turnLeft();
            }

        // Kill the motors and return
        stopRobot();
        }

    void setPositionDelta(int dposLeft, int dposRight)
        {
        motorLeft.setTargetPosition(motorLeft.getCurrentPosition() + dposLeft);
        motorRight.setTargetPosition(motorRight.getCurrentPosition() + dposRight);
        }

    /** sets the modes of both motors to the indicated value */
    void setModes(DcMotorController.RunMode mode)
        {
        motorLeft.setMode(mode);
        motorRight.setMode(mode);
        }

    void goStraight() throws InterruptedException
        {
        setPositionDelta(dencStraight, dencStraight);
        waitUntilMotorsNotBusy();
        }

    void turnLeft() throws InterruptedException
        {
        setPositionDelta(0, dencTurn);
        waitUntilMotorsNotBusy();
        }

    /** this waits until the motors have reached their RUN_TO_POSITION targets */
    void waitUntilMotorsNotBusy() throws InterruptedException
        {
        while (motorLeft.isBusy() || motorRight.isBusy())
            idle();
        }

    void stopRobot()
        {
        motorLeft.setPower(0);
        motorRight.setPower(0);
        }
    }
