package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "CompTele", group = "Tele")
@Disabled
public class CompTeleFinal extends OpMode
{
    NewRobotFinal newRobot;
    boolean isWingUp = true;

    public void init()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        gamepad2.setJoystickDeadzone(.15f);//attachments dude
        gamepad1.setJoystickDeadzone(.15f);//driver
    }

    public void start()
    {

    }

    public void loop()
    {
        telemetry.addData("Lift enc",newRobot.getLiftMotor().getCurrentPosition());
        telemetry.update();

        if(gamepad2.dpad_up && gamepad2.a)
            newRobot.moveLift(1, .76f);
        else if (gamepad2.dpad_up)
            newRobot.moveLift(1);

        if(gamepad2.dpad_down && gamepad2.a)
            newRobot.moveLift(-1, .76f);
        else if (gamepad2.dpad_down)
            newRobot.moveLift(-1);

        newRobot.fineMoveLift(gamepad2.left_stick_y);

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
