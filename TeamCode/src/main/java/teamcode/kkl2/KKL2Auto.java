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

        approach();

        while (opModeIsActive());
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

    private void turn(int degrees) {
        double radians = (degrees / 180.0) * Math.PI;
        turn(radians);
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

/*
        telemetry.addData("rightTarget", targetDriveRMotorPos);
        telemetry.addData("rightCurrent", currentDriveRMotorPos);
        telemetry.addData("leftTarget", targetDriveLMotorPos);
        telemetry.addData("leftCurrent", currentDriveLMotorPos);

        telemetry.update();
*/
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

        // lower the robot using the servo
        KKL2HardwareManager.liftLockServo.setPosition(1.0);
        sleep(9500);
        KKL2HardwareManager.liftLockServo.setPosition(0.5);

        // push arm backwards

        double liftBaseMotorPower = 1.0;
        double ticksPerDegree = Helper.REV_CORE_HEX_MOTOR_TICKS_PER_ROTATION / 360.0 * 7.0;
        int ticks = (int)(ticksPerDegree * 5);
        /*
        KKL2HardwareManager.liftBaseMotor.setTargetPosition(ticks);
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);
        while (opModeIsActive() && !liftNearTarget()) ;

        zeroLiftMotorPower();
        resetLiftEncoders();
        */

        // unlatch the lift arm
        KKL2HardwareManager.liftLatchServo.setPosition(0);
        sleep(500);
        KKL2HardwareManager.liftLatchServo.setPosition(1);
        sleep(500);
        KKL2HardwareManager.liftLatchServo.setPosition(0);
        sleep(2000);

        turn(7);

        // move arm back down
        resetLiftEncoders();
        ticks = (int)(ticksPerDegree * -70);
        KKL2HardwareManager.liftBaseMotor.setTargetPosition(ticks);
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);
        while (opModeIsActive() && !liftNearTarget()) ;
        zeroLiftMotorPower();
        resetLiftEncoders();
    }

    private void approach() {
        float height = 0;
        double power = 1.0;
        List<Mineral> minerals = null;

        /*
        while(1) {
        error = desired_value – actual_value
        integral = integral + (error*iteration_time)
        derivative = (error – error_prior)/iteration_time
        output = KP*error + KI*integral + KD*derivative + bias
        error_prior = error
        sleep(iteration_time)
        }
        */

        float error_prev = 1000;
        float error = 0;
        float integral = 0;
        float derivative = 0;
        float target_x = 640;
        double c = 1000;
        double ratio = 30.48 / 200; // centimeters / pixels (num pixel when the object is 1 ft away)
        double forward_distance = 25.4;
        boolean completed = false;

        while (opModeIsActive() && !completed) {
            minerals = this.tfManager.getRecognizedMinerals();

            if (minerals != null) {
                for (Mineral mineral : minerals) {
                    if (mineral.isGold()) {
                        /*
                        float center_y = (mineral.getLeft() + mineral.getRight()) / 2;
                        float center_x = (mineral.getBottom() + mineral.getTop()) / 2;
                        height = mineral.getRight() - mineral.getLeft();
                        telemetry.addData("Gold Height", height);
                        telemetry.addData("Gold Center X", center_x);
                        telemetry.addData("Gold Center Y", center_y);

                        c = Helper.getCentimetersFromPixels(height); // centimeters
                        error = target_x - center_x; // adjacent side in pixels
                        double a = c * error / Helper.CAMERA_DISTANCE; // centimeters
                        double b = Math.sqrt((c*c) - (a*a)); // centimeters
                        double radians = Math.asin(a / c);
                        double degrees = radians * 180.0 / Math.PI;

                        telemetry.addData("error", error);
                        telemetry.addData("inches away", c / 2.54);
                        telemetry.addData("degrees away", degrees);
                        */

                        double degrees = mineral.getAngle();
                        if (degrees > 10 || degrees < -10) {
                            // turn towards the gold
                            turn(degrees * Math.PI / 180);
                        } else {
                            telemetry.addData("Facing Gold", "No Turn, Move Forward");
                            // Move to knock the gold
                            drive(c + 45, power);
                            completed = true;
                        }

                        telemetry.update();
                    }
                }
            }
            else {
                telemetry.addData("Can't find minerals", "Move back");
                telemetry.update();
                // drive back to try and detect objects
                drive(-3, power);
            }
        }
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
