package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * This is similar to LinearAutonomousPolygon, but uses a 'run to position'
 * approach rather than a timed delay approach, and it
 */
@Autonomous(name="Autonomous Polygon (encoders)", group="Swerve Examples")
public class SyncAutonomousPolygonEncoders extends SynchronousOpMode
    {
    DcMotor motorRight;
    DcMotor motorLeft;

    final int    encRotation = 1440;
    final int    encStraight = encRotation * 10;
    final int    encTurn     = encRotation * 3;
    final double drivePower  = 1.0;
    final int    numSides    = 4;

    public @Override void main() throws InterruptedException
        {
        motorLeft   = hardwareMap.dcMotor.get("motorLeft");
        motorRight  = hardwareMap.dcMotor.get("motorRight");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        setModes(DcMotorController.RunMode.RESET_ENCODERS);

        motorLeft.setPower(drivePower);
        motorRight.setPower(drivePower);

        setPositionDelta(0, 0);
        setModes(DcMotorController.RunMode.RUN_TO_POSITION);

        this.waitForStart();

        for (int i = 0; i < numSides; i++)
            {
            goStraight();
            turnLeft();
            }

        stopRobot();
        }

    void setPositionDelta(int dposLeft, int dposRight)
        {
        motorLeft.setTargetPosition(motorLeft.getCurrentPosition() + dposLeft);
        motorRight.setTargetPosition(motorRight.getCurrentPosition() + dposRight);
        }

    void setModes(DcMotorController.RunMode mode)
        {
        motorLeft.setMode(mode);
        motorRight.setMode(mode);
        }

    void goStraight() throws InterruptedException
        {
        setPositionDelta(encStraight, encStraight);
        while (motorLeft.isBusy() || motorRight.isBusy())
            idle();
        }

    void turnLeft() throws InterruptedException
        {
        setPositionDelta(0, encTurn);
        while (motorLeft.isBusy() || motorRight.isBusy())
            idle();
        }

    void stopRobot()
        {
        motorLeft.setPower(0);
        motorRight.setPower(0);
        }
    }
