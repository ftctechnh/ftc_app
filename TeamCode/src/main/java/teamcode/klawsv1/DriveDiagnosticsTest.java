package teamcode.klawsv1;

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
        HardwareManager.init(this);
        this.initialize();
        telemetry.addData("Status", "Now driving straight 4 feet at 50% power");
        telemetry.update();
        drive(48, 0.5);
        telemetry.addData("Status", "Now turning 360 degrees");
        telemetry.update();
        turn(2 * Math.PI);
    }


    private void initialize() {
        HardwareManager.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    private void drive(double inches, double power) {
        HardwareManager.lDriveMotor.setPower(0.0);
        HardwareManager.rDriveMotor.setPower(0.0);
        HardwareManager.lDriveMotor.setTargetPosition((int) (inches * TICKS_PER_INCH_COVERED));
        HardwareManager.rDriveMotor.setTargetPosition((int) (inches * TICKS_PER_INCH_COVERED));
        HardwareManager.lDriveMotor.setPower(power);
        HardwareManager.rDriveMotor.setPower(power);
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
        HardwareManager.lDriveMotor.setPower(0.0);
        HardwareManager.rDriveMotor.setPower(0.0);
        HardwareManager.lDriveMotor.setTargetPosition(posL);
        HardwareManager.rDriveMotor.setTargetPosition(posR);
        HardwareManager.lDriveMotor.setPower(TURN_POWER);
        HardwareManager.rDriveMotor.setPower(TURN_POWER);
        while (!motorsNearTarget()) ;
        resetEncoders();
    }

    private boolean motorsNearTarget() {
        int targetLDriveMotorPos = HardwareManager.lDriveMotor.getTargetPosition();
        int currentLDriveMotorPos = HardwareManager.lDriveMotor.getCurrentPosition();
        int targetRDriveMotorPos = HardwareManager.rDriveMotor.getTargetPosition();
        int currentRDriveMotorPos = HardwareManager.rDriveMotor.getCurrentPosition();
        telemetry.addData("targetL", targetLDriveMotorPos);
        telemetry.addData("currentL", currentLDriveMotorPos);
        telemetry.addData("targetR", targetRDriveMotorPos);
        telemetry.addData("currentR", currentRDriveMotorPos);
        telemetry.update();
        return Math.abs(currentLDriveMotorPos - targetLDriveMotorPos) < 10.0 && Math.abs(currentRDriveMotorPos - targetRDriveMotorPos) < 10.0;
    }

    private void resetEncoders() {
        HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


}
