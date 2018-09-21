package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "kobaltKlawsBaseProgramBenjamin", group = "Linear OpMode")

public class kobaltKlawsBaseProgramBenjamin extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private Servo servo1;
    private Servo servo2;

    //establishes and sets starting motor positions
    double extendMotorPosition = this.motor3.getCurrentPosition();
    double swingMotorPosition = this.motor4.getCurrentPosition();

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
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            double grabberPosition;




            if (drive !=0){
                leftPower = (-drive/2);
                rightPower = (-drive/2);
                //if drive is not 0, set both motor powers to the value of drive
            }
            else if (turn !=0){
                leftPower = (drive - (turn/2));
                rightPower = (drive + (turn/2));
                //if the drive if reports false, it runs this turn block
            }
            else {
                leftPower = 0;
                rightPower = 0;
                //if neither of these if statements report true it sets the motor powers to 0
                // if you don't push a stick, the robot don't move
            }
            this.motor1.setPower(leftPower);
            this.motor2.setPower(rightPower);

            if (gamepad1.x) {
                grabberPosition = this.servo1.getPosition();
                if (grabberPosition == 0) {
                    this.servo1.setPosition(0.5);
                    this.servo2.setPosition(0.5);
                    //If grabber is closed, open grabber
                } else if (grabberPosition == 0.5) {
                    this.servo1.setPosition(0);
                    this.servo2.setPosition(0);
                    //If grabber is open, close grabber
                }
            }

            if (gamepad1.y) {
                //Sets motor to work with position
                motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*Goes to top position

                motor4.setTargetPosition();
                motor4.setPower(1.0);

                */
            }

            if (gamepad1.b) {
                //Sets motor to work with position
                motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*Goes to middle or hang position

                motor4.setTargetPosition();
                motor4.setPower(1.0);

                */
            }

            if (gamepad1.a) {
                //Sets motor to work with position
                motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                /*Goes to bottom position
                motor4.setTargetPosition();
                motor4.setPower(1.0);

                */
                swingMotorPosition = motor4.getCurrentPosition();
            }

            if (gamepad1.left_bumper) {
                //Sets motor to work with encoder and speed
                motor3.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

                //Moves the arm up until the left bumper is released or the arm hits the upper limit
                /*while(gamepad1.left_bumper || extendMotorPosition >= [upper limit]){
                    this.motor3.setPower(1.0);
                    swingMotorPosition = this.motor4.getCurrentPosition
                    [something to prevent runtime errors]
                }
                this.motor3.setPower(0);

                */

            }

            if (gamepad1.right_bumper) {
                //Sets motor to work with encoder and speed
                motor3.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

                //Moves the arm down until the right bumper is released or the arm hits the lower limit
                /*while(gamepad1.right_bumper || extendMotorPosition <= [lower limit] ){
                    this.motor3.setPower(-1.0);
                    swingMotorPosition = this.motor4.getCurrentPosition
                    [something to prevent runtime errors]
                }
                this.motor3.setPower(0);

                */

            }

            if (gamepad1.left_trigger >= 0) {
                //Sets motor to work with encoder and speed
                motor4.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

                //Moves the arm up until the left trigger is released or the arm hits the upper limit
                /*while(gamepad1.left_bumper || swingMotorPosition >= [upper limit]){
                    this.motor4.setPower(gamepad1.left_trigger);
                    swingMotorPosition = this.motor4.getCurrentPosition
                    [something to prevent runtime errors]
                }
                this.motor4.setPower(0);

                */

            }

            if (gamepad1.right_trigger >= 0) {
                //Sets motor to work with encoder and speed
                motor4.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

                //Moves the arm down until the right bumper is released or the arm hits the lower limit
                /*while(gamepad1.right_trigger || swingMotorPosition <= [lower limit] ){
                    this.motor3.setPower(-1.0);
                    swingMotorPosition = this.motor4.getCurrentPosition
                    [something to prevent runtime errors]
                }
                this.motor4.setPower(0);

                */

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
        this.motor1 = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.motor2 = hardwareMap.get (DcMotor.class, "RightDriveMotor");
        this.motor3 = hardwareMap.get (DcMotor.class, "ArmMotorExtend");
        this.motor4 = hardwareMap.get (DcMotor.class, "ArmMotorSwing");
        this.servo1 = hardwareMap.get (Servo.class, "GrabberServoLeft");
        this.servo2 = hardwareMap.get (Servo.class, "GrabberServoRight");

        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);
        motor3.setDirection(DcMotor.Direction.FORWARD);
        motor4.setDirection(DcMotor.Direction.FORWARD);

        servo1.setDirection(Servo.Direction.FORWARD);
        servo2.setDirection(Servo.Direction.REVERSE);

        this.servo1.setPosition(0);
        this.servo2.setPosition(0);


        //Sets arm motors to work with position
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets motors to starting hang positions
        //motor3.setTargetPosition();
        //motor3.setPower(1.0);
        //motor4.setTargetPosition();
        //motor4.setPower(1.0);


        //TO DO: Figure out how to set reference arm position and length

        telemetry.addData("Status", "Online");
        telemetry.addData("Arm Position", extendMotorPosition);
        telemetry.addData("Arm Length", swingMotorPosition);
        telemetry.update();
    }
}
