package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
public abstract class VoltageBase extends LinearOpMode{

    public ElapsedTime runtime = new ElapsedTime();
    //Declare Motors
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor liftMotor;

    //Declare Servos
    public Servo mineralArm;

    //Variables or any other data you will use later here
    public final static int Core_EncoderTicksperRevolution = (288/1);
    public final static int HD_EncoderTicksperRevolution = (2240/1);
    public final static double one_revolutionperInchDistanceofDrive = (1/0.7539822369); //60mm diameter to inches times pi = wheel circumference"

    public final static int stringInches = 6; //length of pulley string in inches //check
    public double one_RevolutionperInchDistanceofPulley = (1/1.5); //distance or circumference of string w/ each revolution //check
    public double liftTicksPerInch = (one_RevolutionperInchDistanceofPulley*HD_EncoderTicksperRevolution);
    public double driveTicksPerInch = (one_revolutionperInchDistanceofDrive*Core_EncoderTicksperRevolution);
    public final static int HD_EncoderExtendedPosition = stringInches*2240;

    public double driveDegreeToTicks = (1/360)*(Core_EncoderTicksperRevolution);  //and this to the ratio between ticks and turn degrees.
    public boolean RobotIsGoingForwards = true;
    public abstract void DefineOpMode();

    //Variables for servo mineral Arm
    public final static double mineralArm_Home = 0.99; //all the way down for servo
    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle    sleep(CYCLE_MS);
    static final double topPOS      =  0.0;     // Maximum rotational position
    static final double bottomPOS   =  1.0;     // Minimum rotational position
    static final double mineralReadyPOS =  0.1; // move to about 162 degrees.
    static final double mineralRaisedPOS =  0.8; // move to about 36 degrees.

    // Define class members
    double  mineralPosition = (bottomPOS - topPOS); // Start at bottom position
    boolean rampUp = true;

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

        // Don't move if they're not supposed to
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mineralArm.setPosition(mineralArm_Home);

        //If Using Encoders
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Encoder set to zero at complete contract position

        // Some of the servos are flipped, too
        mineralArm.setDirection(Servo.Direction.REVERSE); //Check

        // Wait for the game to start (driver presses PLAY)
        DefineOpMode();



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

    public void DriveMotors(double speed) {
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
    public void DriveForwardsOrBackwards(double speed) {
        DriveMotors(speed);
    }

    public void TurnLeft(double speed) {
        leftDrive.setPower(-speed);
        rightDrive.setPower(speed);
    }

    public void TurnRight(double speed) {
        leftDrive.setPower(speed);
        rightDrive.setPower(-speed);
    }

    public void StopDriving() {
        DriveMotors(0);
    }

    public void DriveForwardsDistance(double speed, double inches) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setTargetPosition((int)Math.round(inches*Core_EncoderTicksperRevolution));
        rightDrive.setTargetPosition((int)Math.round(inches*Core_EncoderTicksperRevolution));

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

    public void DriveBackwardsDistance(double speed, double inches) {
        ChangeDirection();

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setTargetPosition((int)Math.round(inches*Core_EncoderTicksperRevolution));
        rightDrive.setTargetPosition((int)Math.round(inches*Core_EncoderTicksperRevolution));

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

    public void TurnLeftDegrees(double speed, int degrees) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition( (int) Math.round (-degrees * driveDegreeToTicks)); //check
        rightDrive.setTargetPosition( (int) Math.round (degrees * driveDegreeToTicks));

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);


        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void TurnRightDegrees(double speed, int degrees) {
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setTargetPosition((int) Math.round (degrees * driveDegreeToTicks));
        rightDrive.setTargetPosition((int) Math.round (-degrees * driveDegreeToTicks));

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        DriveForwardsOrBackwards(speed);

        while (leftDrive.isBusy() && rightDrive.isBusy()) {

        }

        StopDriving();

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //Hook Arm Method

    public void StopHook() {
        liftMotor.setPower(0);
        }

    /*public void extendHook() {
            liftMotor.setPower(0.8);
            liftMotor.setTargetPosition(0); //check
            StopHook();
    }
    public void contractHook() {
            liftMotor.setPower(0.8);
            liftMotor.setTargetPosition(HD_EncoderExtendedPosition); //check
            StopHook();
    } */

    public void completeHookExtend(double speed_1, double inches) {
        liftMotor.setPower(-speed_1);
        //liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setTargetPosition((int)Math.round(inches * stringInches*one_RevolutionperInchDistanceofPulley*HD_EncoderTicksperRevolution)); //stringInches = length of string
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (liftMotor.isBusy()) {
            //Wait until the target position is reached
        }
        //Stop and change modes back to normal
        StopHook();
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void completeHookContract(double speed_1) {
        liftMotor.setPower(speed_1);
        //liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setTargetPosition(0); //Check
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (liftMotor.isBusy() ) {
            //Wait until the target position is reached
        }
        //Stop and change modes back to normal
        StopHook();
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //Mineral Arm Method
    public void mineralArmlowStop(){
        mineralArm.setPosition(.20);
    };
    public void mineralArmHighStop() {
        mineralArm.setPosition(.80);
    }

}