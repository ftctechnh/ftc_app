package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by E-6420 on 11/28/2017.
 */
@TeleOp(name = "7518Teleop", group = "7518")
public class Team7518Teleop extends LinearOpMode{

    private DcMotor  leftFront, rightFront, leftRear, rightRear, absoluteRight, absoluteLeft, yAxis, zAxis;
    private Servo topLeftServo, topRightServo, bottomLeftServo, bottomRightServo;
    int clawPosition=0;
    @Override
    public void runOpMode() throws InterruptedException
    {

        //Declare hardwareMap here, example below
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        absoluteRight=hardwareMap.dcMotor.get("absoluteRight");
        absoluteLeft=hardwareMap.dcMotor.get("absoluteLeft");
        yAxis=hardwareMap.dcMotor.get("yAxis");
        zAxis=hardwareMap.dcMotor.get("zAxis");
        topRightServo=hardwareMap.servo.get("topRightServo");
        topLeftServo=hardwareMap.servo.get("topLeftServo");
        bottomLeftServo=hardwareMap.servo.get("bottomLeftServo");
        bottomRightServo=hardwareMap.servo.get("bottomRightServo");


        //Include any code to run only once here
        waitForStart();

        while(opModeIsActive())
        {

                        //Include commands to run on controller presses here

                        //Mecanum Drive
                        double h = Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y);
                        double robotAngle = Math.atan2(gamepad1.right_stick_y, gamepad1.right_stick_x) - Math.PI / 4;

                        final double leftFrontPower = h * Math.cos(robotAngle) + gamepad1.left_stick_y;
                        final double rightFrontPower = h * Math.sin(robotAngle) - gamepad1.left_stick_y;
                        final double leftRearPower = h * Math.sin(robotAngle) + gamepad1.left_stick_y;
                        final double rightRearPower = h * Math.cos(robotAngle) - gamepad1.left_stick_y;

                        leftFront.setPower(leftFrontPower);
                        rightFront.setPower(rightFrontPower);
                        leftRear.setPower(leftRearPower);
                        rightRear.setPower(rightRearPower);

                        //write code above idel

                        //green wheels

                        absoluteLeft.setPower(gamepad1.right_trigger);
                        absoluteRight.setPower(-gamepad1.right_trigger);
                        absoluteRight.setPower(gamepad1.left_trigger);
                        absoluteLeft.setPower(-gamepad1.left_trigger);

                        //foldable doors
                          while (gamepad1.a){
                              topRightServo.setPosition(.7);
                              topLeftServo.setPosition(.3);
                              bottomRightServo.setPosition(.3);
                              bottomLeftServo.setPosition(.7);
                          }

                          while (gamepad1.b) {
                              topRightServo.setPosition(1);
                              topLeftServo.setPosition(0);
                              bottomRightServo.setPosition(0);
                              bottomLeftServo.setPosition(1);
                          }


                        //z-axis motor
                        if (gamepad1.dpad_left)
                            zAxis.setPower(1);
                        if (gamepad1.dpad_right)
                            zAxis.setPower(-1);
                        else zAxis.setPower(0);

                        //y-axis motor
                        if (gamepad1.x)
                            yAxis.setPower(1);
                        if (gamepad1.y)
                            yAxis.setPower(-1);
                        else yAxis.setPower(0);

                        //color sensor


                        idle();

        }//end while(opModeIsActive)


    }//end runOpMode
}//end class
