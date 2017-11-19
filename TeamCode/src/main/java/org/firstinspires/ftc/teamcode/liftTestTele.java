package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "liftTestTele", group = "Tele")
public class liftTestTele extends OpMode
{
    NewRobot newRobot;
    boolean isWingUp = true;
    public void init()
    {
        newRobot = new NewRobot(hardwareMap);
        gamepad1.setJoystickDeadzone(.15f);
    }

    public void start()
    {

    }

    public void loop()
    {
        telemetry.addData("Lift enc",newRobot.getLiftMotor().getCurrentPosition());
        telemetry.update();

        if(gamepad1.dpad_up && gamepad1.a)
            newRobot.moveLift(1, .78f);
        else if (gamepad1.dpad_up)
            newRobot.moveLift(1);

        if(gamepad1.dpad_down && gamepad1.a)
            newRobot.moveLift(-1, .78f);
        else if (gamepad1.dpad_down)
            newRobot.moveLift(-1);

        newRobot.fineMoveLift(gamepad1.left_stick_y);
//test wi gs
        telemetry.addData("ARM ENCODER= ", newRobot.getWingMotor().getCurrentPosition());
        if (gamepad1.y)
            newRobot.getWingMotor().setPower(.75);
        else if (gamepad1.b)
        {
            isWingUp = !isWingUp;
            newRobot.moveWing(isWingUp);
        }
        else
            newRobot.getWingMotor().setPower(0);
    }
}
