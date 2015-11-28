package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by tdoylend on 2015-10-22.
 *
 * This class is overridden by the top-level opmode.
 * You use it to interface to the hardware components.
 *
 * Change log:
 * 1.5.0 - Added belt code
 * 1.4.3 - Refactor, added new code.
 * 1.4.2 - Added LEFT and RIGHT constants.
 * 1.4.1 - Fixed getUser().
 * 1.4.0 - Refactored sweeper code.
 * 1.3.0 - Added pusher code.
 * 1.2.1 - Slight bugfix in sweeper init.
 * 1.2.0 - Added thrower code.
 * 1.1.0 - Added sweeper code.
 * 1.0.1 - Fixed controller override bug.
 * 1.0.0 - First version.
*/
public class PacmanBotHardwareBase extends OpMode {
    final static public VersionNumber hwbVersion = new VersionNumber(1,5,0);

    final static double REAR_MULTIPLIER = 0.667;
    final static double COLOR_DETECTION_THRESHOLD = 0.25;

    final static double WINCH_RATE = 1.0;

    public enum ColorDetected {COLOR_RED,COLOR_BLUE,COLOR_NEITHER}

    DcMotorController.DeviceMode deviceMode;
    DcMotorController frontController;
    DcMotorController rearController;

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor rearLeft;
    DcMotor rearRight;

    DcMotor winch;
    DcMotor belt;

    Servo sweeper;
    Servo thrower;
    Servo release;

    Gamepad gamepad;
    int gamepadOverride=0;
    boolean gamepadAllower=true;

    DcMotor brush;

    ColorSensor eye;

    final static boolean LEFT = false;
    final static boolean RIGHT= true;

    double driveClamp=1.0;              //The forward drive value is clamped to this.
    double turnClamp=1.0;               //The turn value is clamped to this.
    double driveExponent=1.0;           //The forward drive is exponentially curved to this.
    double turnExponent=1.0;            //The turn is exponentially curved to this.
    double driveMultiplier=1.0;          //Then the forward drive value is scaled to this.
    double turnMultiplier=1.0;         //The turn value is scaled to this.
    double motorExponent=1.0;           //The final motor powers are exponentially curved to this.
    double finalRateMultiplier=1.0;     //The final motor powers are scaled to this.

    public void setDriveClamp(double value){driveClamp=value;}
    public void setTurnClamp(double value){turnClamp=value;}
    public void setDriveExponent(double value){driveExponent=value;}
    public void setTurnExponent(double value){turnExponent=value;}
    public void setDriveMultiplier(double value){driveMultiplier=value;}
    public void setTurnMultiplier(double value){turnMultiplier=value;}
    public void setMotorExponent(double value){motorExponent=value;}
    public void setFinalRateMultiplier(double value){finalRateMultiplier=value;}

    public void setBrush(double power){
        brush.setPower(power);
    }

    public ColorDetected getEyeColor(){
        int red = eye.red();
        int green = eye.green();
        int blue = eye.blue();

        if (Math.abs(red - blue)>COLOR_DETECTION_THRESHOLD) {
            if (red > blue) {
                return ColorDetected.COLOR_RED;
            }
            else {
                return ColorDetected.COLOR_BLUE;
            }
        }
        return ColorDetected.COLOR_NEITHER;
    }

    public String getColorString(ColorDetected color) {
        switch (color) {
            case COLOR_BLUE: return "Blue";
            case COLOR_RED: return "Red";
            case COLOR_NEITHER: return "Neither";
        }
        return "Error";
    }

    public void setEyeLED(boolean active)
    {
        eye.enableLed(active);

    }

