package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.HashMap;
import java.util.Locale;

public class DriveTrain {

    private LinearOpMode opMode;
    private Motor lfDrive, rfDrive, lbDrive, rbDrive;
    private Motors driveMotors;
    private IMU imu;
    private Logger l;

    // PID Values (kp, ki, kd, minError)
    private static final HashMap<String, Double> PID_DRIVE = new HashMap<String, Double>() {{
        put("kp", 1.0 / (18 * Values.TICKS_PER_INCH_FORWARD));
        put("ki", 0.0);
        put("kd", 0.0);
        put("minError", Values.TICKS_PER_INCH_FORWARD * 0.5);
    }};
    private static final HashMap<String, Double> PID_ROTATE = new HashMap<String, Double>() {{
        put("kp", 1.0 / 90.0);
        put("ki", 0.0);
        put("kd", 0.0);
        put("minError", 2.0);
        put("minPower", 0.2);
    }};
    private static final HashMap<String, Double> PID_STRAFE = new HashMap<String, Double>() {{
        put("kp", 1.0 / (6 * Values.TICKS_PER_INCH_STRAFE));
        put("ki", 0.0);
        put("kd", 0.0);
        put("minError", Values.TICKS_PER_INCH_STRAFE * 0.5);
    }};

    public DriveTrain(LinearOpMode opMode) {
        this.opMode = opMode;
        initMotors();
        initIMU();
        this.l = new Logger("Drive Train");
    }

    private void initMotors() {
        l.log("Initializing motors...");

        DcMotor lf = opMode.hardwareMap.dcMotor.get("lfDrive");
        DcMotor rf = opMode.hardwareMap.dcMotor.get("rfDrive");
        DcMotor lb = opMode.hardwareMap.dcMotor.get("lbDrive");
        DcMotor rb = opMode.hardwareMap.dcMotor.get("rbDrive");

        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        rb.setDirection(DcMotorSimple.Direction.FORWARD);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        rf.setDirection(DcMotorSimple.Direction.FORWARD);

        lfDrive = new Motor(lf, this.opMode);
        rfDrive = new Motor(rf, this.opMode);
        lbDrive = new Motor(lb, this.opMode);
        rbDrive = new Motor(rb, this.opMode);

        this.driveMotors = new Motors(new Motor[]{lfDrive, rfDrive, lbDrive, rbDrive});

        l.log("Motors initialized!");
    }

    private void resetMotors() {
        l.log("Motors reset");
        driveMotors.resetEncoders();
        driveMotors.useEncoders();
    }

    private void initIMU() {
        l.log("Initializing IMU...");
        BNO055IMU adaImu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu = new IMU(adaImu);
        l.log("IMU Initialized!");
    }

    private double fixAngle(double angle) {
        while (angle > 180 || angle < -180) {
            if (angle > 180) {
                angle -= 360;
            } else {
                angle += 360;
            }
        }
        return angle;
    }

    public IMU getImu() {
        return imu;
    }

    public void setPowers(double lf, double rf, double lb, double rb) {
        driveMotors.setPowers(lf, rf, lb, rb);
        l.logData("lfPower", lf);
        l.logData("rfPower", rf);
        l.logData("lbPower", lb);
        l.logData("rbPower", rb);
    }

    public void stopAll() {
        setPowers(0, 0, 0, 0);
        l.log("Motors stopped!");
    }

    public void move(Direction direction, double power) {
        l.log("Moving " + direction.toString());
        switch(direction) {
            case FORWARD:
                setPowers(power,power,
                        power,power);
                break;
            case BACK:
                setPowers(-power,-power,
                        -power,-power);
                break;
            case RIGHT:
                setPowers(-power,power,
                        power,-power);
                break;
            case LEFT:
                setPowers(power,-power,
                        -power,power);
                break;
            case CW:
                setPowers(power,-power,
                        power,-power);
                break;
            case CCW:
                setPowers(-power,power,
                        -power,power);
                break;
            default:
                stopAll();
        }
    }

