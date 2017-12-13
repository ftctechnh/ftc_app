package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "CompTele", group = "Tele")
public class CompTeleFinal extends OpMode
{
    boolean isWingUp = true;
    boolean liftArmed = true;
    NewRobotFinal newRobot;

    public void init()
    {
        gamepad2.setJoystickDeadzone(.3f);//attachments
        gamepad1.setJoystickDeadzone(.3f);//driver
        newRobot = new NewRobotFinal(hardwareMap);
    }

    public void start()
    {
        // newRobot.initEndGame(hardwareMap);

    }

    public void loop()
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
        } else if (gamepad2.dpad_down)
        {
            if (liftArmed)
            {   telemetry.addData("In calclift -1", null);
                newRobot.CalcLiftTarget(-1);
                liftArmed = false;
            }
        } else
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

        telemetry.update();

        /*
        if (gamepad2.left_bumper)
            newRobot.fineAdjDoors(.001f);
        else if (gamepad2.left_trigger > .2f)
            newRobot.fineAdjDoors(-.001f);
          */
        if (gamepad2.left_bumper)           //DO NO
        {
            newRobot.openOrCloseDoor(false);
        } else if (gamepad2.left_trigger > .3f)
            newRobot.openOrCloseDoor(true);

        if (gamepad2.right_bumper)
            newRobot.getTailRelease().setPower(-.6f);//release
        else if (gamepad2.right_trigger > .2f)
            newRobot.getTailRelease().setPower(.6f);//retract
        else
            newRobot.getTailRelease().setPower(0f);

        if (gamepad2.a)
            newRobot.fineAdjGrabber(.01f);
        else if (gamepad2.b)
            newRobot.fineAdjGrabber(-.01f);

        if (gamepad2.y)
        {
            newRobot.fineAdjGrabberRotator(-.01f);
        } else if (gamepad2.x)
        {
            newRobot.fineAdjGrabberRotator(.01f);
        }


        /**
         *DRIVE CONTROLS
         * GAMEPAD 1
         */
        if (gamepad1.right_trigger > .4f)
            newRobot.driveMotors(gamepad1.left_stick_y / 2, -gamepad1.right_stick_y / 2);
        else
            newRobot.driveMotors(gamepad1.left_stick_y, -gamepad1.right_stick_y);

        //
        if (gamepad1.right_bumper)
        {
            telemetry.addData("GAMEPAD 1 R BUMPER", null);
            newRobot.driveMotors(-.9f, .9f);
            telemetry.addData("LEFT POW= ", newRobot.getDriveLeftOne().getPower());
            telemetry.addData("RIGHt POW= ", newRobot.getDriveRightOne().getPower());
        }
        telemetry.addData("LEFT POW= ", newRobot.getDriveLeftOne().getPower());
        telemetry.addData("RIGHt POW= ", newRobot.getDriveRightOne().getPower());

        if (gamepad1.y)
            newRobot.getWingMotor().setPower(.6f);//lift wing
        else if (gamepad1.b)
            newRobot.getWingMotor().setPower(-.6f);
        else
            newRobot.getWingMotor().setPower(0f);

        telemetry.addData("gamepad2 Trigger l", gamepad2.left_trigger);
        telemetry.addData("gamepad2 Trigger r", gamepad2.right_trigger);
        telemetry.addData("gamepad1 joystick l", gamepad1.left_stick_y);
        telemetry.addData("gamepad1 joystick r", gamepad1.right_stick_y);
        telemetry.update();
    }

    public void stop()
    {
        newRobot.stopAllMotors();
        newRobot.kill();
    }

}
