package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
public abstract class VoltageBase extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    //Declare Motors
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor liftMotor;

    //Declare Servos
    public Servo mineralArm;

    //variables or any other data you will use later here
    public int inchConstant = 1; //if you are using encoders on your drivewheels, change this to the ratio of ticks to inches.
    public int encoderTicksperRevConstant = 288;
    public int degConstant = 1;  //and this to the ratio between ticks and turn degrees.
    public int thingsInBot = 0; //currently not used for anything.
    public boolean RobotIsGoingForwards = true;
    public int mineralPosition = 0;
    public boolean hanging = true;
    public int randomChangeSoICanPush = 7;
    public abstract void DefineOpMode();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        //put all initializing stuff here.  hardwaremaps, starting settings for motors and servos, etc.
        //Map the Motors
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");

        //Map the Servos
        mineralArm = hardwareMap.get(Servo.class, "mineralArm");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD); //Check motors, but left and right should be the same in this case
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);

        // Don't let motors move if they're not supposed to
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //If Using Encoders
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Some of the servos are flipped, too
        mineralArm.setDirection(Servo.Direction.REVERSE); //Check

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        DefineOpMode(); //I moved waitforstart inside defineopmode to make autonomous easier.

        //Check
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }



    }

    /** POV Mode uses left stick to go forward, and right stick to turn.
     // - This uses basic math to combine motions and is easier to drive straight.
     double drive = -gamepad1.left_stick_y;
     double turn  =  gamepad1.right_stick_x;
     leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
     rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
     **/

    //Here is a set of methods for everything the robot needs to do.  These can be used anywhere.

    //Utility Methods

    public void DriveMotors(int speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(speed);
    }

    public void ChangeDirection() {
        leftDrive.getMode();
        if (leftDrive.getDirection() == DcMotorSimple.Direction.FORWARD
                && rightDrive.getDirection() == DcMotorSimple.Direction.FORWARD) {
            leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            RobotIsGoingForwards = false;
        } else {
            leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
            RobotIsGoingForwards = true;
        }
    }


    //Movement Methods

    //Tank Drive
    public void DriveForwardsOrBackwards(int speed) {
        DriveMotors(speed);
    }

    public void TurnLeft(int speed) {
        leftDrive.setPower(-speed);
        rightDrive.setPower(speed);
    }

    public void TurnRight(int speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(-speed);
    }

    public void StopDriving() {
        DriveMotors(0);
    }

    public void DriveForwardsDistance(int speed, int inches) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(inches / inchConstant);
        rightDrive.setTargetPosition(inches / inchConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {
            //Wait until the target position is reached
        }
        //Stop and change modes back to normal
        StopDriving();
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void DriveBackwardsDistance(int speed, int inches) {
        ChangeDirection();

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(inches / inchConstant);
        rightDrive.setTargetPosition(inches / inchConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {
            //Wait until the target position is reached
        }

        StopDriving();

        if (RobotIsGoingForwards == true) {

        } else {
            ChangeDirection();
        }

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnLeftDegrees(int speed, int degrees) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(-degrees / degConstant);
        rightDrive.setTargetPosition(degrees / degConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);


        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnRightDegrees(int speed, int degrees) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition(degrees / degConstant);
        rightDrive.setTargetPosition(-degrees / degConstant);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //Arm Methods

    public void extendHook() {
        if (gamepad2.b = true) {
            liftMotor.setPower(-0.8);
            //Fix this – Thread.sleep(2000);
            liftMotor.setPower(0);
        }

    }

    public void contractHook() {
        if (gamepad2.a = true) {
            liftMotor.setPower(0.8);
            //Fix this – Thread.sleep(2000);
            liftMotor.setPower(0);
        }
    }

}