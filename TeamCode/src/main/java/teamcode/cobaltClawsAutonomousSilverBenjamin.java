package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "cobaltClawsAutonomousSilverBenjamin", group = "Linear OpMode")

public class cobaltClawsAutonomousSilverBenjamin extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor; //motor 0
    private DcMotor RightDriveMotor; //motor 1
    private DcMotor ArmMotor; //motor 2
    private DcMotor HangMotor; //motor 3

    private Servo ArmServoBottom;
    private Servo ArmServoTop;
    private Servo GrabberServo;

    //establishes and sets starting motor position
    int armMotorPosition = this.ArmMotor.getCurrentPosition();

    //1000 ticks is about 26 inches


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
        this.ArmMotor = hardwareMap.get (DcMotor.class, "ArmMotor");
        this.HangMotor = hardwareMap.get (DcMotor.class, "HangMotor");

        this.ArmServoTop = hardwareMap.get (Servo.class, "ArmServoTop");
        this.ArmServoBottom = hardwareMap.get (Servo.class, "ArmServoBottom");
        this.GrabberServo = hardwareMap.get (Servo.class, "GrabberServo");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        ArmMotor.setDirection(DcMotor.Direction.FORWARD);
        HangMotor.setDirection(DcMotor.Direction.FORWARD);

        ArmServoBottom.setDirection(Servo.Direction.FORWARD);
        ArmServoTop.setDirection(Servo.Direction.FORWARD);
        GrabberServo.setDirection(Servo.Direction.FORWARD);



        //Sets arm motors to work with position
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets grabber servos to open
        this.GrabberServo.setPosition(0);

        //Gets the current arm motor positions so driver can make sure motors are properly
        // calibrated.
        armMotorPosition = this.ArmMotor.getCurrentPosition();

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.addData("Arm Position: ", armMotorPosition);
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

        LeftDriveMotor.setPower    (speed);
        RightDriveMotor.setPower   (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower    (0);
        RightDriveMotor.setPower   (0);

    }

    public void turn(String direction, int distance, double speed){


        //Resets the encoders and does a left point turn for the inputted degrees
        RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);

        if(direction == "L"){// left

            RightDriveMotor.setTargetPosition  (-distance);
            LeftDriveMotor.setTargetPosition   (distance);

        }

        if(direction == "R"){// right

            RightDriveMotor.setTargetPosition  (distance);
            LeftDriveMotor.setTargetPosition   (-distance);

        }

        LeftDriveMotor.setPower    (speed);
        RightDriveMotor.setPower   (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower    (0);
        RightDriveMotor.setPower   (0);

    }

    public boolean motorsBusy(){

        return (RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget(){

        int lDif = (LeftDriveMotor.getTargetPosition() - LeftDriveMotor.getCurrentPosition());
        int rDif = (RightDriveMotor.getTargetPosition() - RightDriveMotor.getCurrentPosition());

        return ((Math.abs(lDif) <= 10) & (Math.abs(rDif) <= 10));

    }

}