    public void drive(Direction direction, double inches, double timeoutS) {
        l.log(String.format(Locale.getDefault(), "Started driving %s %f inches",
                direction.toString(), inches));
        l.logData("Kp", PID_DRIVE.get("kp"));
        l.logData("Ki", PID_DRIVE.get("ki"));
        l.logData("Kd", PID_DRIVE.get("kd"));
        l.logData("minError", PID_DRIVE.get("minError"));

        resetMotors();

        driveMotors.addPID(PID_DRIVE.get("kp"), PID_DRIVE.get("ki"),
                PID_DRIVE.get("kd"), PID_DRIVE.get("minError"));

        int dir = direction.getValue();
        driveMotors.setAllTargets(dir * (int) (inches * Values.TICKS_PER_INCH_FORWARD));

        double startTime = System.currentTimeMillis();
        do {
            driveMotors.updateErrors();

            double[] errors = driveMotors.getErrors();
            l.logData("lfError", errors[0]);
            l.logData("rfError", errors[1]);
            l.logData("lbError", errors[2]);
            l.logData("rbError", errors[3]);

            driveMotors.setPowers(driveMotors.getPIDOutput());
        } while (opMode.opModeIsActive() && !opMode.isStopRequested() &&
                !driveMotors.withinMinError() &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS);

        stopAll();

        l.log("Finished driving!");
    }

    public void strafe(Direction direction, double inches, double timeoutS) {
        l.log("Strafing " + Double.toString(inches) + " inches...");
        l.logData("Kp", PID_STRAFE.get("kp"));
        l.logData("Ki", PID_STRAFE.get("ki"));
        l.logData("Kd", PID_STRAFE.get("kd"));
        l.logData("minError", PID_STRAFE.get("minError"));

        resetMotors();

        driveMotors.addPID(PID_STRAFE.get("kp"), PID_STRAFE.get("ki"),
                PID_DRIVE.get("kd"), PID_DRIVE.get("minError"));

        int dir = direction.getValue();
        int val = (int) (inches * Values.TICKS_PER_INCH_STRAFE);
        driveMotors.setTargets(
                -dir * val, dir * val,
                dir * val, -dir * val
        );

        double startTime = System.currentTimeMillis();
        do {
            driveMotors.updateErrors();

            double[] errors = driveMotors.getErrors();
            l.logData("lfError", errors[0]);
            l.logData("rfError", errors[1]);
            l.logData("lbError", errors[2]);
            l.logData("rbError", errors[3]);

            double[] drivePowers = driveMotors.getPIDOutput();

            driveMotors.setPowers(drivePowers);
        } while (opMode.opModeIsActive() && !opMode.isStopRequested() &&
                !driveMotors.withinMinError() &&
                (System.currentTimeMillis() - startTime) / 1000 < timeoutS);

        stopAll();

        l.log("Finished strafing!");
    }

    public void rotate(Direction direction, double angle, double timeoutS) {
        l.log("Rotating " + Double.toString(angle) + " degrees...");

        int dir = direction.getValue();

        double currentHeading = imu.getAngle();
        double targetHeading = fixAngle(currentHeading + dir * angle);

        rotateTo(targetHeading, timeoutS);
    }

    public void rotateTo(double targetHeading, double timeoutS) {
        double minError = PID_ROTATE.get("minError");
        double minPower = PID_ROTATE.get("minPower");

        l.log("Started rotating...");
        l.logData("targetHeading", targetHeading);
        l.logData("Kp", PID_ROTATE.get("kp"));
        l.logData("Ki", PID_ROTATE.get("ki"));
        l.logData("Kd", PID_ROTATE.get("kd"));
        l.logData("minError", minError);

        PID pid = new PID(PID_ROTATE.get("kp"), PID_ROTATE.get("ki"),
                PID_ROTATE.get("kd"), opMode.telemetry);

        double currentHeading;
        double error;

        double startTime = System.currentTimeMillis();
        do {
            currentHeading = imu.getAngle();
            error = fixAngle(targetHeading - currentHeading);

            l.logData("currentHeading", currentHeading);
            l.logData("error", error);

            double proportionalPower = pid.getOutput(error);
            proportionalPower += (proportionalPower > 0) ? minPower : -minPower;
            move(Direction.CW, proportionalPower);

            if (Math.abs(error) < minError) {
                l.log("Within minError! Waiting...");
                stopAll();
                Utils.waitFor(300);
                currentHeading = imu.getAngle();
                error = fixAngle(targetHeading - currentHeading);
            }
        } while (opMode.opModeIsActive() && !opMode.isStopRequested() &&
                Math.abs(error) > minError
                && (System.currentTimeMillis() - startTime) / 1000 < timeoutS);

        stopAll();

        l.log("Done rotating!");
    }

}
