package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import teamcode.examples.TensorFlowManager;
import teamcode.kkl2.KKL2HardwareManager;

@Autonomous(name = "cobaltClawsAutoTest", group = "Linear OpMode")

public class cobaltClawsAutoTest extends LinearOpMode {



    double driveSpeed = 0.25;

    private ColorSensor colorSensorOuter;
    private ColorSensor colorSensorInner;


    private static final double INCH_CONVERSION_RATIO = 55.0;
    private static final double RADIAN_CONVERSION_RATIO = 1066.15135303;
    private static final double DEGREES_TO_RADIANS = Math.PI / 180;

    private TensorFlowManager tfManager;

    public enum Direction {Forward, Backward, Left, Right}

    @Override
    public void runOpMode() {


        //run the initialize block
        KKL2HardwareManager.initialize(this);
        this.initialize();

        waitForStart();

        while (opModeIsActive()) {

            //HANG RELEASE

            KKL2HardwareManager.liftLockServo.setPosition(0);

            sleep(10500);

            KKL2HardwareManager.liftLockServo.setPosition(0.5);

            KKL2HardwareManager.liftLatchServo.setPosition(1.0);

            sleep(2000);

            move(Direction.Forward, 5, driveSpeed);

            requestOpModeStop();


            /*//GOLD TEST

            move(Direction.Backward, 400, driveSpeed);
            //Looks at the right mineral. If the right mineral is gold, turns and knocks over the
            // mineral, then turns and goes to the depot, then turns and drives into the crater.

            if (isGold()) {

                telemetry.addData("Gold: ", "Detected");
                telemetry.update();

                turn(Direction.Left, 350, driveSpeed);
                SensorServo.setPosition(0);
                move(Direction.Forward, 1200, driveSpeed);
                setArm(0.3, 0.8, -0.5);
                sleep(200);
                setArm(0, 0, 0);
                turn(Direction.Left, 250, driveSpeed);
                move(Direction.Forward, 800, driveSpeed);
                turn(Direction.Left, 270, driveSpeed);
                move(Direction.Forward, 2000, driveSpeed);

                break;

            } else {

                //Looks at the center mineral. If the center mineral is gold, goes straight to the
                // depot, then turns and drives into the crater.

                move(Direction.Backward, 400, driveSpeed);

                if (isGold()) {

                    telemetry.addData("Gold: ", "Detected");
                    telemetry.update();

                    turn(Direction.Left, 150, driveSpeed);
                    SensorServo.setPosition(0);
                    move(Direction.Forward, 1400, driveSpeed);
                    setArm(0.3, 0.8, -0.5);
                    sleep(200);
                    setArm(0, 0, 0);
                    turn(Direction.Left, 500, driveSpeed);
                    move(Direction.Forward, 800, driveSpeed);
                    turn(Direction.Left, 270, driveSpeed);
                    move(Direction.Forward, 2000, driveSpeed);

                    break;

                } else {

                    //Turns to face the left mineral. If the left mineral is gold, goes to the
                    // mineral, then turns and goes to the depot, then turns and drives into the
                    // crater.
                    move(Direction.Backward, 400, driveSpeed);


                    if (isGold()) {

                        telemetry.addData("Gold: ", "Detected");
                        telemetry.update();

                        turn(Direction.Left, 250, driveSpeed);
                        SensorServo.setPosition(0);
                        move(Direction.Forward, 1200, driveSpeed);
                        setArm(0.3, 0.8, -0.5);
                        sleep(200);
                        setArm(0, 0, 0);
                        turn(Direction.Left, 1000, driveSpeed);
                        move(Direction.Forward, 800, driveSpeed);
                        turn(Direction.Left, 270, driveSpeed);
                        move(Direction.Forward, 2000, driveSpeed);

                        break;


                    }

                }

            }*/
            requestOpModeStop();


            //SILVER TEST



            /*if(isGold()) {

                //Looks at the left mineral. If the left mineral is gold, goes to the mineral,
                // then drives to the depot, then turns and drives into the crater.

                move(Direction.Forward,  1000, 0.25);
                move(Direction.Backward, 200,  0.25);
                turn(Direction.Left,     500,  0.25);
                move(Direction.Forward,  1200, 0.25);
                turn(Direction.Left,     50,   0.25);
                move(Direction.Forward,  1750, 0.25);
                turn(Direction.Right,    750,  0.25);
                move(Direction.Forward,  3000, 0.25);

                break;


            } else {

                //Looks at the center mineral. If the center mineral is gold, goes to the mineral,
                // then drives to the depot, then turns and drives into the crater.
                move(Direction.Backward, 100, 0.25);
                turn(Direction.Right, 250, 0.25);
                move(Direction.Forward, 100, 0.25);

                if(isGold()) {

                    move(Direction.Forward, 1000, 0.25);
                    move(Direction.Backward, 200, 0.25);
                    turn(Direction.Left, 300, 0.25);
                    move(Direction.Forward, 1500, 0.25);
                    turn(Direction.Left, 200, 0.25);
                    move(Direction.Forward, 1750, 0.25);
                    turn(Direction.Right, 750, 0.25);
                    move(Direction.Forward, 3000, 0.25);

                    break;

                } else{

                    //Turns to face the right mineral. If the right mineral is gold, goes to the
                    // mineral, then drives to the depot, then turns and drives into the
                    // crater.
                    move(Direction.Backward, 100, 0.25);
                    turn(Direction.Right, 250, 0.25);
                    move(Direction.Forward, 100, 0.25);

                    if(isGold()){

                        move(Direction.Forward,  1000, 0.25);
                        move(Direction.Backward, 200,  0.25);
                        turn(Direction.Left,     1000, 0.25);
                        move(Direction.Forward,  400,  0.25);
                        turn(Direction.Left,     30,   0.25);
                        move(Direction.Forward,  1300, 0.25);
                        turn(Direction.Left,     300,  0.25);
                        move(Direction.Forward,  1750, 0.25);
                        turn(Direction.Right,    750,  0.25);
                        move(Direction.Forward,  3000, 0.25);

                        break;

                    }

                }

            }*/

        }


    }

    private void initialize() {
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.tfManager = new TensorFlowManager(this.hardwareMap);
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

            KKL2HardwareManager.driveRMotor.setTargetPosition(radians);
            KKL2HardwareManager.driveLMotor.setTargetPosition(-radians);

        }

        if (direction == Direction.Right) {

            KKL2HardwareManager.driveRMotor.setTargetPosition(-radians);
            KKL2HardwareManager.driveLMotor.setTargetPosition(radians);

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

}

