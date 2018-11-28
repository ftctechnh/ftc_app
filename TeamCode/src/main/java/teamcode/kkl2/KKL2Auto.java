package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import teamcode.examples.Helper;
import teamcode.examples.Mineral;
import teamcode.examples.TensorFlowManager;

@Autonomous(name = "KKL2Auto", group = "Linear OpMode")
public class KKL2Auto extends LinearOpMode {

    private static final double DRIVE_TICKS_PER_CENTIMETER_COVERED = -55.0;
    private static final double TURN_TICKS_PER_RADIAN_COVERED = 1066.15135303;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;
    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;
    private static final double TURN_POWER = 0.5;

    private TensorFlowManager tfManager;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        this.initialize();

        waitForStart();
        resetDriveEncoders();

        unlatch();

        approachGold();
    }

    private void drive(double centimeters, double power) {
        zeroDriveMotorPower();

        KKL2HardwareManager.driveLMotor.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        KKL2HardwareManager.driveRMotor.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));

        KKL2HardwareManager.driveLMotor.setPower(power);
        KKL2HardwareManager.driveRMotor.setPower(power);

        while (opModeIsActive() && !driveNearTarget()) ;
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

        while (opModeIsActive() && !driveNearTarget()) ;
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

    private boolean driveNearTarget() {
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

    private void resetLiftEncoders() {
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void zeroLiftMotorPower() {
        KKL2HardwareManager.liftBaseMotor.setPower(0.0);
    }

    private boolean liftNearTarget() {

        int targetLiftMotorPos = KKL2HardwareManager.liftBaseMotor.getTargetPosition();

        int currentLiftMotorPos = KKL2HardwareManager.liftBaseMotor.getCurrentPosition();

        boolean nearTarget = Math.abs(currentLiftMotorPos - targetLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;

        telemetry.addData("liftTarget", targetLiftMotorPos);
        telemetry.addData("liftCurrent", currentLiftMotorPos);

        telemetry.update();

        return nearTarget;
    }

    private void unlatch() {
        zeroLiftMotorPower();

        // lower the robot using the lift arm
        double liftBaseMotorPower = 1.0;
        double ticksPerDegree = Helper.REV_CORE_HEX_MOTOR_TICKS_PER_ROTATION / 360.0 * 7.0;
        int ticks = (int)(ticksPerDegree * 70);
        KKL2HardwareManager.liftBaseMotor.setTargetPosition(ticks);
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);

        while (opModeIsActive() && !liftNearTarget()) ;
        zeroLiftMotorPower();
        resetLiftEncoders();

        // unlatch the lift arm
        KKL2HardwareManager.liftLatchServo.setPosition(0);
        sleep(2000);
    }

    private void approachGold() {
        boolean approachComplete = false;
        double power = 1.0;
        int goldIndex = 0;
        List<Mineral> minerals = null;

        // Get close to view the minerals
        //while (!approachComplete){
        while (opModeIsActive()) {
            minerals = this.tfManager.getRecognizedMinerals();
            Mineral gold = null;
            int index = 0;
            if (minerals != null) {
                // Update trajectory
                int i = 0;
                for (Mineral mineral : minerals) {
                    if (mineral.isGold()) {
                        gold = mineral;
                        index++;
                    }
                    addTelemetry(mineral, ++i);
                }
                telemetry.update();
            }

            sleep(2000);

            // Check if we are within approach limit
            // 1280 x 720
            // center of the screen is y = 360 and x = 640
            if (gold != null) {
                float center_y = (gold.getLeft() + gold.getRight()) / 2;
                float center_x = (gold.getBottom() + gold.getTop()) / 2;
                float height = gold.getRight() - gold.getLeft();
                telemetry.addData("Gold Height", height);
                telemetry.addData("Gold Center Y", center_y);
                telemetry.addData("Gold Center X", center_x);
                telemetry.update();
                /*
                if (center_y >= 350 && center_y <= 370) {
                    goldIndex = index;
                    break;
                }*/
                if (height >= 20) {
                    //break;
                }
            }
            else {
                // Move robot 1 inch closer
                //drive(2.54, power);
            }
        }

        // Turn to face the gold
        double ninetyDegreesInRadians = Math.PI / 2;
        double distanceBetweenMinerals = 30;
        if (goldIndex == 1) {
            // gold is on the left
            // turn left 90
            turn(-ninetyDegreesInRadians);
            drive(distanceBetweenMinerals, power);
            turn(ninetyDegreesInRadians);
        }
        else if (goldIndex == 3) {
            // gold is on the right
            // turn right 90
            turn(ninetyDegreesInRadians);
            drive(distanceBetweenMinerals, power);
            turn(-ninetyDegreesInRadians);
        }

        // Knock the gold off
        drive(30, power);
        minerals = this.tfManager.getRecognizedMinerals();
        if (minerals != null) {
            int i = 0;
            for (Mineral mineral : minerals) {
                addTelemetry(mineral, ++i);
            }
            telemetry.update();
        }
    }

    private void initialize() {
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.tfManager = new TensorFlowManager(this.hardwareMap);
        this.tfManager.initialize();
    }

    private void addTelemetry(Mineral mineral, int num) {
        if (mineral != null) {
            telemetry.addData(
                    "Mineral" + num + " isGold left bottom",
                    "%s %f %f",
                    mineral.isGold(),
                    mineral.getLeft(),
                    mineral.getBottom());
        }
    }
}
