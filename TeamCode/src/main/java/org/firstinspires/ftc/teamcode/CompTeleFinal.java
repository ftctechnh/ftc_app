package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "CompTele", group = "Tele")
public class CompTeleFinal extends OpMode
{
    boolean liftArmed = true;
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
        // newRobot.initEndGame(hardwareMap);
    }

    public void loop ()
    {
        /**
         * ATTACHMENTS CONTROLLER FIRST
         * GAMEPAD 2
         */
        if (gamepad2.dpad_up)
        {
            if (liftArmed)
            {
                telemetry.addData("In calclift 1", null);
                newRobot.CalcLiftTarget(1);
                liftArmed = false;
            }
        }
        else if (gamepad2.dpad_down)
        {
            if (liftArmed)
            {
                telemetry.addData("In calclift -1", null);
                newRobot.CalcLiftTarget(-1);
                liftArmed = false;
            }
        }
        else
        {
            telemetry.addData("Armedlift true", null);
            liftArmed = true;
        }

        if (gamepad2.left_stick_y == 0)
        {
            telemetry.addData("left stick is 0, adj dir", null);
            telemetry.addData("liftdir", newRobot.getLiftDir());
            newRobot.AdjLiftDir();
        }
        else
        {
            newRobot.fineMoveLift(gamepad2.left_stick_y, .76f);
            telemetry.addData("Fine move lift", null);
        }

        if (gamepad2.left_bumper)
            newRobot.fineAdjDoors(-.16f);
        else if (gamepad2.left_trigger > .2)
            newRobot.fineAdjDoors(.16f);

        if (gamepad2.right_bumper)
            newRobot.getTailRelease().setPower(-1);//release
        else if (gamepad2.right_trigger > .2f)
            newRobot.getTailRelease().setPower(1);//retract
        else
            newRobot.getTailRelease().setPower(0f);

        if (gamepad2.a)
            newRobot.fineAdjGrabber(.04f);
        else if (gamepad2.b)
            newRobot.fineAdjGrabber(-.04f);

        if (gamepad2.x)
        {
            newRobot.fineAdjGrabberRotator(.004f);
        }
        else if (gamepad2.y)
        {
            newRobot.fineAdjGrabberRotator(-.004f);
        }

        /**
         *DRIVE CONTROLS
         * GAMEPAD 1
         */
        if (gamepad1.right_trigger > .4f)
            newRobot.driveMotors(gamepad1.left_stick_y / 2, gamepad1.right_stick_y / 2);
        else
            newRobot.driveMotors(gamepad1.left_stick_y, gamepad1.right_stick_y);

        if (gamepad1.y)
            newRobot.getWingMotor().setPower(1);//lift wing
        else if (gamepad1.b)
            newRobot.getWingMotor().setPower(-1f);
        else
            newRobot.getWingMotor().setPower(0f);

        telemetry.addData("LiftEnc", newRobot.getLiftMotor().getCurrentPosition());
        telemetry.addData("RightDriveEnc ", newRobot.getDriveRightOne().getCurrentPosition());
        telemetry.addData("LeftDriveEnc", newRobot.getDriveLeftOne().getCurrentPosition());
        telemetry.addData("Left Y", gamepad1.left_stick_y);
        telemetry.addData("Right y", gamepad1.right_stick_y);
        telemetry.update();
    }

    public void stop ()
    {
        newRobot.stopAllMotors();
    }

}
