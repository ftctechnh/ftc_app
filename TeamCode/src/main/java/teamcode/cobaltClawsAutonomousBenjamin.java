package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "cobaltClawsAutonomousBenjamin", group = "Linear OpMode")

public class cobaltClawsAutonomousBenjamin extends LinearOpMode {

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

    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {


            
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

}
