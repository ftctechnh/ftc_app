package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "CompTele", group = "Tele")
public class CompTeleFinal extends OpMode
{
    boolean isWingUp = true;
    NewRobotFinal newRobot;

    public void init()
    {
        gamepad2.setJoystickDeadzone(.15f);//attachments
        gamepad1.setJoystickDeadzone(.15f);//driver
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
        if (gamepad2.dpad_up && gamepad2.y)
            newRobot.moveLift(1, .76f);
        else if (gamepad2.dpad_up)
            newRobot.moveLift(1);

        if (gamepad2.dpad_down && gamepad2.y)
            newRobot.moveLift(-1, .76f);
        else if (gamepad2.dpad_down)
            newRobot.moveLift(-1);

        newRobot.fineMoveLift(gamepad2.left_stick_y, .76f);

        if (gamepad2.left_bumper)
            newRobot.fineAdjDoors(.1f);
        else if (gamepad2.left_trigger > .2)
            newRobot.fineAdjDoors(-.1f);

        if (gamepad2.right_bumper)
            newRobot.getTailRelease().setPower(-.6f);//release
        else if (gamepad2.right_trigger > .2)
            newRobot.getTailRelease().setPower(.6f);//retract
        else
            newRobot.getTailRelease().setPower(0);

        if (gamepad2.a)
            newRobot.fineAdjGrabber(.1f);
        else if (gamepad2.b)
            newRobot.fineAdjGrabber(-.1f);

        /**
         *DRIVE CONTROLS
         * GAMEPAD 1
         */
        if(gamepad1.a)
            newRobot.autoPark();
        else
             newRobot.driveMotors(gamepad1.left_stick_y, -gamepad1.right_stick_y);

        if (gamepad1.y)
            newRobot.getWingMotor().setPower(.6);//lift wing
        else if (gamepad1.b)
            newRobot.getWingMotor().setPower(-.6);
        else
            newRobot.getWingMotor().setPower(0);

        telemetry.addData("gamepad2 Trigger l", gamepad2.left_trigger);
        telemetry.addData("gamepad2 Trigger r", gamepad2.right_trigger);
        telemetry.addData("gamepad1 joystick l", gamepad1.left_stick_y);
        telemetry.addData("gamepad1 joystick r", gamepad1.left_stick_x);
        telemetry.update();
    }

    public void stop()
    {
        newRobot.stopAllMotors();
        newRobot.kill();
    }

}
