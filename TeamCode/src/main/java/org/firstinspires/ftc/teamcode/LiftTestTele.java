package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "liftTestTeleNew", group = "Tele")
public class LiftTestTele extends OpMode
{
    NewRobotFinal newRobot;
    boolean liftArmed = true;
    //boolean isWingUp = true;
    //Servo leftDoorWall = null;
    //Servo rightDoorWall = null;

    public void init()
    {

        newRobot = new NewRobotFinal(hardwareMap);
    }

    public void start()
    {

    }

    public void loop()
    {
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
            {   telemetry.addData("In calclift -1", null);
                newRobot.CalcLiftTarget(-1);
                liftArmed = false;
            }
        }
        else
        {
            telemetry.addData("Armedlift true", null);
            liftArmed = true;
        }
        telemetry.addData("lift count", newRobot.getLiftMotor().getCurrentPosition());
        telemetry.update();
        newRobot.fineMoveLift(gamepad2.left_stick_y, .9f);
    }
}
