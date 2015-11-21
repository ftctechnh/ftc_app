package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * This synchronous OpMode makes the the robot move in the shape
 * of (approximately) a simple polygon using time-based motion limits.
 *
 * It expects two motors, named "motorLeft" and "motorRight".
 *
 * @see SyncAutoPolygonEncoders
 */
@Autonomous(name="Auto Polygon (Sync)", group="Swerve Examples")
@Disabled
public class SyncAutoPolygon extends SynchronousOpMode
    {
    DcMotor motorRight;
    DcMotor motorLeft;

    final long msStraight   = 2000;
    final long msTurn       = 1000;
    final double drivePower = 1.0;
    final int  numSides     = 4;

    public @Override void main() throws InterruptedException
        {
        motorLeft   = hardwareMap.dcMotor.get("motorLeft");
        motorRight  = hardwareMap.dcMotor.get("motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        this.waitForStart();

        for (int i = 0; i < numSides; i++)
            {
            goStraight();
            Thread.sleep(msStraight);

            turnLeft();
            Thread.sleep(msTurn);
            }

        stopRobot();
        }

    void goStraight()
        {
        motorLeft.setPower(drivePower);
        motorRight.setPower(drivePower);
        }
    void turnLeft()
        {
        motorLeft.setPower(-drivePower);
        motorRight.setPower(drivePower);
        }
    void stopRobot()
        {
        motorLeft.setPower(0);
        motorRight.setPower(0);
        }
    }

