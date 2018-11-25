package teamcode.kkl1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "KobaltKlawsAutonomousCraterSide", group = "Linear OpMode")
public class KobaltKlawsAutonomousCraterSide extends LinearOpMode {

    private static final double TICKS_PER_INCH_COVERED = -44.5633840657;
    /**
     * In inches.
     */
    private static final double DISTANCE_BETWEEN_WHEELS = 16.0;
    private static final double STEER_TICKS_PER_RADIAN = DISTANCE_BETWEEN_WHEELS / 4 * TICKS_PER_INCH_COVERED;
    private static final double TURN_POWER = 0.25;

    @Override
    public void runOpMode() {
        waitForStart();
        HardwareManager.init(this);
        this.initialize();
        HardwareManager.liftMotor.setTargetPosition(-500);
        HardwareManager.liftMotor.setPower(1.0);
        sleep(800);
        HardwareManager.liftMotor.setPower(0.0);
        sleep(1000);
        drive(40, 1.0);
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
        HardwareManager.lDriveMotor.setPower(0.0);
        HardwareManager.rDriveMotor.setPower(0.0);
        resetEncoders();
    }

    private void turn(double radians) {
        int posL = (int) (2 * radians * STEER_TICKS_PER_RADIAN);
        int posR = (int) (-2 * radians * STEER_TICKS_PER_RADIAN);
        HardwareManager.lDriveMotor.setPower(0.0);
        HardwareManager.rDriveMotor.setPower(0.0);
        HardwareManager.lDriveMotor.setTargetPosition(posL);
        HardwareManager.rDriveMotor.setTargetPosition(posR);
        HardwareManager.lDriveMotor.setPower(TURN_POWER);
        HardwareManager.rDriveMotor.setPower(TURN_POWER);
        while (!motorsNearTarget()) ;
        HardwareManager.lDriveMotor.setPower(0.0);
        HardwareManager.rDriveMotor.setPower(0.0);
        resetEncoders();
    }

    private boolean motorsNearTarget() {
        int targetLDriveMotorPos = HardwareManager.lDriveMotor.getTargetPosition();
        int currentLDriveMotorPos = HardwareManager.lDriveMotor.getCurrentPosition();
        int targetRDriveMotorPos = HardwareManager.rDriveMotor.getTargetPosition();
        int currentRDriveMotorPos = HardwareManager.rDriveMotor.getCurrentPosition();
        return Math.abs(currentLDriveMotorPos - targetLDriveMotorPos) < 10.0
                && Math.abs(currentRDriveMotorPos - targetRDriveMotorPos) < 10.0;
    }

    private void resetEncoders() {
        HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private boolean targetIsGold() {
        int minRed = 40;
        double minDifference = 1.5;
        if (HardwareManager.innerColorSensor.red() > minRed &&
                (HardwareManager.innerColorSensor.red() / HardwareManager.innerColorSensor.blue()) >= minDifference) {
            return true;
        }
        if (HardwareManager.outerColorSensor.red() > minRed &&
                (HardwareManager.outerColorSensor.red() / HardwareManager.outerColorSensor.blue()) >= minDifference) {
            return true;
        }
        return false;
    }

    private void extendArm() {
        HardwareManager.setArmBaseServoPower(0.6);
        sleep(1500);
        HardwareManager.armWristServo.setPosition(0.5);
        sleep(1000);
    }

    private void retractArm() {
        HardwareManager.setArmBaseServoPower(0.4);
        sleep(1000);
        HardwareManager.armWristServo.setPosition(0.0);
        sleep(1000);
    }

}