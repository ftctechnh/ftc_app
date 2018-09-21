package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "kobaltKlawsBaseProgramBenjamin", group = "Linear OpMode")

public class kobaltKlawsBaseProgramBenjamin extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor;
    private DcMotor RightDriveMotor;
    private DcMotor ExtendArmMotor;
    private DcMotor SwingArmMotor;
    //private Servo RotateArmServo;
    private Servo GrabberServoLeft;
    private Servo GrabberServoRight;

    //establishes and sets starting motor positions
    int extendMotorPosition = this.ExtendArmMotor.getCurrentPosition();
    int swingMotorPosition = this.SwingArmMotor.getCurrentPosition();

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
            double drive = -gamepad1.left_stick_y / 2;
            double turn = gamepad1.left_stick_x / 2;

            double grabberPosition;




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

            //TO DO: Change so servos work independently, with bumpers. Use booleans.
            if (gamepad1.x) {
                grabberPosition = this.GrabberServoLeft.getPosition();
                if (grabberPosition == 0) {
                    this.GrabberServoLeft.setPosition(0.5);
                    this.GrabberServoRight.setPosition(0.5);
                    //If grabber is closed, open grabber
                } else if (grabberPosition == 0.5) {
                    this.GrabberServoLeft.setPosition(0);
                    this.GrabberServoRight.setPosition(0);
                    //If grabber is open, close grabber
                }
            }

            //TO DO: Refactor preset positions so a single method is used for all three
            if (gamepad1.y) {
                //Sets motor to work with position
                SwingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*Goes to top position

                SwingArmMotor.setTargetPosition();
                SwingArmMotor.setPower(1.0);

                */
                swingMotorPosition = SwingArmMotor.getCurrentPosition();
            }

            if (gamepad1.b) {
                //Sets motor to work with position
                SwingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*Goes to middle or hang position

                SwingArmMotor.setTargetPosition();
                SwingArmMotor.setPower(1.0);

                */
                swingMotorPosition = SwingArmMotor.getCurrentPosition();
            }

            if (gamepad1.a) {
                //Sets motor to work with position
                SwingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*Goes to bottom position
                SwingArmMotor.setTargetPosition();
                SwingArmMotor.setPower(1.0);

                */
                swingMotorPosition = SwingArmMotor.getCurrentPosition();
            }

            //TO DO: Change extending to work with vertical d-pad
            if (gamepad1.left_bumper) {
                //Sets motor to work with encoder and speed
                ExtendArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm up until the left bumper is released or the arm hits the upper limit
                /*while(gamepad1.left_bumper || extendMotorPosition <= [upper limit]){
                    this.ExtendArmMotor.setPower(1.0);
                    extendMotorPosition = this.SwingArmMotor.getCurrentPosition
                    [something to prevent runtime errors]
                }
                this.ExtendArmMotor.setPower(0);

                */

            }

            if (gamepad1.right_bumper) {
                //Sets motor to work with encoder and speed
                ExtendArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm down until the right bumper is released or the arm hits the lower limit
                /*while(gamepad1.right_bumper || extendMotorPosition >= 0 ){

                    this.ExtendArmMotor.setPower(-0.5);
                    extendMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    //[something to prevent runtime errors]
                }
                this.ExtendArmMotor.setPower(0);

                */

            }

            if (gamepad1.left_trigger >= 0) {
                //Sets motor to work with encoder and speed
                SwingArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm up until the left trigger is released or the arm hits the upper limit
                while(gamepad1.left_trigger > 0 || swingMotorPosition <= 90){

                    swingMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    SwingArmMotor.setTargetPosition(swingMotorPosition + 5);
                    this.SwingArmMotor.setPower(gamepad1.left_trigger);

                }



            } else if (gamepad1.right_trigger >= 0) {
                //Sets motor to work with encoder and speed
                SwingArmMotor.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

                //Moves the arm down until the right bumper is released or the arm hits the lower limit
                while(gamepad1.right_trigger > 0 || swingMotorPosition <= 0 ){

                    swingMotorPosition = this.SwingArmMotor.getCurrentPosition();
                    SwingArmMotor.setTargetPosition(swingMotorPosition - 5);
                    this.ExtendArmMotor.setPower(-gamepad1.right_trigger);

                }



            }


            //Gives stats and updates
            telemetry.addData("Status", "Running");
            telemetry.addData("Arm Position", extendMotorPosition);
            telemetry.addData("Arm Length", swingMotorPosition );
            telemetry.update();
        }
    }


    private void initialize(){

        //giving internal hardware an external name for the app config
        //also initializing the hardware?
        this.LeftDriveMotor = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get (DcMotor.class, "RightDriveMotor");
        this.ExtendArmMotor = hardwareMap.get (DcMotor.class, "ExtendArmMotor");
        this.SwingArmMotor = hardwareMap.get (DcMotor.class, "SwingArmMotor");
        this.GrabberServoLeft = hardwareMap.get (Servo.class, "GrabberServoLeft");
        this.GrabberServoRight = hardwareMap.get (Servo.class, "GrabberServoRight");

        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        ExtendArmMotor.setDirection(DcMotor.Direction.FORWARD);
        SwingArmMotor.setDirection(DcMotor.Direction.FORWARD);

        GrabberServoLeft.setDirection(Servo.Direction.FORWARD);
        GrabberServoRight.setDirection(Servo.Direction.REVERSE);

        this.GrabberServoLeft.setPosition(0);
        this.GrabberServoRight.setPosition(0);


        //Sets arm motors to work with position
        ExtendArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SwingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets motors to starting hang positions
        //ExtendArmMotor.setTargetPosition();
        //ExtendArmMotor.setPower(1.0);
        //SwingArmMotor.setTargetPosition();
        //SwingArmMotor.setPower(1.0);


        //TO DO: Figure out how to set reference arm position and length

        telemetry.addData("Status", "Online");
        telemetry.addData("Arm Position", extendMotorPosition);
        telemetry.addData("Arm Length", swingMotorPosition);
        telemetry.update();
    }
}
