/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.Util;

@TeleOp(name = "Servo Calibrate", group = "Main")
//@Disabled
public class ServoAdjust extends OpMode {

    boolean currentServo = false;
    boolean dpadDelay = false;

    BotHardware robot = new BotHardware();

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        //currentServo = robot.leftServo;

        // hardware maps
        robot.init(this, false);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

        if(gamepad1.dpad_left || gamepad1.dpad_right ){
            if(!dpadDelay) currentServo = !currentServo;
            dpadDelay = true;
        }
        else dpadDelay = false;


        if(currentServo){
            telemetry.addData("Left Servo", robot.leftServo.getPosition());
            if(gamepad1.dpad_up) robot.leftServo.setPosition(Range.clip(robot.leftServo.getPosition() + 0.01, -1, 1));
            else if(gamepad1.dpad_down) robot.leftServo.setPosition(Range.clip(robot.leftServo.getPosition() - 0.01, -1, 1));
        }
        else {
            telemetry.addData("Right Servo", robot.rightServo.getPosition());
            if(gamepad1.dpad_up) robot.rightServo.setPosition(Range.clip(robot.rightServo.getPosition() + 0.01, -1, 1));
            else if(gamepad1.dpad_down) robot.rightServo.setPosition(Range.clip(robot.rightServo.getPosition() - 0.01, -1, 1));
        }

        /*

                         1,1,1,1
                            |
                 0,0,1,1    |    1,1,0,0
                            |
            -1,-1,1,1 ------------- 1,1,-1,-1
                            |
               -1,-1,0,0    |    0,0,-1,-1
                            |
                       -1,-1,-1,-1

        float x = gamepad1.left_stick_x;
        float y = -1 * gamepad1.left_stick_y;

        float ySign = (y >= 0) ? 1 : -1;

        float frontPower = (ySign * x > 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);
        float backPower = (ySign * x < 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);

        */

        // run drivetrain motors
        // dpad steering
        if(gamepad1.dpad_up && gamepad1.dpad_left) {
            robot.setFrontPower(0.0);
            robot.setBackPower(-1.0);
        }
        else if(gamepad1.dpad_up && gamepad1.dpad_right) {
            robot.setFrontPower(-1.0);
            robot.setBackPower(0.0);
        }
        else if(gamepad1.dpad_down && gamepad1.dpad_left) {
            robot.setFrontPower(1.0);
            robot.setBackPower(0.0);
        }
        else if(gamepad1.dpad_down && gamepad1.dpad_right) {
            robot.setFrontPower(0.0);
            robot.setBackPower(1.0);
        }
        else if(gamepad1.dpad_up) {
            robot.setFrontPower(-1.0);
            robot.setBackPower(-1.0);
        }
        else if(gamepad1.dpad_left) {
            robot.setFrontPower(1.0);
            robot.setBackPower(-1.0);
        }
        else if(gamepad1.dpad_right) {
            robot.setFrontPower(-1.0);
            robot.setBackPower(1.0);
        }
        else if(gamepad1.dpad_down) {
            robot.setFrontPower(1.0);
            robot.setBackPower(1.0);
        }
        else {
            // joystick tank steering
            robot.frontLeftMotor.setPower(gamepad1.left_stick_y);
            robot.frontRightMotor.setPower(gamepad1.right_stick_y);
            robot.backLeftMotor.setPower(gamepad1.left_stick_y);
            robot.backRightMotor.setPower(gamepad1.right_stick_y);
        }

        // run lifter motor
        if(gamepad2.left_bumper) {
            robot.lifterMotor.setPower(1.0);
        }
        else if(gamepad2.right_bumper) {
            robot.lifterMotor.setPower(-1.0);
        }
        else {
            robot.lifterMotor.setPower(0.0);
        }

        // run launcher motor
        if(gamepad2.a) {
            robot.launcherMotor.setPower(1.0);
        }
        else {
            robot.launcherMotor.setPower(0.0);
        }
    }
}
