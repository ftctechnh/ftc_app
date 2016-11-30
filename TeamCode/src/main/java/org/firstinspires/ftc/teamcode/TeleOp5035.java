package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

@TeleOp(name = "TeleOp5035")
public class TeleOp5035 extends OpMode {
    Hardware5035 robot = new Hardware5035();


    boolean BallBoosterPoweringUp = false;
    boolean touchPressedLastLoop = false;
    boolean Reverse = false;
    int counter = 0;
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        //tank driveReverse add
        if(gamepad1.guide && Reverse == false && counter > 500) {
            Reverse = true;
            counter = 0;
        }
        if(gamepad1.guide && Reverse == true && counter > 500) {
            Reverse = false;
            counter = 0;
        }
        counter++;
        
        if(Reverse == false)
        {
        if (null != robot.leftMotor) robot.leftMotor.setPower(gamepad1.left_stick_y);
        if (null != robot.rightMotor) robot.rightMotor.setPower(gamepad1.right_stick_y);
        } else if (Reverse) {
            if (null != robot.leftMotor) robot.leftMotor.setPower(-gamepad1.right_stick_y);
            if (null != robot.rightMotor) robot.rightMotor.setPower(-gamepad1.left_stick_y);
        }

        ballFiringLoop();
        ballPickupLoop();
    }

    public void ballFiringLoop() {
        // starts ball booster motors when x is pressed and shuts them down when A is pressed.
        if (gamepad2.x == true)
        {
            BallBoosterPoweringUp = true;
        }
        if(gamepad2.a == true)
        {
            BallBoosterPoweringUp = false;
            if (null != robot.ballBooster1) robot.ballBooster1.setPower(0);
            if (null != robot.ballBooster2) robot.ballBooster2.setPower(0);
        }
        if(BallBoosterPoweringUp)
        {
            if (null != robot.ballBooster1) robot.ballBooster1.setPower(Math.max(robot.ballBooster1.getPower() + .000005,1));
            if (null != robot.ballBooster2) robot.ballBooster2.setPower(Math.max(robot.ballBooster2.getPower() + .000005,1));
        }
        //if ball boosters are on or the gamepad2.guide button is pressed, and the gamepad2.righttrigger is pressed, the pop up servo fires.
        if ((robot.ballBooster1.getPower() > 0.9 || gamepad2.guide == true) && gamepad2.right_trigger > 0.5)
        {
            robot.triggered();
        }
        if(gamepad2.right_trigger < 0.25)
        {
            robot.detriggered();
        }
    }
    public void ballPickupLoop(){
        if(gamepad1.right_trigger > .10)
        {
            if(null !=robot.ballDump)          robot.ballDump.setPower((gamepad1.right_trigger / 2));//Range.scale(gamepad1.left_trigger, .1, 1.0, 0.1, 0.5));
        }
        else if (gamepad1.left_trigger > .10)
        {
            if(null !=robot.ballDump)           robot.ballDump.setPower(-(gamepad1.left_trigger / 2));//-Range.scale(gamepad1.right_trigger, .1, 1.0, 0.1, 0.5));
        }
        else
        {
            if(null !=robot.ballDump)          robot.ballDump.setPower(0);
        }
        if(robot.grabbutton.isPressed() && touchPressedLastLoop == false){
            robot.ballDump.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        telemetry.addData("Values type = Unknown/ BallBooster is active", BallBoosterPoweringUp);
        telemetry.addData("Ball Booster Power", robot.ballBooster1.getPower());
        telemetry.addData("Position of server", robot.popUp.getPosition());
        telemetry.addData("popUp pos", robot.popUp.getPosition());
        telemetry.addData("Triggered Left", gamepad1.left_trigger);
        telemetry.addData("Triggered Right",gamepad1.right_trigger);
        telemetry.addData("motor encode", robot.ballDump.getCurrentPosition());
        telemetry.addData("this is a message", "hi");
        telemetry.update(); //this should work
        touchPressedLastLoop = robot.grabbutton.isPressed();
    }

}
