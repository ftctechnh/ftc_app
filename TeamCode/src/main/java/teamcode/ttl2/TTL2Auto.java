package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import teamcode.examples.TensorFlowManager;

public abstract class TTL2Auto extends LinearOpMode {

    private static final double DRIVE_MOTOR_TICKS_PER_CENTIMETER_COVERED_VERTICAL = -36.3;
    private static final double DRIVE_MOTOR_TICKS_PER_CENTIMETER_COVERED_LATERAL = -45.4;
    private static final double DRIVE_MOTOR_TICKS_PER_RADIAN_COVERED = -1370.8;
    private static final int LIFT_MOTOR_TICKS_TO_LOWER = 575;
    private static final double LOWER_POWER = 0.25;
    private static final double TURN_POWER = 0.5;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;

    private TensorFlowManager tfManager;

    @Override
    public void runOpMode() {
        TTL2HardwareManager.initialize(this);
        this.tfManager = new TensorFlowManager(this.hardwareMap);
        this.tfManager.initialize();
        waitForStart();
        resetDriveEncoders();

        lowerRobot();
        driveLateral(10, 0.25);
        driveVertical(50, 0.5);
        turn(0.5 * Math.PI);
        driveVertical(100,1.0);
        turn(0.25 * Math.PI);
        driveVertical(300,1.0);

        //this.run();
    }

    protected void lowerRobot() {
//        TTL2HardwareManager.liftMotorL.setDirection(DcMotorSimple.Direction.REVERSE);
//        TTL2HardwareManager.liftMotorR.setDirection(DcMotorSimple.Direction.REVERSE);

        TTL2HardwareManager.liftMotorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.liftMotorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL2HardwareManager.liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        TTL2HardwareManager.liftMotorL.setTargetPosition(LIFT_MOTOR_TICKS_TO_LOWER);
        TTL2HardwareManager.liftMotorR.setTargetPosition(LIFT_MOTOR_TICKS_TO_LOWER);

        TTL2HardwareManager.liftMotorL.setPower(LOWER_POWER);
        TTL2HardwareManager.liftMotorR.setPower(LOWER_POWER);

        while (opModeIsActive() && !liftMotorsNearTarget()) ;

        telemetry.addData("status", "arm lowered");
        telemetry.update();

        TTL2HardwareManager.liftMotorL.setPower(0.0);
        TTL2HardwareManager.liftMotorR.setPower(0.0);
    }


    protected void driveVertical(double centimeters, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (centimeters * DRIVE_MOTOR_TICKS_PER_CENTIMETER_COVERED_VERTICAL);

        TTL2HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        TTL2HardwareManager.frontRightDrive.setTargetPosition(ticks);
        TTL2HardwareManager.backLeftDrive.setTargetPosition(ticks);
        TTL2HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL2HardwareManager.frontLeftDrive.setPower(power);
        TTL2HardwareManager.frontRightDrive.setPower(power);
        TTL2HardwareManager.backLeftDrive.setPower(power);
        TTL2HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) {
            telemetry.addData("ticks", ticks);
            telemetry.addData("frontLeft", "%s %s", TTL2HardwareManager.frontLeftDrive.getCurrentPosition(),
                    TTL2HardwareManager.frontLeftDrive.getDirection());
            telemetry.addData("backLeft", "%s %s", TTL2HardwareManager.backLeftDrive.getCurrentPosition(),
                    TTL2HardwareManager.backLeftDrive.getDirection());
            telemetry.addData("frontRight", "%s %s", TTL2HardwareManager.frontRightDrive.getCurrentPosition(),
                    TTL2HardwareManager.frontRightDrive.getDirection());
            telemetry.addData("backRight", "%s %s", TTL2HardwareManager.backRightDrive.getCurrentPosition(),
                    TTL2HardwareManager.backRightDrive.getDirection());
            telemetry.update();
        }
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    protected void driveLateral(double centimeters, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (centimeters * DRIVE_MOTOR_TICKS_PER_CENTIMETER_COVERED_LATERAL);

        TTL2HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        TTL2HardwareManager.frontRightDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL2HardwareManager.frontLeftDrive.setPower(power);
        TTL2HardwareManager.frontRightDrive.setPower(power);
        TTL2HardwareManager.backLeftDrive.setPower(power);
        TTL2HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    protected void turn(double radians) {
        zeroDriveMotorPower();
        int ticks = (int) (radians * DRIVE_MOTOR_TICKS_PER_RADIAN_COVERED);

        TTL2HardwareManager.frontLeftDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.frontRightDrive.setTargetPosition(ticks);
        TTL2HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL2HardwareManager.frontLeftDrive.setPower(TURN_POWER);
        TTL2HardwareManager.frontRightDrive.setPower(TURN_POWER);
        TTL2HardwareManager.backLeftDrive.setPower(TURN_POWER);
        TTL2HardwareManager.backRightDrive.setPower(TURN_POWER);

        while (opModeIsActive() && !driveMotorsNearTarget()) ;
        resetDriveEncoders();
        zeroDriveMotorPower();
    }

    private void resetDriveEncoders() {
        TTL2HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL2HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void zeroDriveMotorPower() {
        TTL2HardwareManager.frontLeftDrive.setPower(0.0);
        TTL2HardwareManager.frontRightDrive.setPower(0.0);
        TTL2HardwareManager.backLeftDrive.setPower(0.0);
        TTL2HardwareManager.backRightDrive.setPower(0.0);
    }

    private boolean driveMotorsNearTarget() {
        int targetFrontLeftDriveMotorPos = TTL2HardwareManager.frontLeftDrive.getTargetPosition();
        int targetFrontRightDriveMotorPos = TTL2HardwareManager.frontRightDrive.getTargetPosition();
        int targetBackLeftDriveMotorPos = TTL2HardwareManager.backLeftDrive.getTargetPosition();
        int targetBackRightDriveMotorPos = TTL2HardwareManager.backRightDrive.getTargetPosition();


        int currentFrontLeftDriveMotorPos = TTL2HardwareManager.frontLeftDrive.getCurrentPosition();
        int currentFrontRightDriveMotorPos = TTL2HardwareManager.frontRightDrive.getCurrentPosition();
        int currentBackLeftDriveMotorPos = TTL2HardwareManager.backLeftDrive.getCurrentPosition();
        int currentBackRightDriveMotorPos = TTL2HardwareManager.backRightDrive.getCurrentPosition();

        return Math.abs(currentFrontLeftDriveMotorPos - targetFrontLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentFrontRightDriveMotorPos - targetFrontRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackLeftDriveMotorPos - targetBackLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackRightDriveMotorPos - targetBackRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    private boolean liftMotorsNearTarget() {
        int targetLeftLiftMotorPos = TTL2HardwareManager.liftMotorL.getTargetPosition();
        int targetRightLiftMotorPos = TTL2HardwareManager.liftMotorR.getTargetPosition();

        int currentLeftLiftMotorPos = TTL2HardwareManager.liftMotorL.getCurrentPosition();
        int currentRightLiftMotorPos = TTL2HardwareManager.liftMotorR.getCurrentPosition();

        return Math.abs(currentLeftLiftMotorPos - targetLeftLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentRightLiftMotorPos - targetRightLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    private double getCentimetersFromPixel(float height) {
        return ((-0.25 * height) + 52.5) * 2.54;
    }
}
