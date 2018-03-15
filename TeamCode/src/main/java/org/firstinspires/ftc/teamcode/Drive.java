package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Kaden on 3/15/18.
 */

public class Drive {
    public static final double MULTI_GLYPH_STRAFE_SPEED = 0.7;
    public static final double DRIVE_INTO_GLYPH_PIT_SPEED = 1;
    public static final double DRIVE_INTO_GLYPH_PIT_DISTANCE = 19;
    public static final double DRIVE_INTO_GLYPHS_SPEED = 0.4;
    public static final double DRIVE_INTO_GLYPHS_DISTANCE = 4;
    static private final double CIRCUMFERENCE_Of_WHEELS = 3.937 * Math.PI;
    static private final int CPR = 1120; //Clicks per rotation of the encoder with the NeveRest 40 motors. Please do not edit.
    static final double MIN_MOVE_SPEED = 0.25;
    public static final double MAX_SPEED = 1;
    static final double MIN_SPIN_SPEED = 0.2;
    static final double MIN_STRAFE_SPEED = 0.35;
    static final double SPIN_ON_BALANCE_BOARD_SPEED = 0.15;
    static final double DRIVE_OFF_BALANCE_BOARD_SPEED = 1; //TEMPORARY... SWITCH BACK TO 0.4 SOMETIME SOON
    static final double STRAFING_PAST_CRYPTOBOX_SPEED = 0.75;
    static final double SPIN_TO_CRYPTOBOX_SPEED = 1;
    static final double DRIVE_INTO_CRYPTOBOX_SPEED = 0.8;
    static final double DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION = 14;
    static final double CRYPTOBOX_COLUMNS_OFFSET_RECOVERY = 7.5;
    static final double CRYPTOBOX_COLUMNS_OFFSET_FAR = 11;
    static final double BACK_AWAY_FROM_BLOCK_SPEED = 1;
    static final double DRIVE_TO_CYRPTOBOX_DISTANCE_FAR = 24;
    static final double SPIN_TO_CENTER_SPEED = 1;
    static final double RAMP_LOG_EXPO = 0.8;
    static final double DISTANCE_TO_FAR_COLUMN = 32.75;
    static final double DISTANCE_TO_CENTER_COLUMN = 25.5;
    static final double DISTANCE_TO_CLOSE_COLUMN = 17.5;
    static final double D_PAD_SLOW_SPEED = 0.25;
    static final double BUMPER_SLOW_SPEED = 0.25;


    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public Drive(OpMode opMode) {
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;
        this.FrontLeft = hardwareMap.dcMotor.get("m1");
        this.FrontRight = hardwareMap.dcMotor.get("m2");
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = hardwareMap.dcMotor.get("m3");
        this.RearRight = hardwareMap.dcMotor.get("m4");
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void setBRAKE() {
        this.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setFLOAT() {
        this.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void driveTranslateRotate(double x, double y, double z, double distance) {
        resetEncoders();
        double clicks = Math.abs(distance * CPR / CIRCUMFERENCE_Of_WHEELS);
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        double highestSpeed = findHigh(new double[]{fl, fr, rl, rr});
        double flTarget = Math.abs(fl / highestSpeed * clicks);
        double frTarget = Math.abs(fr / highestSpeed * clicks);
        double rlTarget = Math.abs(rl / highestSpeed * clicks);
        double rrTarget = Math.abs(rr / highestSpeed * clicks);
        driveSpeeds(fl, fr, rl, rr);
        ElapsedTime time = new ElapsedTime();
        time.reset();
        while (!(isMotorAtTarget(FrontLeft, flTarget)) && (!(isMotorAtTarget(FrontRight, frTarget))) && (!(isMotorAtTarget(RearLeft, rlTarget))) && (!(isMotorAtTarget(RearRight, rrTarget)))) {
            if (time.seconds() > 3 * clicks / 420 / highestSpeed) {
                telemetry.addLine("It timed out...");
                telemetry.update();
            }
            driveSpeeds(calculateSpeedLog(FrontLeft, flTarget, fl), calculateSpeedLog(FrontRight, frTarget, fr), calculateSpeedLog(RearLeft, rlTarget, rl), calculateSpeedLog(RearRight, rrTarget, rr));
        }
        stopMotors();
    }

    public void driveTranslateRotate(double x, double y, double z, long miliseconds) {
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();
        while (runtime.milliseconds() < miliseconds) {
            driveSpeeds(fl, fr, rl, rr);
        }
        stopMotors();
    }

    public void driveTranslateRotate(double x, double y, double z) {
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        driveSpeeds(fl, fr, rl, rr);
    }

    public void forward(double speed, double distance) {
        driveTranslateRotate(0, Math.abs(speed), 0, distance);
    }

    public void forward(double speed) {
        driveTranslateRotate(0, Math.abs(speed), 0);
    }

    public void backward(double speed, double distance) {
        driveTranslateRotate(0, -Math.abs(speed), 0, distance);
    }

    public void backward(double speed) {
        driveTranslateRotate(0, -Math.abs(speed), 0);
    }

    public void strafeRight(double speed, double distance) {
        driveTranslateRotate(Math.abs(speed), 0, 0, distance);
    }

    public void strafeRight(double speed) {
        driveTranslateRotate(Math.abs(speed), 0, 0);
    }

    public void strafeLeft(double speed, double distance) {
        driveTranslateRotate(-Math.abs(speed), 0, 0, distance);
    }

    public void strafeLeft(double speed) {
        driveTranslateRotate(-Math.abs(speed), 0, 0);
    }

    public void spinRight(double speed, double distance) {
        driveTranslateRotate(0, 0, Math.abs(speed), distance);
    }

    public void spinRight(double speed) {
        driveTranslateRotate(0, 0, Math.abs(speed));
    }

    public void spinLeft(double speed, double distance) {
        driveTranslateRotate(0, 0, -Math.abs(speed), distance);
    }

    public void spinLeft(double speed) {
        driveTranslateRotate(0, 0, -Math.abs(speed));
    }

    public void forwardTime(double speed, long miliseconds) {
        driveTranslateRotate(0, Math.abs(speed), 0, miliseconds);
    }

    public void backwardTime(double speed, long miliseconds) {
        driveTranslateRotate(0, -Math.abs(speed), 0, miliseconds);
    }

    public void strafeRightTime(double speed, long miliseconds) {
        driveTranslateRotate(Math.abs(speed), 0, 0, miliseconds);
    }

    public void strafeLeftTime(double speed, long miliseconds) {
        driveTranslateRotate(-Math.abs(speed), 0, 0, miliseconds);
    }

    public void spinRightTime(double speed, long miliseconds) {
        driveTranslateRotate(0, 0, Math.abs(speed), miliseconds);
    }

    public void spinLeftTime(double speed, long miliseconds) {
        driveTranslateRotate(0, 0, -Math.abs(speed), miliseconds);
    }

    public void driveSpeeds(double flSpeed, double frSpeed, double rlSpeed, double rrSpeed) {
        FrontLeft.setPower(clip(flSpeed));
        FrontRight.setPower(clip(frSpeed));
        RearLeft.setPower(clip(rlSpeed));
        RearRight.setPower(clip(rrSpeed));
    }

    private double clip(double value) {
        return Range.clip(value, -1, 1);
    }

    private double findHigh(double[] nums) {
        double high = 0;
        for (int i = 0; i < nums.length; i++) {
            if (Math.abs(nums[i]) > high) {
                high = Math.abs(nums[i]);
            }
        }
        return high;
    }

    private void resetEncoders() {
        FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void stopMotors() {
        driveSpeeds(0, 0, 0, 0);
    }

    private int getCurrentPosition(DcMotor motor) {
        return motor.getCurrentPosition();
    }

    private boolean isMotorAtTarget(DcMotor motor, double target) {
        return Math.abs(getCurrentPosition(motor)) >= Math.abs(target);
    }

    public void stop() {
        driveSpeeds(0,0,0,0);
    }

    private void telemetrizeEncoders() {
        telemetry.addData("First motor: ", FrontLeft.getCurrentPosition());
        telemetry.addData("Second motor: ", FrontRight.getCurrentPosition());
        telemetry.addData("third motor: ", RearLeft.getCurrentPosition());
        telemetry.addData("fourth motor: ", RearRight.getCurrentPosition());
    }

    private void telemetrizeSpeeds() {
        telemetry.addLine("Motor speeds:");
        telemetry.addData("Front left motor", FrontLeft.getPower());
        telemetry.addData("Front right motor", FrontRight.getPower());
        telemetry.addData("Back left motor", RearLeft.getPower());
        telemetry.addData("Back right motor", RearRight.getPower());
    }

    private double calculateSpeedLog(DcMotor motor, double targetClicks, double defaultSpeed) {
        double speed;
        double k = 4 / targetClicks;
        double currentPosition = Math.abs(motor.getCurrentPosition());
        if (defaultSpeed != 0) {
            speed = k * currentPosition * (1 - currentPosition / targetClicks) * defaultSpeed;
        } else {
            return 0;
        }
        double expo_speed = Math.pow(Math.abs(clipMoveSpeed(speed)), RAMP_LOG_EXPO);
        if (speed > 0) {
            return expo_speed;
        } else {
            return -expo_speed;
        }
    }

    public double clipSpinSpeed(double speed) {
        if (speed > 0) {
            return Range.clip(speed, MIN_SPIN_SPEED, 1);
        } else if (speed < 0) {
            return Range.clip(speed, -1, -MIN_SPIN_SPEED);
        } else {
            return 0;
        }
    }

    private double clipMoveSpeed(double speed) {
        if (speed > 0) {
            return Range.clip(speed, MIN_MOVE_SPEED, 1);
        } else if (speed < 0) {
            return Range.clip(speed, -1, -MIN_MOVE_SPEED);
        } else {
            return 0;
        }
    }

    public double clipStrafeSpeed(double speed) {
        if (speed > 0) {
            return Range.clip(speed, Math.abs(MIN_STRAFE_SPEED), 1);
        } else if (speed < 0) {
            return Range.clip(speed, -1, -Math.abs(MIN_STRAFE_SPEED));
        } else {
            return 0;
        }
    }

    public void driveLeftRight(double xLeft, double xRight, double yLeft, double yRight) {
        //This one is kinda complicated for using in an autonomous program or any linear set of commands. I'd recommend using driveTranslateRotate for things other than driving. However I will explain this one anyway.
        //There are two forces for each side (left/right) of the robot. y controls forward/backward, x controls side-to-side (strafing). For instance if both ys are positive it will move forward. If both xs are positive it will move right.
        driveSpeeds(yLeft - xLeft, yRight + xRight, yLeft + xLeft, yRight - xRight);
    }

    public void swingRight() {
        driveSpeeds(0, 0, -1, 1);
    }

    public void swingLeft() {
        driveSpeeds(0, 0, 1, -1);
    }

}
