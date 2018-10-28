package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "cobaltClawsArmTest", group = "Linear OpMode")

public class cobaltClawsArmTest extends LinearOpMode{


    //Declares servo and motors
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor ArmMotor; //motor 0

    private Servo ArmServoElbow;
    private Servo ArmServoWrist;
    private Servo GrabberServo;

    //establishes and sets starting motor positions
    int armInitialPosition = 0; //guessed limit
    int armMaximumPosition = 420; //guessed limit

    final double TICKS_PER_DEGREE = 4.67;

    int armPosition;


    boolean leftGrabberOpen;
    boolean rightGrabberOpen;

    @Override
    public void runOpMode() {

        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){


            //Arm System

            if (gamepad1.left_bumper) {

                //Turns the servo so that the left grabber opens, waits until the left bumper is
                // pressed again, and then moves back to original position
                if(!leftGrabberOpen) {

                    GrabberServo.setPosition(0);

                } else if(leftGrabberOpen){

                    GrabberServo.setPosition(0.5);

                }

                leftGrabberOpen = !leftGrabberOpen;

            } else if (gamepad1.right_bumper) {

                //Turns the servo so that the right grabber opens, waits until the right bumper is
                // pressed again, and then moves back to original position
                if(!rightGrabberOpen) {

                    GrabberServo.setPosition(1);

                } else if(rightGrabberOpen){

                    GrabberServo.setPosition(0.5);

                }

                rightGrabberOpen = !rightGrabberOpen;

            }

            if (gamepad1.y) {

                //Goes to top position and length
                quickArm("deposit");

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

                ArmServoWrist.setPosition(this.ArmServoWrist.getPosition() + 0.05);

            }else if (gamepad1.dpad_down) {

                //Moves the arm down until the d-pad is changed/released or the arm hit the lower
                // limit
                ArmServoWrist.setPosition(this.ArmServoWrist.getPosition() - 0.05);

            }

            if(gamepad1.right_stick_y != 0){

                //Sets motor to work with encoder and speed
                ArmMotor.setMode((DcMotor.RunMode.RUN_TO_POSITION));

                //Moves the arm up or down until the right stick is no longer being moved vertically
                // or the arm hits the upper or lower limit
                if(armPosition >= armInitialPosition
                        && armPosition <= armMaximumPosition ){

                    int verticalRightStick = (int) gamepad1.right_stick_y;

                    ArmMotor.setTargetPosition(armPosition + verticalRightStick);
                    this.ArmMotor.setPower(0.5);
                    while (ArmMotor.isBusy());
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
        this.ArmMotor = hardwareMap.get (DcMotor.class, "ArmMotor");
        this.ArmServoWrist = hardwareMap.get (Servo.class, "ArmServoWrist");
        this.ArmServoElbow = hardwareMap.get (Servo.class, "ArmServoElbow");
        this.GrabberServo = hardwareMap.get (Servo.class, "GrabberServo");


        //Sets correct directions for motors and servos
        ArmMotor.setDirection(DcMotor.Direction.FORWARD);

        ArmServoWrist.setDirection(Servo.Direction.FORWARD);
        ArmServoElbow.setDirection(Servo.Direction.REVERSE);

        GrabberServo.setDirection(Servo.Direction.FORWARD);



        //Sets arm motors to work with position
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets grabber servos to closed
        this.GrabberServo.setPosition(0.5);
        leftGrabberOpen = false;
        rightGrabberOpen = false;

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
            //arm is vertical
            ArmServoWrist.setPosition(0);
            ArmServoElbow.setPosition(0);
            ArmMotor.setTargetPosition(armMaximumPosition);
            ArmMotor.setPower(0.5);

        }

        else if(position == "hang"){
            // arm is retracted onto itself
            ArmServoWrist.setPosition(0.9);
            ArmServoElbow.setPosition(0.9);

            ArmMotor.setTargetPosition(0);
            ArmMotor.setPower(0.5);

        }

        else if(position == "pickUp"){
            // arm is extended and pointed towards the ground
            ArmServoWrist.setPosition(0);
            ArmServoElbow.setPosition(0);

            ArmMotor.setTargetPosition(0);
            ArmMotor.setPower(0.5);

        }



        //Re-grabs encoder positions for future reference
        armPosition = ArmMotor.getCurrentPosition();

    }
}
