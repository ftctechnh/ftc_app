package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "CompTeleDemo", group = "Tele")
public class CompTeleDemo extends OpMode
{
    NewRobotFinal newRobot;

    public void init ()
    {
        gamepad2.setJoystickDeadzone(.2f);//attachments
        gamepad1.setJoystickDeadzone(.2f);//driver
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.openOrCloseDoor(false);
    }

    public void start ()
    {

    }

    public void loop ()
    {
        /**
         * ATTACHMENTS CONTROLLER
         * GAMEPAD 2
         */
        newRobot.fineMoveLift(gamepad2.left_stick_y, .76f);

        if (gamepad2.left_bumper)
            newRobot.fineAdjDoors(-.16f);
        else if (gamepad2.left_trigger > .2)
            newRobot.fineAdjDoors(.16f);

        /**
         *DRIVE CONTROLS
         * GAMEPAD 1
         */

        if (gamepad1.right_trigger > .4f)
            newRobot.driveMotors(gamepad1.left_stick_y / 2, gamepad1.right_stick_y / 2);
        else
            newRobot.driveMotors(gamepad1.left_stick_y, gamepad1.right_stick_y);

        if (gamepad1.y)
        {
            if (!newRobot.getWingTouchSens().getState())
            {

            }
            else
            newRobot.getWingMotor().setPower(1);//lift wing
        }
        else if (gamepad1.b)
        {
            if (!newRobot.getWingTouchSens().getState())
            {

            }
            else
                newRobot.getWingMotor().setPower(-1f);
        }
        else
            newRobot.getWingMotor().setPower(0f);

        telemetry.addData("LiftEnc", newRobot.getLiftMotor().getCurrentPosition());
        telemetry.addData("RightDriveEnc ", newRobot.getDriveRightOne().getCurrentPosition());
        telemetry.addData("LeftDriveEnc", newRobot.getDriveLeftOne().getCurrentPosition());
        telemetry.addData("WingEnc", newRobot.getWingMotor().getCurrentPosition());
        telemetry.addData("Left Y", gamepad1.left_stick_y);
        telemetry.addData("Right y", gamepad1.right_stick_y);
        telemetry.update();
    }

    public void stop ()
    {
        newRobot.stopAllMotors();
    }
}
