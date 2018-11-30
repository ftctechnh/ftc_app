package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "cobaltClawsAutoTest", group = "Linear OpMode")

public class cobaltClawsAutoTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor;  //motor 0
    private DcMotor RightDriveMotor; //motor 1

    //private DcMotor ArmMotor; //motor 2
    private DcMotor HangMotor; //motor 3

    private Servo ArmServoBase;  //servo
    private Servo ArmServoWrist; //servo
    private Servo GrabberServo;  //servo
    private Servo SensorServo;   // servo

    //establishes and sets starting motor positions
    //int armInitialPosition = 0; //guessed limit
    //double armMaximumPosition = 600; //guessed limit

    //int armPosition;

    boolean hangArmUp;

    double driveSpeed = 0.25;

    private ColorSensor colorSensorOuter;
    private ColorSensor colorSensorInner;


    private static final double INCH_CONVERSION_RATIO = 55.0;
    private static final double RADIAN_CONVERSION_RATIO = 1066.15135303;

    public enum Direction {Forward, Backward, Left, Right}

    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            //HANG RELEASE

            //Moves the grabber wrist out of the way, then rotates the arm motor until the robot is
            //on the ground. Then moves right wheel forward to get out of hook, and positions for
            //route.

            HangMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            HangMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            HangMotor.setTargetPosition(-2000);
            HangMotor.setPower(0.8);

            while (HangMotor.isBusy()) {

            }


            turn(Direction.Right, 295, driveSpeed);

            move(Direction.Forward, 1175, driveSpeed);

            turn(Direction.Right, 320, driveSpeed);

            SensorServo.setPosition(0.5);
            sleep(1000);


            //GOLD TEST

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

            }
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

        //giving internal hardware an external name for the app config
        this.LeftDriveMotor = hardwareMap.get(DcMotor.class, "LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get(DcMotor.class, "RightDriveMotor");
        //this.ArmMotor = hardwareMap.get (DcMotor.class, "ArmMotor");
        this.HangMotor = hardwareMap.get(DcMotor.class, "HangMotor");
        this.ArmServoWrist = hardwareMap.get (Servo.class, "ArmServoWrist");
        this.ArmServoBase = hardwareMap.get (Servo.class, "ArmServoBase");
        this.GrabberServo = hardwareMap.get (Servo.class, "GrabberServo");
        this.SensorServo = hardwareMap.get(Servo.class, "SensorServo");

        this.colorSensorOuter = hardwareMap.get(ColorSensor.class, "colorSensorOuter");
        this.colorSensorInner = hardwareMap.get(ColorSensor.class, "colorSensorInner");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        //ArmMotor.setDirection(DcMotor.Direction.FORWARD);
        HangMotor.setDirection(DcMotor.Direction.FORWARD);


        ArmServoBase.setDirection(Servo.Direction.FORWARD);
        ArmServoWrist.setDirection(Servo.Direction.FORWARD);

        GrabberServo.setDirection(Servo.Direction.FORWARD);

        SensorServo.setDirection(Servo.Direction.FORWARD);

        //Sets motors to work with position
        //ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HangMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        RightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //ArmMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HangMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        hangArmUp = true;

        //Sets grabber servo to closed
        //this.GrabberServo.setPosition(0);

        //Sets arm servos to hang position
        //this.ArmServoWrist.setPosition(0);
        //this.ArmServoBase.setPosition(0);

        //Gives power to the arm motor
        //this.ArmMotor.setPower(1.0);

        //Gets the current arm motor positions so driver can make sure motors are properly
        // calibrated.
        //armPosition = this.ArmMotor.getCurrentPosition();

        //Tells the driver station that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.addData("Servo Position: ", SensorServo.getPosition());
        telemetry.update();
    }

    public void move(Direction direction, int inches, double speed) {

        //Changes inches to work with ticks
        inches *= INCH_CONVERSION_RATIO;

        //Resets encoder and moves the inputted ticks
        RightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (direction == Direction.Forward) {

            LeftDriveMotor.setTargetPosition(inches);
            RightDriveMotor.setTargetPosition(inches);

        } else if (direction == Direction.Backward) {

            LeftDriveMotor.setTargetPosition(-inches);
            RightDriveMotor.setTargetPosition(-inches);

        }

        LeftDriveMotor.setPower(speed);
        RightDriveMotor.setPower(speed);

        while (!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower(0);
        RightDriveMotor.setPower(0);

    }

    public void turn(Direction direction, int radians, double speed) {

        //Changes degrees to work with ticks
        radians *= RADIAN_CONVERSION_RATIO;

        //Resets the encoders and does a left point turn for the inputted degrees
        RightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (direction == Direction.Left) {

            RightDriveMotor.setTargetPosition(radians);
            LeftDriveMotor.setTargetPosition(-radians);

        }

        if (direction == Direction.Right) {

            RightDriveMotor.setTargetPosition(-radians);
            LeftDriveMotor.setTargetPosition(radians);

        }

        LeftDriveMotor.setPower(speed);
        RightDriveMotor.setPower(speed);

        while (!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower(0);
        RightDriveMotor.setPower(0);

    }

    public boolean motorsBusy() {

        return (RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget() {

        int lDif = (LeftDriveMotor.getTargetPosition() - LeftDriveMotor.getCurrentPosition());
        int rDif = (RightDriveMotor.getTargetPosition() - RightDriveMotor.getCurrentPosition());

        return ((Math.abs(lDif) <= 10) & (Math.abs(rDif) <= 10));

    }

    /*public boolean isGold() {

        if ((((colorSensorOuter.red() >= (2 * colorSensorOuter.blue()))
                && (colorSensorOuter.green() >= (2 * colorSensorOuter.blue()))) ||
                ((colorSensorInner.red() >= (2 * colorSensorInner.blue()))
                && (colorSensorInner.green() >= (2 * colorSensorInner.blue()))))) {
            return true;

        }

        return false;

    }*/

    public boolean isGold() {
        int minRed = 40;
        double minDifference = 1.5;

        if (colorSensorInner.red() > minRed &&
                (colorSensorInner.red() / colorSensorInner.blue()) >= minDifference) {
            return true;
        }

        if (colorSensorOuter.red() > minRed &&
                (colorSensorOuter.red() / colorSensorOuter.blue()) >= minDifference) {
            return true;
        }

        return false;
    }

    //Moves arm servos to indicated positions
    //For GrabberPosition, 0.5 is Left Open, -0.5 is Right Open, 0 is Closed
    public void setArm(double BasePosition, double WristPosition, double GrabberPosition){

        ArmServoBase.setPosition(BasePosition);
        ArmServoWrist.setPosition(WristPosition);
        GrabberServo.setPosition(GrabberPosition);

    }

}