    public void drive(double drive_rate, double turn_rate) {
        drive_rate = -drive_rate;
        drive_rate = limit(drive_rate,-driveClamp,driveClamp);
        turn_rate = limit(turn_rate, -turnClamp, turnClamp);
        drive_rate = exp(drive_rate, driveExponent);
        turn_rate = exp(turn_rate,turnExponent);
        drive_rate = drive_rate * driveMultiplier;
        turn_rate = turn_rate * turnMultiplier;
        double motorLeftPower = limit(drive_rate + turn_rate,-1,1);
        double motorRightPower = limit(drive_rate - turn_rate,-1,1);
        motorLeftPower = exp(motorLeftPower,motorExponent) * finalRateMultiplier;
        motorRightPower = exp(motorRightPower,motorExponent) * finalRateMultiplier;
        telemetry.addData("Left",motorLeftPower);
        telemetry.addData("Right",motorRightPower);
        frontLeft.setPower(motorLeftPower);
        frontRight.setPower(motorRightPower);
        rearLeft.setPower(motorLeftPower * REAR_MULTIPLIER);
        rearRight.setPower(motorRightPower * REAR_MULTIPLIER);
    }
    public double limit(double value,double min,double max){
        if (value>max) {return max;}
        if (value<min) {return min;}
        return value;
    }
    public double exp(double value, double exponent){
        boolean negative = (value<0);
        value = Math.abs(Math.pow(value,exponent));
        if (negative) {return -value;}
        return value;
    }

    public void stop_drive() {
        drive(0, 0);
    }

    public void setupHardware() {
        frontController = hardwareMap.dcMotorController.get("front_ctrl");
        rearController = hardwareMap.dcMotorController.get("rear_ctrl");
        frontLeft = hardwareMap.dcMotor.get("front_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        rearLeft = hardwareMap.dcMotor.get("rear_left");
        rearRight = hardwareMap.dcMotor.get("rear_right");
        brush = hardwareMap.dcMotor.get("brush");
        deviceMode = DcMotorController.DeviceMode.WRITE_ONLY;
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        rearRight.setDirection(DcMotor.Direction.REVERSE);
        brush.setDirection(DcMotor.Direction.FORWARD);
        brush.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        rearLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        rearRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        winch = hardwareMap.dcMotor.get("winch");
        belt = hardwareMap.dcMotor.get("belt");
        //hook.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        eye = hardwareMap.colorSensor.get("eye");
        setEyeLED(false);

        gamepad = new Gamepad();

        sweeper = hardwareMap.servo.get("sweeper");
        thrower = hardwareMap.servo.get("thrower");
        release = hardwareMap.servo.get("hook_release");
        thrower.setPosition(0.75);
        sweeper.setPosition(0.53);
        release.setPosition(0.53);
    }
    public void setThrower(boolean pos) {thrower.setPosition(pos ? 0.15 : 0.75);}

    public void setBelt(double power) {belt.setPower(power);}

    public void setSweeperPosition(boolean sweeperSide) {
        sweeper.setPosition(sweeperSide ? .35 : .05);
    }

    public void setWinch(double power) {

        winch.setPower(WINCH_RATE * power);
    }

    public void releaseHook() {
        release.setPosition(0);
    }

    public double threeWay(boolean a,boolean b) {
        if (a) return -1.0;
        if (b) return 1.0;
        return 0;
    }

    @Override
    public void init() {}
    @Override
    public void loop() {}

    public String getUser() {
        if (gamepadOverride < 2) return "Driver 1";
        return "Driver 2";
    }

    public void checkUsers() {
        if (gamepadAllower) {
            if (gamepad2.start) {
                gamepadOverride++;
                gamepadAllower=false;
            }
        }
        if (!gamepad2.start) gamepadAllower=true;
        if (gamepadOverride >= 2) copyGamepadState(gamepad2, gamepad);
        else copyGamepadState(gamepad1,gamepad);
    }

    public void copyGamepadState(Gamepad g1,Gamepad g2) {
        g2.right_stick_x = g1.right_stick_x;
        g2.right_stick_y = g1.right_stick_y;
        g2.left_stick_x  = g1.left_stick_x;
        g2.left_stick_y  = g1.left_stick_y;
        g2.right_stick_button = g1.right_stick_button;
        g2.left_stick_button = g1.left_stick_button;
        g2.left_bumper = g1.left_bumper;
        g2.right_bumper = g1.right_bumper;
        g2.left_trigger = g1.left_trigger;
        g2.right_trigger = g1.right_trigger;
        g2.a = g1.a;
        g2.b = g1.b;
        g2.x = g1.x;
        g2.y = g1.y;
        g2.dpad_left = g1.dpad_left;
        g2.dpad_right = g1.dpad_right;
        g2.dpad_up = g1.dpad_up;
        g2.dpad_down = g1.dpad_down;
    }
}
