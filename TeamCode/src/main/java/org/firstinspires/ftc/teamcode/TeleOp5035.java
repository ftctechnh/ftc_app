package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 * <p/>
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * <p/>
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 * <p/>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "TankDrive TeleOp")
public class TeleOp5035 extends OpMode {
    Hardware5035 robot = new Hardware5035();


    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        //tank drive
        if (null != robot.leftMotor) robot.leftMotor.setPower(-gamepad1.left_stick_y);
        if (null != robot.rightMotor) robot.rightMotor.setPower(-gamepad1.right_stick_y);

        ballFiringLoop();
        ballPickupLoop();
    }

    public void ballFiringLoop() {
        // starts ball booster motors when x is pressed and shuts them down when A is pressed.
        if (gamepad2.x == true)
        {
            if (null != robot.ballBooster1) robot.ballBooster1.setPower(1);
            if (null != robot.ballBooster2) robot.ballBooster2.setPower(1);
        }
        if(gamepad2.a == true)
        {
            if (null != robot.ballBooster1) robot.ballBooster1.setPower(0);
            if (null != robot.ballBooster2) robot.ballBooster2.setPower(0);
        }
        //if ball boosters are on or the gamepad2.guide button is pressed, and the gamepad2.righttrigger is pressed, the pop up servo fires.
        if ((robot.ballBooster1.getPower() > 0.9 || gamepad2.guide == true) && gamepad2.right_trigger > 0.5)
        {
            if(null !=robot.popUp)              robot.popUp.setPosition(.25);

        }
        if(gamepad2.right_trigger < 0.25)
        {
            if(null !=robot.popUp)              robot.popUp.setPosition(0);

        }
    }
    public void ballPickupLoop()
    {
        if(gamepad1.x == true)
        {
            if(null !=robot.ballDump)          robot.ballDump.setPower(1);
        }
        else if (gamepad1.a == true)
        {
            if(null !=robot.ballDump)           robot.ballDump.setPower(-1);
        }
        else
        {
            if(null !=robot.ballDump)          robot.ballDump.setPower(0);
        }

    }


}
