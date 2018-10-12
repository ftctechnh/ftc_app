package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

    private Servo ArmServoBottom;
    private Servo ArmServoTop;
    private Servo GrabberServo;

    //establishes and sets starting motor positions
    int armInitialPosition = 0; //guessed limit
    double armMaximumPosition = 600; //guessed limit

    int armPosition;


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

                //Turns the servo so that the right grabber opens, waits until the right bumber is
                // released, and then moves back to original position
                GrabberServo.setPosition(-0.5);

                while(gamepad1.right_bumper) {

                }

                GrabberServo.setPosition(0);

            }

            if (gamepad1.y) {

                //Goes to top position and length
                quickArm("top");

            }else if (gamepad1.b) {

                //Goes to hang position and length
                quickArm("hang");

            }else if (gamepad1.a) {

                //Goes to movement position and hang
                quickArm("pickUp");

            }



            if (gamepad1.dpad_up) {

                //Moves the arm up until the d-pad is changed/released or the arm hits the upper
                // limit
                while(gamepad1.dpad_up && (this.ArmServoTop.getPosition()
                        + this.ArmServoBottom.getPosition() < 1)){


                    ArmServoTop.setPosition(this.ArmServoTop.getPosition() + 0.05);
                    ArmServoBottom.setPosition(this.ArmServoBottom.getPosition() + 0.05);


                }

            }else if (gamepad1.dpad_down) {

                //Moves the arm down until the d-pad is changed/released or the arm hit the lower
                // limit
                while(gamepad1.dpad_down && (this.ArmServoTop.getPosition()
                        + this.ArmServoBottom.getPosition() > -1)){


                    ArmServoTop.setPosition(this.ArmServoTop.getPosition() - 0.05);
                    ArmServoBottom.setPosition(this.ArmServoBottom.getPosition() - 0.05);


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
        armPosition = this.ArmMotor.getCurrentPosition();

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.addData("Arm Position: ", armPosition);
        telemetry.update();
    }

    public void quickArm(String position){

        //Sets motor to work with position
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Goes to specified position and length
        if(position == "deposit"){

            ArmServoTop.setPosition(1);
            ArmServoBottom.setPosition(1);

            ArmMotor.setTargetPosition(90);
            ArmMotor.setPower(1.0);

        }

        else if(position == "hang"){

            ArmServoTop.setPosition(0.75);
            ArmServoBottom.setPosition(0.75);

            ArmMotor.setTargetPosition(75);
            ArmMotor.setPower(1.0);

        }

        else if(position == "pickUp"){

            ArmServoTop.setPosition(0);
            ArmServoBottom.setPosition(0);

            ArmMotor.setTargetPosition(0);
            ArmMotor.setPower(1.0);

        }



        //Re-grabs encoder positions for future reference
        armPosition = ArmMotor.getCurrentPosition();

    }
}
