package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "7518Teleop", group = "7518")
public class Team7518Teleop extends LinearOpMode{

    private DcMotor  leftFront, rightFront, leftRear, rightRear, rightLift, leftLift, rightIntake, leftIntake;
    private Servo rightFlip, leftFlip, colorSensorServo;

    boolean lift;

    @Override
    public void runOpMode() throws InterruptedException
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Declare hardwareMap here, example below
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap.dcMotor.get("leftLift");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");
        leftIntake = hardwareMap.dcMotor.get("leftIntake");

        rightFlip = hardwareMap.servo.get("rightFlip");
        leftFlip = hardwareMap.servo.get("leftFlip");
        colorSensorServo = hardwareMap.servo.get("colorSensorServo");


        //Include any code to run only once here
        waitForStart();

        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLift.setPower(.25);

        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLift.setPower(.25);

        int flipIteration = 0;


        while(opModeIsActive())
        {




                       //Keep Color Sensor Upright
                        colorSensorServo.setPosition(0);


                        //Mecanum Drive
                        double h = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
                        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

                        final double leftFrontPower = h * Math.sin(robotAngle) - gamepad1.right_stick_x;
                        final double rightFrontPower = h * Math.cos(robotAngle) + gamepad1.right_stick_x;
                        final double leftRearPower = h * Math.cos(robotAngle) - gamepad1.right_stick_x;
                        final double rightRearPower = h * Math.sin(robotAngle) + gamepad1.right_stick_x;

                        leftFront.setPower(leftFrontPower);
                        rightFront.setPower(-rightFrontPower);
                        leftRear.setPower(leftRearPower);
                        rightRear.setPower(-rightRearPower);


                        //Intake Wheels
                        if (gamepad1.left_trigger>0){
                            leftIntake.setPower(gamepad1.left_trigger);
                            rightIntake.setPower(-gamepad1.left_trigger);
                        }//end if
                        else if (gamepad1.right_trigger>0){
                            leftIntake.setPower(-gamepad1.right_trigger);
                            rightIntake.setPower(gamepad1.right_trigger);
                        }//end else if
                        else{
                            leftIntake.setPower(0);
                            rightIntake.setPower(0);
                        }//end else


                        //lift
                        if(gamepad1.left_bumper==true){
                            lift=false;
                            sleep(250);
                        }//end if
                        else if (gamepad1.right_bumper==true){
                            lift=true;
                            sleep(250);
                        }//end else if
                        setLift(lift);
                        telemetry.addData("rightLift:\t", rightLift.getCurrentPosition());
                        telemetry.addData("leftLift:\t", leftLift.getCurrentPosition());
                        telemetry.update();

//                        while(gamepad1.dpad_up){
//                            rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//                            rightLift.setPower(.25);
//                        }
//                        while(gamepad1.dpad_down){
//                            rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//                            rightLift.setPower(-.25);
//                        }
//                        while(gamepad1.dpad_right){
//                            leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//                            leftLift.setPower(.25);
//                        }
//                        while(gamepad1.dpad_left){
//                            leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//                            leftLift.setPower(-.25);
//                        }
//                        leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                        leftLift.setPower(.25);
//
//                        rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                        rightLift.setPower(.25);


                        //manual flipper adjustment
                        if(gamepad1.x){
                            setFlip(0);
                            flipIteration=0;
                        }//end if
                        else if(gamepad1.y){
                            setFlip(0.64);
                            flipIteration=2;
                        }//end else if
                        else if(gamepad1.a){
                            setFlip(0.75);
                            flipIteration=1;
                        }//end else if


                        //automatic flipper adjustment
                        if(gamepad1.b && flipIteration==0){
                            setFlip(0.75);
                            flipIteration++;
                        }//end if
                        else if (gamepad1.b && flipIteration==1){
                            setFlip(0.64);
                            flipIteration++;
                        }//end else if
                        else if (gamepad1.b && flipIteration==2){
                            setFlip(0);
                            flipIteration = 0;
                        }//end else if

        }//end while(opModeIsActive)


    }//end runOpMode

    public void setFlip(double position){
        rightFlip.setPosition(1-position);
        leftFlip.setPosition(position);
        sleep(250);
    }//end setFlip

    public void setLift(boolean up){
        if(up){
            rightLift.setTargetPosition(-11000);
            leftLift.setTargetPosition(-11000);
        }//end if
        else{
            rightLift.setTargetPosition(0);
            leftLift.setTargetPosition(0);
        }//end else
    }//end setLift


}//end class
