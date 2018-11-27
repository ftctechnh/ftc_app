package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import teamcode.examples.Mineral;
import teamcode.examples.TensorFlowManager;

@Autonomous(name = "TTL2Auto", group = "Linear OpMode")
public class TTL2Auto extends LinearOpMode {

    private static final double DRIVE_TICKS_PER_CENTIMETER_COVERED = -100.0;
    private static final double TURN_TICKS_PER_RADIAN_COVERED = 1066.15135303;
    private static final double TURN_POWER = 0.5;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;

    private TensorFlowManager tfManager;

    @Override
    public void runOpMode() {
        TTL2HardwareManager.initialize(this);
        //  this.tfManager = new TensorFlowManager(this.hardwareMap);
        // this.tfManager.initialize();
        waitForStart();
        resetDriveEncoders();

        driveVertical(25.0, 1.0);

        while (opModeIsActive()) {

//            List<Mineral> minerals = this.tfManager.getRecognizedMinerals();
//            Mineral gold = null;
//
//            if (minerals != null) {
//                for (Mineral mineral : minerals) {
//                    if (mineral.isGold()) {
//                        // update the gold mineral data
//                        gold = mineral;
//                        break;
//                    }
//                }
//            }
//
//            if (gold != null) {
//                // drive towards the gold
//
//            }
        }

    }


    private void driveVertical(double centimeters, double power) {
        zeroDriveMotorPower();

        TTL2HardwareManager.frontLeftDrive.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        TTL2HardwareManager.frontRightDrive.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        TTL2HardwareManager.backLeftDrive.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        TTL2HardwareManager.backRightDrive.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));

        TTL2HardwareManager.frontLeftDrive.setPower(power);
        TTL2HardwareManager.frontRightDrive.setPower(power);
        TTL2HardwareManager.backLeftDrive.setPower(power);
        TTL2HardwareManager.backRightDrive.setPower(power);

        while (!motorsNearTarget() && opModeIsActive()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    private void driveLateral(double centimeters, double power) {
        zeroDriveMotorPower();

        TTL2HardwareManager.frontLeftDrive.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        TTL2HardwareManager.frontRightDrive.setTargetPosition((int) (centimeters * -DRIVE_TICKS_PER_CENTIMETER_COVERED));
        TTL2HardwareManager.backLeftDrive.setTargetPosition((int) (centimeters * -DRIVE_TICKS_PER_CENTIMETER_COVERED));
        TTL2HardwareManager.backRightDrive.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));

        TTL2HardwareManager.frontLeftDrive.setPower(power);
        TTL2HardwareManager.frontRightDrive.setPower(power);
        TTL2HardwareManager.backLeftDrive.setPower(power);
        TTL2HardwareManager.backRightDrive.setPower(power);

        while (!motorsNearTarget() && opModeIsActive()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    private void turn(double radians) {
        zeroDriveMotorPower();

        while (opModeIsActive() && !motorsNearTarget()) ;
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

    private boolean motorsNearTarget() {
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

}
