package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;

import teamcode.connor.HardwareManager;
import teamcode.examples.Helper;
import teamcode.examples.Mineral;
import teamcode.examples.TensorFlowManager;
import teamcode.kkl2.KKL2HardwareManager;

@Autonomous(name = "cobaltClawsAutoTest", group = "Linear OpMode")

public class cobaltClawsAutoTest extends LinearOpMode {



    double driveSpeed = 0.25;

    private static final double INCH_CONVERSION_RATIO = 55.0 / 0.39370079;
    private static final double RADIAN_CONVERSION_RATIO = 1066.15135303;
    private static final double DEGREES_TO_RADIANS = Math.PI / 180;

    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;

    private TensorFlowManager tfManager;

    public enum Direction {Forward, Backward, Left, Right}

    @Override
    public void runOpMode() {


        //run the initialize block
        KKL2HardwareManager.initialize(this);
        this.initialize();

        waitForStart();

        while (opModeIsActive()) {


            // lower the robot using the servo
            KKL2HardwareManager.liftSupportServo.setPosition(1.0);
            sleep(9500);
            KKL2HardwareManager.liftSupportServo.setPosition(0.5);

            // push arm backwards

            double liftBaseMotorPower = 1.0;
            double ticksPerDegree = Helper.REV_CORE_HEX_MOTOR_TICKS_PER_ROTATION / 360.0 * 5.0;

            // unlatch the lift arm
            KKL2HardwareManager.liftLatchServo.setPosition(0);
            sleep(500);
            KKL2HardwareManager.liftLatchServo.setPosition(1);
            sleep(500);
            KKL2HardwareManager.liftLatchServo.setPosition(0);
            sleep(500);

            // move arm back down
            resetLiftEncoders();
            int ticks = (int)(ticksPerDegree * -70);
            KKL2HardwareManager.liftBaseMotor.setTargetPosition(ticks);
            KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);
            while (opModeIsActive() && !liftNearTarget()) ;
            zeroLiftMotorPower();
            resetLiftEncoders();

            //requestOpModeStop();

            //move(Direction.Forward, 10, driveSpeed);

            int correctionAngle = 0;
            int correctionDistance = 0;
            boolean searchForMinerals = true;

            while(searchForMinerals) {
                //Detects the gold, turns and drives forward to know it off, then moves back and goes to
                //  a designated spot, then turns
                List<Mineral> minerals = tfManager.getRecognizedMinerals();


                if (minerals != null) {

                    for (Mineral mineral : minerals) {

                        if (mineral.isGold()) {
                            // check where in the field of view is the gold
                            float center = (mineral.getTop() + mineral.getBottom()) / 2;
                            if (Math.abs(640 - center) <= 300) {

                                telemetry.addData("position: ", "center activated");
                                telemetry.addData("center: ", center);

                                //move straight
                                move(Direction.Forward, 40, driveSpeed);

                                correctionDistance += 10;
                                searchForMinerals = !searchForMinerals;



                            } else if (640 - center < 0) {

                                telemetry.addData("position: ", "right activated");
                                telemetry.addData("center: ", center);

                                //turn left
                                turn(Direction.Right, 20, driveSpeed);
                                move(Direction.Forward, 30, driveSpeed);

                                correctionAngle += 20;
                                searchForMinerals = !searchForMinerals;



                            } else {

                                telemetry.addData("position: ", "left activated");
                                telemetry.addData("center: ", center);

                                //turn right
                                turn(Direction.Left, 20, driveSpeed);
                                move(Direction.Forward, 30, driveSpeed);

                                correctionAngle -= 20;
                                searchForMinerals = !searchForMinerals;


                            }


                        }

                    }

                }

                telemetry.update();

            }

            //sleep(2000);

            telemetry.addData("position: ", "go to crater");
            telemetry.addData("correctionAngle", correctionAngle);
            telemetry.update();

            move(Direction.Backward, 30 + correctionDistance, + driveSpeed);
            turn(Direction.Left, correctionAngle, driveSpeed);
            move(Direction.Forward, 17, driveSpeed);
            turn(Direction.Left, 80, driveSpeed);


            //From the designated spot, drives to and into the crater.
            move(Direction.Forward, 23, driveSpeed);
            turn(Direction.Left, 35, driveSpeed);
            move(Direction.Forward, 36, driveSpeed);
            requestOpModeStop();

        }


    }

    private void initialize() {

        KKL2HardwareManager.driveLMotor.setDirection(DcMotor.Direction.FORWARD);
        KKL2HardwareManager.driveRMotor.setDirection(DcMotor.Direction.REVERSE);

        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.tfManager = new TensorFlowManager(this.hardwareMap, this.telemetry);
        this.tfManager.initialize();
    }


    public void move(Direction direction, int inches, double speed) {

        //Changes inches to work with ticks
        inches *= INCH_CONVERSION_RATIO;

        //Resets encoder and moves the inputted ticks
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (direction == Direction.Forward) {

            KKL2HardwareManager.driveLMotor.setTargetPosition(inches);
            KKL2HardwareManager.driveRMotor.setTargetPosition(inches);

        } else if (direction == Direction.Backward) {

            KKL2HardwareManager.driveLMotor.setTargetPosition(-inches);
            KKL2HardwareManager.driveRMotor.setTargetPosition(-inches);

        }

        KKL2HardwareManager.driveLMotor.setPower(speed);
        KKL2HardwareManager.driveRMotor.setPower(speed);

        while (!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        KKL2HardwareManager.driveLMotor.setPower(0);
        KKL2HardwareManager.driveRMotor.setPower(0);

    }

    public void turn(Direction direction, int degrees, double speed) {

        //Converts degrees to radians, then changes to work with ticks
        int radians = (int)(degrees * DEGREES_TO_RADIANS * RADIAN_CONVERSION_RATIO);


        //Resets the encoders and does a left point turn for the inputted degrees
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (direction == Direction.Left) {

            KKL2HardwareManager.driveRMotor.setTargetPosition(-radians);
            KKL2HardwareManager.driveLMotor.setTargetPosition(radians);

        }

        if (direction == Direction.Right) {

            KKL2HardwareManager.driveRMotor.setTargetPosition(radians);
            KKL2HardwareManager.driveLMotor.setTargetPosition(-radians);

        }

        KKL2HardwareManager.driveLMotor.setPower(speed);
        KKL2HardwareManager.driveRMotor.setPower(speed);

        while (!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        KKL2HardwareManager.driveLMotor.setPower(0);
        KKL2HardwareManager.driveRMotor.setPower(0);

    }

    public boolean motorsBusy() {

        return (KKL2HardwareManager.driveRMotor.isBusy() || KKL2HardwareManager.driveLMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget() {

        int lDif = (KKL2HardwareManager.driveLMotor.getTargetPosition()
                - KKL2HardwareManager.driveLMotor.getCurrentPosition());
        int rDif = (KKL2HardwareManager.driveRMotor.getTargetPosition()
                - KKL2HardwareManager.driveRMotor.getCurrentPosition());

        return ((Math.abs(lDif) <= 10) & (Math.abs(rDif) <= 10));

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

}

