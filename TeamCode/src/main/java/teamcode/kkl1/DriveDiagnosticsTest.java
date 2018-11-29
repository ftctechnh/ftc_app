package teamcode.kkl1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "DriveDiagnostics", group = "Linear OpMode")
public class DriveDiagnosticsTest extends LinearOpMode {

    private static final double TICKS_PER_INCH_COVERED = -44.5633840657;
    /**
     * In inches.
     */
    private static final double DISTANCE_BETWEEN_WHEELS = 16.0;
    private static final double STEER_TICKS_PER_RADIAN = DISTANCE_BETWEEN_WHEELS / 2 * TICKS_PER_INCH_COVERED;
    private static final double TURN_POWER = 0.25;

    @Override
    public void runOpMode() {
        waitForStart();
        KKL1HardwareManager.init(this);
        this.initialize();
        telemetry.addData("Status", "Now driving straight 4 feet at 50% power");
        telemetry.update();
        drive(48, 0.5);
        telemetry.addData("Status", "Now turning 360 degrees");
        telemetry.update();
        turn(2 * Math.PI);
    }


    private void initialize() {
        KKL1HardwareManager.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL1HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL1HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    private void drive(double inches, double power) {
        KKL1HardwareManager.lDriveMotor.setPower(0.0);
        KKL1HardwareManager.rDriveMotor.setPower(0.0);
        KKL1HardwareManager.lDriveMotor.setTargetPosition((int) (inches * TICKS_PER_INCH_COVERED));
        KKL1HardwareManager.rDriveMotor.setTargetPosition((int) (inches * TICKS_PER_INCH_COVERED));
        KKL1HardwareManager.lDriveMotor.setPower(power);
        KKL1HardwareManager.rDriveMotor.setPower(power);
        while (!motorsNearTarget()) ;
        resetEncoders();
    }

    private void turn(double radians) {
        int posL;
        int posR;
        if (radians > 0.0) {
            posL = (int) (2 * radians * STEER_TICKS_PER_RADIAN);
            posR = 0;
        } else if (radians < 0.0) {
            posL = 0;
            posR = (int) (2 * -radians * STEER_TICKS_PER_RADIAN);
        } else {
            posL = 0;
            posR = 0;
        }
        KKL1HardwareManager.lDriveMotor.setPower(0.0);
        KKL1HardwareManager.rDriveMotor.setPower(0.0);
        KKL1HardwareManager.lDriveMotor.setTargetPosition(posL);
        KKL1HardwareManager.rDriveMotor.setTargetPosition(posR);
        KKL1HardwareManager.lDriveMotor.setPower(TURN_POWER);
        KKL1HardwareManager.rDriveMotor.setPower(TURN_POWER);
        while (!motorsNearTarget()) ;
        resetEncoders();
    }

    private boolean motorsNearTarget() {
        int targetLDriveMotorPos = KKL1HardwareManager.lDriveMotor.getTargetPosition();
        int currentLDriveMotorPos = KKL1HardwareManager.lDriveMotor.getCurrentPosition();
        int targetRDriveMotorPos = KKL1HardwareManager.rDriveMotor.getTargetPosition();
        int currentRDriveMotorPos = KKL1HardwareManager.rDriveMotor.getCurrentPosition();
        telemetry.addData("targetL", targetLDriveMotorPos);
        telemetry.addData("currentL", currentLDriveMotorPos);
        telemetry.addData("targetR", targetRDriveMotorPos);
        telemetry.addData("currentR", currentRDriveMotorPos);
        telemetry.update();
        return Math.abs(currentLDriveMotorPos - targetLDriveMotorPos) < 10.0 && Math.abs(currentRDriveMotorPos - targetRDriveMotorPos) < 10.0;
    }

    private void resetEncoders() {
        KKL1HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL1HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


}
