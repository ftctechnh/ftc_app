package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Jeremy on 11/17/2017.
 */
@TeleOp(name = "liftTestTele", group = "Tele")
public class LiftTestTele extends OpMode
{
    NewRobot newRobot;
    boolean isWingUp = true;
    Servo leftDoorWall = null;
    Servo rightDoorWall = null;

    public void init()
    {
        leftDoorWall = hardwareMap.get(Servo.class, "leftDoorWall");
        rightDoorWall = hardwareMap.get(Servo.class, "rightDoorWall");
        newRobot = new NewRobot(hardwareMap);
        gamepad1.setJoystickDeadzone(.15f);
        rightDoorWall.scaleRange(-Math.PI/2, 0);
        leftDoorWall.scaleRange(-Math.PI/2, 0);
        rightDoorWall.setDirection(Servo.Direction.FORWARD);
        leftDoorWall.setDirection(Servo.Direction.REVERSE);
        gamepad2.setJoystickDeadzone(.14f);
    }

    public void start()
    {

    }

    public void loop()
    {
        //telemetry.addData("Lift enc",newRobot.getLiftMotor().getCurrentPosition());
        //telemetry.update();

        if(gamepad1.dpad_up && gamepad1.a)
            newRobot.moveLift(1, .4f);
        else if (gamepad1.dpad_up)
            newRobot.moveLift(1);
        if(gamepad1.left_bumper)
        {
            fineAdjDoors(.3);
        }
        else if (gamepad1.right_bumper)
        {
            fineAdjDoors(-.3);
        }

        if(gamepad1.dpad_down && gamepad1.a)
            newRobot.moveLift(-1, .4f);
        else if (gamepad1.dpad_down)
            newRobot.moveLift(-1);

        newRobot.fineMoveLift(gamepad1.left_stick_y);
//test wi gs
        //telemetry.addData("ARM ENCODER= ", newRobot.getWingMotor().getCurrentPosition());
        if (gamepad1.y)
            newRobot.getWingMotor().setPower(.3);
        else if (gamepad1.b)
        {
            isWingUp = !isWingUp;
            newRobot.moveWing(isWingUp);
        }
        else
            newRobot.getWingMotor().setPower(0);
    }

    public void openOrCloseDoor(boolean close)
    {
        if (!close)
        {
            leftDoorWall.setPosition(-Math.PI/4);
            rightDoorWall.setPosition(-Math.PI/4);
        }
        else
        {
            leftDoorWall.setPosition(Math.PI/4);
            rightDoorWall.setPosition(Math.PI/4);
        }
    }

    public void fineAdjDoors(double in)
    {
        leftDoorWall.setPosition(leftDoorWall.getPosition() + in);
        rightDoorWall.setPosition(rightDoorWall.getPosition() + in);
    }
}
