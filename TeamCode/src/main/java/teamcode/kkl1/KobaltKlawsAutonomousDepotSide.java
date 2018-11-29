package teamcode.kkl1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "KobaltKlawsAutonomousDepotSide", group = "Linear OpMode")
public class KobaltKlawsAutonomousDepotSide extends LinearOpMode {

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
        KKL1HardwareManager.init(this);
        this.initialize();
        KKL1HardwareManager.liftMotor.setTargetPosition(-500);
        KKL1HardwareManager.liftMotor.setPower(1.0);
        sleep(800);
        KKL1HardwareManager.liftMotor.setPower(0.0);
        sleep(1000);
        drive(65, 0.5);
        KKL1HardwareManager.intakeServo.setPosition(1.0); // opens right claw
        sleep(3000);
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
        KKL1HardwareManager.lDriveMotor.setPower(0.0);
        KKL1HardwareManager.rDriveMotor.setPower(0.0);
        resetEncoders();
    }

    private void turn(double radians) {
        int posL = (int) (2 * radians * STEER_TICKS_PER_RADIAN);
        int posR = (int) (-2 * radians * STEER_TICKS_PER_RADIAN);
        KKL1HardwareManager.lDriveMotor.setPower(0.0);
        KKL1HardwareManager.rDriveMotor.setPower(0.0);
        KKL1HardwareManager.lDriveMotor.setTargetPosition(posL);
        KKL1HardwareManager.rDriveMotor.setTargetPosition(posR);
        KKL1HardwareManager.lDriveMotor.setPower(TURN_POWER);
        KKL1HardwareManager.rDriveMotor.setPower(TURN_POWER);
        while (!motorsNearTarget()) ;
        KKL1HardwareManager.lDriveMotor.setPower(0.0);
        KKL1HardwareManager.rDriveMotor.setPower(0.0);
        resetEncoders();
    }

    private boolean motorsNearTarget() {
        int targetLDriveMotorPos = KKL1HardwareManager.lDriveMotor.getTargetPosition();
        int currentLDriveMotorPos = KKL1HardwareManager.lDriveMotor.getCurrentPosition();
        int targetRDriveMotorPos = KKL1HardwareManager.rDriveMotor.getTargetPosition();
        int currentRDriveMotorPos = KKL1HardwareManager.rDriveMotor.getCurrentPosition();
        return Math.abs(currentLDriveMotorPos - targetLDriveMotorPos) < 10.0 && Math.abs(currentRDriveMotorPos - targetRDriveMotorPos) < 10.0;
    }

    private void resetEncoders() {
        KKL1HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL1HardwareManager.lDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL1HardwareManager.rDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private boolean targetIsGold() {
        int minRed = 40;
        double minDifference = 1.5;
        if (KKL1HardwareManager.innerColorSensor.red() > minRed &&
                (KKL1HardwareManager.innerColorSensor.red() / KKL1HardwareManager.innerColorSensor.blue()) >= minDifference) {
            return true;
        }
        if (KKL1HardwareManager.outerColorSensor.red() > minRed &&
                (KKL1HardwareManager.outerColorSensor.red() / KKL1HardwareManager.outerColorSensor.blue()) >= minDifference) {
            return true;
        }
        return false;
    }

    private void extendArm() {
        KKL1HardwareManager.setArmBaseServoPower(0.6);
        sleep(1500);
        KKL1HardwareManager.armWristServo.setPosition(0.5);
        sleep(1000);
    }

    private void retractArm() {
        KKL1HardwareManager.setArmBaseServoPower(0.4);
        sleep(1000);
        KKL1HardwareManager.armWristServo.setPosition(0.0);
        sleep(1000);
    }
}
