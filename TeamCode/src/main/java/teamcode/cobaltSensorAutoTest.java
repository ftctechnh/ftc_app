package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "cobaltSensorAutoTest", group = "Linear OpMode")

public class cobaltSensorAutoTest extends LinearOpMode {

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

    //1000 ticks is about 26 inches

    public enum Direction {Forward, Backward, Left, Right}

    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            telemetry.addData("Gold: ", isGold());
            telemetry.addData("OuterSensor RED:", colorSensorOuter.red());
            telemetry.addData("OuterSensor GREEN:", colorSensorOuter.green());
            telemetry.addData("OuterSensor BLUE:", colorSensorOuter.blue());
            telemetry.addData("InnerSensor RED:", colorSensorInner.red());
            telemetry.addData("InnerSensor GREEN:", colorSensorInner.green());
            telemetry.addData("InnerSensor BLUE:", colorSensorInner.blue());
            telemetry.update();


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

        int tolerance = 200;

        if (((Math.abs(colorSensorInner.red() - 275) <= tolerance) &&
                (Math.abs(colorSensorInner.green() - 185) <= tolerance) &&
                (Math.abs(colorSensorInner.blue() - 100) <= tolerance)) ||
                ((Math.abs(colorSensorOuter.red() - 310) <= tolerance) &&
                (Math.abs(colorSensorOuter.green() - 210) <= tolerance) &&
                (Math.abs(colorSensorOuter.blue() - 95) <= tolerance))) {
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

