package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "KKL2Auto", group = "Linear OpMode")
public class KKL2Auto extends LinearOpMode {

    private static final double DRIVE_TICKS_PER_CENTIMETER_COVERED = -55.0;
    private static final double TURN_TICKS_PER_RADIAN_COVERED = 1066.15135303;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;
    private static final double TURN_POWER = 0.5;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        waitForStart();
        resetDriveEncoders();

        turn(Math.PI);

    }

    private void drive(double centimeters, double power) {
        zeroDriveMotorPower();

        KKL2HardwareManager.driveLMotor.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        KKL2HardwareManager.driveRMotor.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));

        KKL2HardwareManager.driveLMotor.setPower(power);
        KKL2HardwareManager.driveRMotor.setPower(power);

        while (opModeIsActive() && !motorsNearTarget()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    private void turn(double radians) {
        zeroDriveMotorPower();

        int posL = (int) (radians * -TURN_TICKS_PER_RADIAN_COVERED);
        int posR = (int) (radians * TURN_TICKS_PER_RADIAN_COVERED);

        KKL2HardwareManager.driveLMotor.setTargetPosition(posL);
        KKL2HardwareManager.driveRMotor.setTargetPosition(posR);
        KKL2HardwareManager.driveLMotor.setPower(TURN_POWER);
        KKL2HardwareManager.driveRMotor.setPower(TURN_POWER);

        while (opModeIsActive() && !motorsNearTarget()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    private void resetDriveEncoders() {
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void zeroDriveMotorPower() {
        KKL2HardwareManager.driveLMotor.setPower(0.0);
        KKL2HardwareManager.driveRMotor.setPower(0.0);
    }

    private boolean motorsNearTarget() {
        int targetDriveLMotorPos = KKL2HardwareManager.driveLMotor.getTargetPosition();
        int targetDriveRMotorPos = KKL2HardwareManager.driveRMotor.getTargetPosition();

        int currentDriveLMotorPos = KKL2HardwareManager.driveLMotor.getCurrentPosition();
        int currentDriveRMotorPos = KKL2HardwareManager.driveRMotor.getCurrentPosition();

        boolean nearTarget = Math.abs(currentDriveLMotorPos - targetDriveLMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentDriveRMotorPos - targetDriveRMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;


        telemetry.addData("rightTarget", targetDriveRMotorPos);
        telemetry.addData("rightCurrent", currentDriveRMotorPos);
        telemetry.addData("leftTarget", targetDriveLMotorPos);
        telemetry.addData("leftCurrent", currentDriveLMotorPos);

        telemetry.update();

        return nearTarget;
    }

}
