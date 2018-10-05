package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "cobaltClawsBaseProgramBenjamin", group = "Linear OpMode")

public class cobaltClawsBaseProgramBenjamin extends LinearOpMode{

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

        while(opModeIsActive()){


            //Drive System

            //double is a variable type that supports decimals
            double leftPower;
            double rightPower;
            //double drive = gamepad1.left_stick_y;
            /*double turn = gamepad1.left_stick_x;




            if (drive !=0){
                leftPower = (-drive);
                rightPower = (-drive);
                //if drive is not 0, set both motor powers to the value of drive
            }
            else if (turn !=0){
                leftPower = (drive - turn);
                rightPower = (drive + turn);
                //if the drive if reports false, it runs this turn block
            }
            else {
                leftPower = 0;
                rightPower = 0;
                //if neither of these if statements report true it sets the motor powers to 0
                // if you don't push a stick, the robot don't move
            }
            this.LeftDriveMotor.setPower(leftPower);
            this.RightDriveMotor.setPower(rightPower);
            */

            if (gamepad1.left_stick_y > 0 && gamepad1.left_stick_x <= 0.5 &&
                    gamepad1.left_stick_x >= -0.5){

                leftPower = 0.8;
                rightPower = 0.8;
                //If left joystick is in the upper hemisphere and center, drive forwards

            } else if (gamepad1.left_stick_y < 0 && gamepad1.left_stick_x <= 0.5 &&
                    gamepad1.left_stick_x >= -0.5){

                leftPower = -0.8;
                rightPower = -0.8;
                //If left joystick is in the lower hemisphere and center, drive backwards

            } else if (gamepad1.left_stick_x > 0 && gamepad1.left_stick_y <= 0.5 &&
                    gamepad1.left_stick_y >= -0.5){

                leftPower = 0.8;
                rightPower = 0.0;
                //If left joystick is in the right hemisphere and right-center, turn right

            } else if (gamepad1.left_stick_x < 0 && gamepad1.left_stick_y <= 0.5 &&
                    gamepad1.left_stick_y >= -0.5){

                leftPower = 0.0;
                rightPower = 0.8;
                //If left joystick is in the left hemisphere and left-center, turn left

            } else {
                leftPower = 0;
                rightPower = 0;
                // if the left joystick is in neutral, stop motors
                // if you don't push a stick, the robot don't move
            }


            this.LeftDriveMotor.setPower(leftPower);
            this.RightDriveMotor.setPower(rightPower);

            //Arm System

            if (gamepad1.left_bumper) {
                if (leftGrabberOpen = false) {

                    this.GrabberServoLeft.setPosition(0.5);
                    leftGrabberOpen = true;
                    //If grabber is closed, open grabber

                } else if (leftGrabberOpen = true) {

                    this.GrabberServoLeft.setPosition(0);
                    leftGrabberOpen = false;
                    //If grabber is open, close grabber

                }
            } else if (gamepad1.right_bumper) {
                if (rightGrabberOpen = false) {

                    this.GrabberServoRight.setPosition(0.5);
                    rightGrabberOpen = true;
                    //If grabber is closed, open grabber

                } else if (rightGrabberOpen = true) {

                    this.GrabberServoRight.setPosition(0);
                    rightGrabberOpen = false;
                    //If grabber is open, close grabber

                }
            }

            if (gamepad1.y) {

                //Goes to top position and length
                //quickArm();

            }else if (gamepad1.b) {

                //Goes to hang position and length
                //quickArm();

            }else if (gamepad1.a) {

                //Goes to movement position and hang
                //quickArm();

            }



            if (gamepad1.dpad_up) {
                //Sets motor to work with encoder and speed
                ExtendArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm up until the d-pad is changed/released or the arm hits the upper
                // limit
                while(gamepad1.left_bumper && extendMotorPosition <= 90){

                    extendMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    ExtendArmMotor.setTargetPosition(extendMotorPosition + 5);
                    this.ExtendArmMotor.setPower(0.5);

                }

            }else if (gamepad1.dpad_down) {
                //Sets motor to work with encoder and speed
                ExtendArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm down until the d-pad is changed/released or the arm hit the lower
                // limit
                while(gamepad1.right_bumper || extendMotorPosition >= 0 ){

                    extendMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    ExtendArmMotor.setTargetPosition(extendMotorPosition - 5);
                    this.ExtendArmMotor.setPower(-0.5);


                }

            }

            if (gamepad1.right_stick_y >= 0) {
                //Sets motor to work with encoder and speed
                SwingArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm up until the left trigger is released or the arm hits the upper limit
                while(gamepad1.right_stick_y > 0 || swingMotorPosition <= 90){

                    swingMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    SwingArmMotor.setTargetPosition(swingMotorPosition + 5);
                    this.SwingArmMotor.setPower(gamepad1.right_stick_y);

                }

            } else if (gamepad1.right_stick_y <= 0) {
                //Sets motor to work with encoder and speed
                SwingArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm down until the right bumper is released or the arm hits the lower limit
                while(gamepad1.right_stick_y < 0 || swingMotorPosition <= 0 ){

                    swingMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    SwingArmMotor.setTargetPosition(swingMotorPosition - 5);
                    this.ExtendArmMotor.setPower(-gamepad1.right_stick_y);

                }

            }

            /*

            if (gamepad1.right_stick_x >= 0) {

                //Rotates the arm right until the left trigger is released or the arm hits the upper limit
                while(gamepad1.right_stick_x > 0 || this.RotateArmServo.getPosition() <= 90){

                    this.RotateArmServo.setPosition(this.RotateArmServo.getPosition() + 0.05);

                }

            } else if (gamepad1.right_stick_x <= 0) {

                //Rotates the arm left until the right bumper is released or the arm hits the lower limit
                while(gamepad1.right_stick_x < 0 || this.RotateArmServo.getPosition() <= -90 ){

                    this.RotateArmServo.setPosition(this.RotateArmServo.getPosition() - 0.05);

                }

            }

             */


            //Gives stats and updates
            telemetry.addData("Status", "Running");
            telemetry.addData("Arm Position", extendMotorPosition);
            telemetry.addData("Arm Length", swingMotorPosition );
            telemetry.update();
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

    public void quickArm(int position, int length){

        //Sets motor to work with position
        SwingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ExtendArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Goes to specified position and length
        SwingArmMotor.setTargetPosition(position);
        SwingArmMotor.setPower(1.0);
        ExtendArmMotor.setTargetPosition(length);
        ExtendArmMotor.setPower(1.0);

        //Re-grabs encoder positions for future reference
        swingMotorPosition = SwingArmMotor.getCurrentPosition();
        extendMotorPosition = ExtendArmMotor.getCurrentPosition();

    }
}
