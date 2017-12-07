package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "CompTele", group = "Tele")
public class CompTeleFinal extends OpMode
{
    NewRobotFinal newRobot;
    boolean isWingUp = true;

    public void init()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        gamepad2.setJoystickDeadzone(.15f);//attachments
        gamepad1.setJoystickDeadzone(.15f);//driver
    }

    public void start()
    {
        newRobot.stopAllMotors();
    }

    public void loop()
    {
        /**
         * ATTACHMENTS CONTROLLER FIRST
         * GAMEPAD 2
         */

        telemetry.addData("Lift enc",newRobot.getLiftMotor().getCurrentPosition());
        telemetry.update();

        if(gamepad2.dpad_up && gamepad2.y)
            newRobot.moveLift(1, .76f);
        else if (gamepad2.dpad_up)
            newRobot.moveLift(1);

        if(gamepad2.dpad_down && gamepad2.y)
            newRobot.moveLift(-1, .76f);
        else if (gamepad2.dpad_down)
            newRobot.moveLift(-1);

        newRobot.fineMoveLift(gamepad2.left_stick_y);

        if(gamepad2.left_bumper)
            newRobot.fineAdjDoors(1/180);
        else if(gamepad2.left_trigger > .5)
            newRobot.fineAdjDoors(-1/180);

        if(gamepad2.right_bumper)
            newRobot.getTailRelease().setPower(-.4f);//release
        else if (gamepad2.right_trigger > .5)
            newRobot.getTailRelease().setPower(.4f);//retract
        else
            newRobot.getTailRelease().setPower(0);

        if (gamepad2.a)
            newRobot.openOrCloseDoor(true);
        else if (gamepad2.b)
            newRobot.openOrCloseDoor(false);

        /**
         *DRIVE CONTROLS
         * GAMEPAD 1
         */
        telemetry.addData("ARM ENCODER= ", newRobot.getWingMotor().getCurrentPosition());

        if(gamepad1.a)
            newRobot.autoPark();
        else if (gamepad1.right_trigger < .4)
        {
            newRobot.getDriveLeftOne().setPower(gamepad1.left_stick_y);
            newRobot.getDriveRightOne().setPower(-gamepad1.right_stick_y);
        }
        else
        {
            newRobot.getDriveLeftOne().setPower(gamepad1.left_stick_y/2);
            newRobot.getDriveRightOne().setPower(-gamepad1.right_stick_y/2);
        }

        if (gamepad1.y)
            newRobot.getWingMotor().setPower(.3);//lift wing
        else if (gamepad1.b)
        {
            isWingUp = !isWingUp;
            newRobot.moveWing(isWingUp);
        }
        else
            newRobot.getWingMotor().setPower(0);
    }

    public void stop()
    {
        newRobot.stopAllMotors();
        newRobot.kill();
    }

}
