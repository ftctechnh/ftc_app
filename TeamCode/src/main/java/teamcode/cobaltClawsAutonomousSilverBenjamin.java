package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "cobaltClawsAutonomousGoldBenjamin", group = "Linear OpMode")

public class cobaltClawsAutonomousSilverBenjamin extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor; //motor 0
    private DcMotor RightDriveMotor; //motor 1
    private DcMotor ExtendArmMotor; //motor 2
    private DcMotor SwingArmMotor; //motor 3
    //private Servo RotateArmServo;
    private Servo GrabberServoLeft;
    private Servo GrabberServoRight;

    //establishes and sets starting motor positions
    int extendMotorPosition = this.ExtendArmMotor.getCurrentPosition();
    int swingMotorPosition = this.SwingArmMotor.getCurrentPosition();

    //establishes grabber positions
    boolean leftGrabberOpen;
    boolean rightGrabberOpen;

    //static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    //static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    //static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    //static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)
    //                                                    / (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            /*

            move(distance to front of items, 0.8);
            detect and move gold cube
            turn(left, away from items, 0.8);
            move(forward, 0.8);
            turn(left, toward depot, 0.8;
            move(to depot, 0.8);
            put marker in depot
            turn(right, toward same color crater, 0.8);
            move(to same color crater, 0.8);

             */


        }


    }

    private void initialize(){

        //giving internal hardware an external name for the app config
        this.LeftDriveMotor = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get (DcMotor.class, "RightDriveMotor");
        this.ExtendArmMotor = hardwareMap.get (DcMotor.class, "ExtendArmMotor");
        this.SwingArmMotor = hardwareMap.get (DcMotor.class, "SwingArmMotor");
        //this.RotateArmServo = hardwareMap.get (DcMotor.class, "RotateArmMotor");
        this.GrabberServoLeft = hardwareMap.get (Servo.class, "GrabberServoLeft");
        this.GrabberServoRight = hardwareMap.get (Servo.class, "GrabberServoRight");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        ExtendArmMotor.setDirection(DcMotor.Direction.FORWARD);
        SwingArmMotor.setDirection(DcMotor.Direction.FORWARD);

        //RotateArmMotor.setDirection(Servo.Direction.FORWARD);
        GrabberServoLeft.setDirection(Servo.Direction.FORWARD);
        GrabberServoRight.setDirection(Servo.Direction.REVERSE);



        //Sets arm motors to work with position
        ExtendArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SwingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets grabber servos to open
        this.GrabberServoLeft.setPosition(0);
        leftGrabberOpen = true;
        this.GrabberServoRight.setPosition(0);
        rightGrabberOpen = true;

        //Gets the current arm motor positions so driver can make sure motors are properly
        // calibrated.
        extendMotorPosition = this.ExtendArmMotor.getCurrentPosition();
        swingMotorPosition = this.SwingArmMotor.getCurrentPosition();

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.addData("Arm Position", extendMotorPosition);
        telemetry.addData("Arm Length", swingMotorPosition);
        telemetry.update();
    }

    public void move(String direction, int distance, double speed){


        //Resets encoder and moves the inputted degrees
        RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);


        if(direction == "F") {

            LeftDriveMotor.setTargetPosition(distance);
            RightDriveMotor.setTargetPosition(distance);

        } else if(direction == "B") {

            LeftDriveMotor.setTargetPosition(-distance);
            RightDriveMotor.setTargetPosition(-distance);

        }

        LeftDriveMotor.setPower(0);
        RightDriveMotor.setPower(0);


        //prevents other action until motors have reached positions
        double currentSpeed = (speed / 4);

        while((RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive()) {

            //Loop body can be empty
            telemetry.update();

            if(currentSpeed < speed){

                currentSpeed = currentSpeed + 0.005;

            }

            LeftDriveMotor.setPower(currentSpeed);
            RightDriveMotor.setPower(currentSpeed);


        }


        //Stops motors, ready for next action
        LeftDriveMotor.setPower(0);
        RightDriveMotor.setPower(0);

    }

    public void turn(String direction, int distance, double speed){

        if(direction == "L"){// left

            //Resets the encoders and does a left point turn for the inputted degrees
            RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
            LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);


            RightDriveMotor.setTargetPosition  (distance);
            LeftDriveMotor.setTargetPosition   (distance);

            LeftDriveMotor.setPower(speed);
            RightDriveMotor.setPower(-speed);


            //prevents other action until motors have reached positions
            while((RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive()) {

                telemetry.update();

            }

            //Stops motors, ready for next action
            LeftDriveMotor.setPower(0);
            RightDriveMotor.setPower(0);


        }

        if(direction == "R"){// right

            //Resets the encoders and does a right point turn for the inputted degrees
            RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
            LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);


            RightDriveMotor.setTargetPosition  (-distance);
            LeftDriveMotor.setTargetPosition   (-distance);

            LeftDriveMotor.setPower(-speed);
            RightDriveMotor.setPower(speed);


            //prevents other action until motors have reached positions
            while((RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive()) {

                telemetry.update();

            }

            //Stops motors, ready for next action
            LeftDriveMotor.setPower(0);
            RightDriveMotor.setPower(0);

        }

    }

}
