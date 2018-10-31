package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "cobaltClawsBaseProgramBenjamin", group = "Linear OpMode")

public class cobaltClawsBaseProgramBenjamin extends LinearOpMode{


    //Declares servo and motors
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor; //motor 0
    private DcMotor RightDriveMotor; //motor 1
    private DcMotor ArmMotor; //motor 2
    private DcMotor HangMotor; //motor 3

    private Servo ArmServoElbow; //servo 2
    private Servo ArmServoWrist; //servo 1
    private Servo GrabberServo; //servo 0

    //establishes and sets starting motor positions
    int armInitialPosition = 0; //guessed limit
    int armMaximumPosition = 600; //guessed limit

    final double TICKS_PER_DEGREE = 4.67;

    int armPosition;

    boolean leftGrabberOpen;
    boolean rightGrabberOpen;

    boolean hangArmUp;

    double wristTimer;
    double grabberTimer;

    double delayMS = 100;

    public enum quickArmSet { deposit, hang, pickUp, }


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
            }


            this.LeftDriveMotor.setPower(leftPower);
            this.RightDriveMotor.setPower(rightPower);

            //Arm System

            if (gamepad1.left_bumper) {

                //Turns the servo so that the left grabber opens, waits until the left bumper is
                // released, and then moves back to original position
                GrabberServo.setPosition(0.5);

                while(gamepad1.left_bumper) {

                }

                GrabberServo.setPosition(0);

            } else if (gamepad1.right_bumper) {

                //Turns the servo so that the right grabber opens, waits until the right bumper is
                // released, and then moves back to original position
                GrabberServo.setPosition(-0.5);

                while(gamepad1.right_bumper) {

                }

                GrabberServo.setPosition(0);

            }

            if (gamepad1.y) {

                //Goes to top position and length
                quickArm(quickArmSet.deposit);

            }else if (gamepad1.b) {

                //Goes to hang position and length
                quickArm(quickArmSet.hang);

            }else if (gamepad1.a) {

                //Goes to movement position and hang
                quickArm(quickArmSet.pickUp);

            }

            if(gamepad1.x){

                if(hangArmUp){

                    HangMotor.setTargetPosition(this.HangMotor.getCurrentPosition() - 600);

                } else HangMotor.setTargetPosition(this.HangMotor.getCurrentPosition() + 600);

                hangArmUp = !hangArmUp;

            }



            if (gamepad1.dpad_up) {

                //Moves the arm up until the d-pad is changed/released or the arm hits the upper
                // limit
                while(gamepad1.dpad_up && (this.ArmServoWrist.getPosition()
                        + this.ArmServoElbow.getPosition() < 1)){


                    ArmServoWrist.setPosition(this.ArmServoWrist.getPosition() + 0.05);
                    ArmServoElbow.setPosition(this.ArmServoElbow.getPosition() + 0.05);


                }

            }else if (gamepad1.dpad_down) {

                //Moves the arm down until the d-pad is changed/released or the arm hit the lower
                // limit
                while(gamepad1.dpad_down && (this.ArmServoWrist.getPosition()
                        + this.ArmServoElbow.getPosition() > -1)){


                    ArmServoWrist.setPosition(this.ArmServoWrist.getPosition() - 0.05);
                    ArmServoElbow.setPosition(this.ArmServoElbow.getPosition() - 0.05);


                }

            }

            if(gamepad1.right_stick_y != 0){

                //Sets motor to work with encoder and speed
                ArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm up or down until the right stick is no longer being moved vertically
                // or the arm hits the upper or lower limit
                while(gamepad1.right_stick_y != 0 && armPosition >= armInitialPosition
                        && armPosition <= armMaximumPosition ){

                    int verticalRightStick = (int) gamepad1.right_stick_y;

                    ArmMotor.setTargetPosition(armPosition + verticalRightStick);
                    this.ArmMotor.setPower(0.5);
                    armPosition = this.ArmMotor.getCurrentPosition();

                }

            }




            //Gives stats and updates
            telemetry.addData("Status", "Running");
            telemetry.addData("Arm Position", armPosition);
            telemetry.update();
        }
    }


    private void initialize(){

        //giving internal hardware an external name for the app config
        this.LeftDriveMotor = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get (DcMotor.class, "RightDriveMotor");
        this.ArmMotor = hardwareMap.get (DcMotor.class, "ArmMotor");
        this.HangMotor = hardwareMap.get (DcMotor.class, "HangMotor");
        this.ArmServoWrist = hardwareMap.get (Servo.class, "ArmServoWrist");
        this.ArmServoElbow = hardwareMap.get (Servo.class, "ArmServoElbow");
        this.GrabberServo = hardwareMap.get (Servo.class, "GrabberServo");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        ArmMotor.setDirection(DcMotor.Direction.FORWARD);
        HangMotor.setDirection(DcMotor.Direction.FORWARD);

        ArmServoElbow.setDirection(Servo.Direction.FORWARD);
        ArmServoWrist.setDirection(Servo.Direction.FORWARD);

        GrabberServo.setDirection(Servo.Direction.FORWARD);



        //Sets arm motors to work with position
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets drive motors to work without position
        RightDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LeftDriveMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hangArmUp = false;

        //Sets grabber servos to closed
        this.GrabberServo.setPosition(0.5);

        //Gets the current arm motor positions so driver can make sure motors are properly
        // calibrated.
        armPosition = this.ArmMotor.getCurrentPosition();

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.addData("Arm Position: ", armPosition);
        telemetry.update();
    }

    public void quickArm(quickArmSet position){

        //Sets motor to work with position
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Goes to specified position and length
        if(position == quickArmSet.deposit){
            //arm is vertical
            ArmServoWrist.setPosition(0);
            ArmServoElbow.setPosition(0.5);
            ArmMotor.setTargetPosition(armMaximumPosition);
            ArmMotor.setPower(0.5);

        }

        else if(position == quickArmSet.hang){
            // arm is retracted onto itself
            ArmServoWrist.setPosition(0.9);
            ArmServoElbow.setPosition(0.9);

            ArmMotor.setTargetPosition(0);
            ArmMotor.setPower(0.5);

        }

        else if(position == quickArmSet.pickUp){
            // arm is extended and pointed towards the ground
            ArmServoWrist.setPosition(0);
            ArmServoElbow.setPosition(0.5);

            ArmMotor.setTargetPosition(0);
            ArmMotor.setPower(0.5);

        }



        //Re-grabs encoder positions for future reference
        armPosition = ArmMotor.getCurrentPosition();

    }
}
