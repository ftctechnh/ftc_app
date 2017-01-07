/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

@TeleOp(name = "TeleOpMain", group = "Main")
//@Disabled
public class TeleOpMain extends OpMode {

    BotHardware robot = new BotHardware();

    boolean lastLeftBumperState = false;
    boolean lastRightBumperState = false;
    double leftPusherState = -1.0;
    double rightPusherState = -1.0;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        robot.init(this, false);

        robot.leftServo.setPosition(leftPusherState);
        robot.rightServo.setPosition(rightPusherState);
    }

    @Override
    public void init_loop(){
        telemetry.addData("NavX Ready", robot.startNavX());
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

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
            robot.setBackPower(1.0);
        }
        else if(gamepad1.dpad_up && gamepad1.dpad_right) {
            robot.setFrontPower(1.0);
            robot.setBackPower(0.0);
        }
        else if(gamepad1.dpad_down && gamepad1.dpad_left) {
            robot.setFrontPower(-1.0);
            robot.setBackPower(0.0);
        }
        else if(gamepad1.dpad_down && gamepad1.dpad_right) {
            robot.setFrontPower(0.0);
            robot.setBackPower(-1.0);
        }
        else if(gamepad1.dpad_up) {
            robot.setFrontPower(1.0);
            robot.setBackPower(1.0);
        }
        else if(gamepad1.dpad_left) {
            robot.setFrontPower(-1.0);
            robot.setBackPower(1.0);
        }
        else if(gamepad1.dpad_right) {
            robot.setFrontPower(1.0);
            robot.setBackPower(-1.0);
        }
        else if(gamepad1.dpad_down) {
            robot.setFrontPower(-1.0);
            robot.setBackPower(-1.0);
        }
        else {
            // joystick tank steering
            robot.frontLeftMotor.setPower(-gamepad1.left_stick_y);
            robot.frontRightMotor.setPower(-gamepad1.right_stick_y);
            robot.backLeftMotor.setPower(-gamepad1.left_stick_y);
            robot.backRightMotor.setPower(-gamepad1.right_stick_y);
        }

        // run lifter motor
        if(gamepad1.left_trigger > 0.0 || gamepad2.b) {
            robot.lifterMotor.setPower(1.0);
            robot.sweeperMotor.setPower(-1.0);
        }
        else if(gamepad1.right_trigger > 0.0 || gamepad2.b) {
            robot.lifterMotor.setPower(-1.0);
            robot.sweeperMotor.setPower(1.0);
        }
        else {
            robot.lifterMotor.setPower(0.0);
            robot.sweeperMotor.setPower(0.0);
        }

        // run launcher motor
        if(gamepad2.a) {
            robot.launcherMotor.setPower(1.0);
        }
        else {
            robot.launcherMotor.setPower(0.0);
        }

        //open ball gate
        if (gamepad2.b){
            robot.ballGate.setPosition(1.0);
        } else {
            robot.ballGate.setPosition(0.0);
        }


        //open/close cap ball mech
        robot.leftLift.setPosition(gamepad2.left_trigger);
        robot.rightLift.setPosition(gamepad2.right_trigger);

        // toggle button pushers
        if(!lastLeftBumperState && gamepad1.left_bumper) {
            leftPusherState *= -1.0;
        }
        if(!lastRightBumperState && gamepad1.right_bumper) {
            rightPusherState *= -1.0;
        }

        robot.leftServo.setPosition(leftPusherState);
        robot.rightServo.setPosition(rightPusherState);

        lastLeftBumperState = gamepad1.left_bumper;
        lastRightBumperState = gamepad1.right_bumper;
    }


    public static float[] getCorrectedSquirrleyMotorPowers(float dt, float mDirection, float mHeading, HeadingSensor mGyro, SensorLib.PID mPid, float mPower) {
        final float heading = mGyro.getHeading();     // get latest reading from direction sensor
        // convention is positive angles CCW, wrapping from 359-0

        final float error = SensorLib.Utils.wrapAngle(heading - mHeading);   // deviation from desired heading
        // deviations to left are positive, to right are negative

        // feed error through PID to get motor power correction value
        final float correction = -mPid.loop(error, dt);

        //calculate motor powers for fancy wheels
        AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(mDirection);

        final float leftPower = correction;
        final float rightPower = -correction;

        //fr, br, fl, bl
        final float[] ret = {
                (rightPower + (float)mp.Front()) * mPower,
                (rightPower + (float)mp.Back()) * mPower,
                (leftPower + (float)mp.Front()) * mPower,
                (leftPower + (float)mp.Back()) * mPower};

        return ret;
    }
}
