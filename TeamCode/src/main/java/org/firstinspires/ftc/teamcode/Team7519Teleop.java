package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Team 7519 Relic Recovery Code
 * Ryan Gniadek and Ben Bernstein
 */
@TeleOp(name = "7519Teleop", group = "7519")
public class Team7519Teleop extends LinearOpMode{

    private CRServo testServo;
    private DcMotor motorLift, leftFront, rightFront, leftRear, rightRear;
    private Servo arm;
    int clawPosition=0;
    @Override
    public void runOpMode() throws InterruptedException
    {

        //Declare hardwareMap here, example below
        motorLift = hardwareMap.dcMotor.get("motorLift");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        testServo = hardwareMap.crservo.get("testServo");
        arm = hardwareMap.servo.get("arm");


        //Include any code to run only once here
        waitForStart();

        while(opModeIsActive())
        {

            arm.setPosition(0);//Lock Servo Arm in Up Position

            //Include commands to run on controller presses here

            //Mecanum Drive
            double h = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

            final double leftFrontPower = h * Math.cos(robotAngle) + gamepad1.right_stick_x;
            final double rightFrontPower = h * Math.sin(robotAngle) - gamepad1.right_stick_x;
            final double leftRearPower = h * Math.sin(robotAngle) + gamepad1.right_stick_x;
            final double rightRearPower = h * Math.cos(robotAngle) - gamepad1.right_stick_x;


            leftFront.setPower(leftFrontPower*.7);
            rightFront.setPower(-rightFrontPower*.7);
            leftRear.setPower(leftRearPower*.7);
            rightRear.setPower(-rightRearPower*.7);

            //Manual Lift Control w/Trigger
            while(gamepad1.right_trigger>0) {
                motorLift.setPower(gamepad1.right_trigger);
            }//end loop
            while (gamepad1.left_trigger>0){
                motorLift.setPower(-gamepad1.left_trigger);
            }//end loop
            motorLift.setPower(0);
            //Claw Control (A-Move Claw, B-Switch Direction)
            if (gamepad1.a)
                testServo.setPower(1);
            else if (gamepad1.b)
                testServo.setPower(-1);
            else
                testServo.setPower(0);

            idle();

        }//end while(opModeIsActive)


    }//end runOpMode
}//end class
