package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 11/25/2017.
 **/

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class AutoDrive {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private REVGyro imu;
    private ModernRoboticsI2cRangeSensor rangeSensor;
    static private final double CIRCUMFERENCE_Of_WHEELS = 3.937 * Math.PI;
    static private final int CPR = 1120; //Clicks per rotation of the encoder with the NeveRest 40 motors. Please do not edit.
    public double heading;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    static final double MIN_MOVE_SPEED = 0.25;
    static final double MIN_SPIN_SPEED = 0.2;
    static final double MIN_STRAFE_SPEED = 0.35;
    static final double GYRO_OFFSET = 2.25;
    static final double SPIN_ON_BALANCE_BOARD_SPEED = 0.15;
    static final double DRIVE_OFF_BALANCE_BOARD_SPEED = 1; //TEMPORARY... SWITCH BACK TO 0.4 SOMETIME SOON
    static final double STRAFING_PAST_CRYPTOBOX_SPEED = 0.75;
    static final double SPIN_TO_CRYPTOBOX_SPEED = 1;
    static final double DRIVE_INTO_CRYPTOBOX_SPEED = 0.6;
    static final double DEFAULT_MOVING_TOWARDS_CRYPTOBOX_DISTANCE_FAR_POSITION = 14;
    static final double CRYPTOBOX_COLUMNS_OFFSET_RECOVERY = 7.5;
    static final double CRYPTOBOX_COLUMNS_OFFSET_FAR = 11;
    static final double BACK_AWAY_FROM_BLOCK_SPEED = 0.75;
    static final double DRIVE_TO_CYRPTOBOX_DISTANCE_FAR = 24;
    static final double SPIN_TO_CENTER_SPEED = 1;
    static final double DRIVE_EXPO = 3;
    static final double RAMP_LOG_EXPO = 0.8;
    static final double DISTANCE_TO_FAR_COLUMN = 32.75;
    static final double DISTANCE_TO_CENTER_COLUMN = 25.5;
    static final double DISTANCE_TO_CLOSE_COLUMN = 17.5;
    static final double DISTANCE_TO_CENTER = 36;

    public AutoDrive(HardwareMap hardwareMap, Telemetry telemetry) {
        this.FrontLeft = hardwareMap.dcMotor.get("m1");
        this.FrontRight = hardwareMap.dcMotor.get("m2");
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = hardwareMap.dcMotor.get("m3");
        this.RearRight = hardwareMap.dcMotor.get("m4");
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
        this.imu = new REVGyro(hardwareMap.get(BNO055IMU.class, "imu"));
        this.rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "d1");
        this.telemetry = telemetry;
        setBRAKE();
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

    public void backward(double speed, double distance) {
        driveTranslateRotate(0, -Math.abs(speed), 0, distance);
    }

    public void strafeRight(double speed, double distance) {
        driveTranslateRotate(Math.abs(speed), 0, 0, distance);
    }

    public void strafeLeft(double speed, double distance) {
        driveTranslateRotate(-Math.abs(speed), 0, 0, distance);
    }

    public void spinRight(double speed, double distance) {
        driveTranslateRotate(0, 0, Math.abs(speed), distance);
    }

    public void spinLeft(double speed, double distance) {
        driveTranslateRotate(0, 0, -Math.abs(speed), distance);
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

    private void driveSpeeds(double flSpeed, double frSpeed, double rlSpeed, double rrSpeed) {
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

    public void rightGyro(double x, double y, double z, double target) {
        double Adjustedtarget = target + GYRO_OFFSET;
        heading = getHeading();
        double derivative = 0;
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        driveSpeeds(fl, fr, rl, rr);
        if (heading < target) {
            while (derivative <= 0) {
                derivative = getHeading() - heading;
                heading = getHeading();
            }
        }
        double start = getHeading();
        double distance = Adjustedtarget - start;
        while (heading >= Adjustedtarget) {
            heading = getHeading();
            double proportion = 1 - (Math.abs((heading - start) / distance));
            driveSpeeds(clipSpinSpeed(fl * proportion), clipSpinSpeed(fr * proportion), clipSpinSpeed(rl * proportion), clipSpinSpeed(rr * proportion));
        }
        stopMotors();
    }

    public void rightGyro(double speed, double target) {
        rightGyro(0, 0, Math.abs(speed), target);
    }

    public void leftGyro(double x, double y, double z, double target) {
        double adjustedTarget = target - GYRO_OFFSET;
        heading = getHeading();
        double derivative = 0;
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        driveSpeeds(fl, fr, rl, rr);
        if (adjustedTarget < heading) {
            while (derivative >= 0) {
                derivative = getHeading() - heading;
                heading = getHeading();
            }
        }
        double start = heading;
        double distance = adjustedTarget - start;
        while (heading <= adjustedTarget) {
            heading = getHeading();
            double proportion = 1 - (Math.abs((heading - start) / distance));
            driveSpeeds(clipSpinSpeed(fl * proportion), clipSpinSpeed(fr * proportion), clipSpinSpeed(rl * proportion), clipSpinSpeed(rr * proportion));
        }
        stopMotors();
    }

    public void leftGyro(double speed, double target) {
        leftGyro(0, 0, -Math.abs(speed), target);
    }

    public void init() {
        imu.calibrate();
        heading = getHeading();
    }

    public double getHeading() {
        return imu.getHeading();
    }

    private void telemetrizeGyro() {
        telemetry.addData("Current heading: ", heading);
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

    private void telemetrizeDistance() {
        telemetry.addData("Distance", getDistance());
    }

    private void setBRAKE() {
        this.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

    private double calculateSpeedLinear(DcMotor motor, double targetClicks, double defaultSpeed) {
        if (defaultSpeed != 0) {
            return clipMoveSpeed(defaultSpeed * (targetClicks - Math.abs(motor.getCurrentPosition())) / targetClicks);
        } else {
            return 0;
        }
    }

    private double clipSpinSpeed(double speed) {
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

    private double clipStrafeSpeed(double speed) {
        if (speed > 0) {
            return Range.clip(speed, Math.abs(MIN_STRAFE_SPEED), 1);
        } else if (speed < 0) {
            return Range.clip(speed, -1, -Math.abs(MIN_STRAFE_SPEED));
        } else {
            return 0;
        }
    }

    public void driveUntilDistance(double x, double y, double z, double endDistance) {
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        driveSpeeds(fl, fr, rl, rr);
        double start = getDistance();
        double distanceToTravel = endDistance - start;
        double proportion;
        double derivative = 0;
        double distance = getDistance();
        int ranTimes = 0;
        int acceptedSensorValue = 0;
        while (distance < endDistance) {
            proportion = 1 - Math.abs((distance - start) / distanceToTravel + 0.0001);
            driveSpeeds(clipStrafeSpeed(fl * proportion), clipStrafeSpeed(fr * proportion), clipStrafeSpeed(rl * proportion), clipStrafeSpeed(rr * proportion));
            derivative = getDistance() - distance;
            if (derivative >= 0 && derivative < 6) {
                distance = getDistance();
                acceptedSensorValue++;
            }
            ranTimes++;
            telemetry.addData("Ran times", ranTimes);
            telemetry.addData("Accepted Ratio", acceptedSensorValue / ranTimes);
            telemetrizeDistance();
        }
        telemetry.update();
        stopMotors();
    }

    public void driveLeftUntilDistance(double speed, double distance) {
        driveUntilDistance(-Math.abs(speed), 0, 0, distance);
    }

    public void driveRightUntilDistance(double speed, double distance) {
        driveUntilDistance(Math.abs(speed), 0, 0, distance);
    }

    public double getDistance(DistanceUnit unit) {
        return rangeSensor.getDistance(unit);
    }

    public double getDistance() {
        return getDistance(DistanceUnit.INCH);
    }
}