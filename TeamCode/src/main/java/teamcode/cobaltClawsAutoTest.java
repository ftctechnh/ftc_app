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


    private ColorSensor colorSensorOuter;
    private ColorSensor colorSensorInner;

    //1000 ticks is about 26 inches

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

            HangMotor.setTargetPosition(-2400);
            HangMotor.setPower(0.8);

            while (HangMotor.isBusy()) {

            }


            turn(Direction.Right, 270, 0.25);

            move(Direction.Forward, 1300, 0.25);

            turn(Direction.Right, 300, 0.25);

            SensorServo.setPosition(0.05);


            //GOLD TEST

            move(Direction.Backward, 200, 0.25);
            //Looks at the right mineral. If the right mineral is gold, turns and knocks over the
            // mineral, then turns and goes to the depot, then turns and drives into the crater.

            if (isGold()) {

                turn(Direction.Left, 350, 0.25);
                SensorServo.setPosition(0);
                move(Direction.Forward, 1200, 0.25);
                setArm(0.3, 0, -0.5);
                sleep(200);
                setArm(0, 0.3, 0);
                turn(Direction.Left, 250, 0.25);
                move(Direction.Forward, 800, 0.25);
                turn(Direction.Left, 270, 0.25);
                move(Direction.Forward, 2000, 0.25);

                break;

            } else {

                //Looks at the center mineral. If the center mineral is gold, goes straight to the
                // depot, then turns and drives into the crater.

                move(Direction.Backward, 100, 0.25);

                if (isGold()) {

                    turn(Direction.Left, 150, 0.25);
                    SensorServo.setPosition(0);
                    move(Direction.Forward, 1400, 0.25);
                    setArm(0.3, 0, -0.5);
                    sleep(200);
                    setArm(0, 0.3, 0);
                    turn(Direction.Left, 500, 0.25);
                    move(Direction.Forward, 800, 0.25);
                    turn(Direction.Left, 270, 0.25);
                    move(Direction.Forward, 2000, 0.25);

                    break;

                } else {

                    //Turns to face the left mineral. If the left mineral is gold, goes to the
                    // mineral, then turns and goes to the depot, then turns and drives into the
                    // crater.
                    move(Direction.Backward, 100, 0.25);


                    if (isGold()) {

                        turn(Direction.Left, 250, 0.25);
                        SensorServo.setPosition(0);
                        move(Direction.Forward, 1200, 0.25);
                        setArm(0.3, 0, -0.5);
                        sleep(200);
                        setArm(0, 0.3, 0);
                        turn(Direction.Left, 1000, 0.25);
                        move(Direction.Forward, 800, 0.25);
                        turn(Direction.Left, 270, 0.25);
                        move(Direction.Forward, 2000, 0.25);

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
        this.GrabberServo.setPosition(0);

        //Sets arm servos to hang position
        this.ArmServoWrist.setPosition(0.9);
        this.ArmServoBase.setPosition(0.9);

        //Gives power to the arm motor
        //this.ArmMotor.setPower(1.0);

        //Gets the current arm motor positions so driver can make sure motors are properly
        // calibrated.
        //armPosition = this.ArmMotor.getCurrentPosition();

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.update();
    }

    public void move(Direction direction, int distance, double speed) {


        //Resets encoder and moves the inputted ticks
        RightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (direction == Direction.Forward) {

            LeftDriveMotor.setTargetPosition(distance);
            RightDriveMotor.setTargetPosition(distance);

        } else if (direction == Direction.Backward) {

            LeftDriveMotor.setTargetPosition(-distance);
            RightDriveMotor.setTargetPosition(-distance);

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

    public void turn(Direction direction, int distance, double speed) {


        //Resets the encoders and does a left point turn for the inputted degrees
        RightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (direction == Direction.Left) {

            RightDriveMotor.setTargetPosition(distance);
            LeftDriveMotor.setTargetPosition(-distance);

        }

        if (direction == Direction.Right) {

            RightDriveMotor.setTargetPosition(-distance);
            LeftDriveMotor.setTargetPosition(distance);

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

    public boolean isGold() {

        if (((colorSensorOuter.red() >= (2 * colorSensorOuter.blue()))
                && colorSensorOuter.green() >= (2 * colorSensorOuter.blue()) ||
                (colorSensorOuter.red() >= (2 * colorSensorOuter.blue()))
                && colorSensorOuter.green() >= (2 * colorSensorOuter.blue()))) {
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

