package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by E-6420 on 11/28/2017.
 */
@TeleOp(name = "7519Teleop", group = "7519")
public class Team7519Teleop extends LinearOpMode{

    private CRServo testServo;
    private DcMotor motorLift, leftFront, rightFront, leftRear, rightRear;
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


        //Include any code to run only once here
        waitForStart();

        while(opModeIsActive())
        {

            //Include commands to run on controller presses here

            //Mecanum Drive
            double hypot = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;

            final double leftFrontPower = hypot * Math.cos(robotAngle) + gamepad1.right_stick_x;
            final double rightFrontPower = hypot * Math.sin(robotAngle) - gamepad1.right_stick_x;
            final double leftRearPower = hypot * Math.sin(robotAngle) + gamepad1.right_stick_x;
            final double rightRearPower = hypot * Math.cos(robotAngle) - gamepad1.right_stick_x;

            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftRear.setPower(leftRearPower);
            rightRear.setPower(rightRearPower);

            //Manual Lift Control w/Trigger
            motorLift.setPower(gamepad1.right_trigger);
            motorLift.setPower(-gamepad1.left_trigger);

            //Claw Control (A-Move Claw, B-Switch Direction)
            if (gamepad1.a)
                testServo.setPower(1);
            if (gamepad1.b)
                testServo.setPower(-1);
            testServo.setPower(0);

//            while (gamepad1.a==false){
//                testServo.setPower(0);
//                if (gamepad1.b==true) {
//                    if (clawPosition == 0) {
//                        testServo.setDirection(CRServo.Direction.REVERSE);
//                        clawPosition++;
//                        sleep(300);
//                    }
//                    else  {
//                        testServo.setDirection(CRServo.Direction.FORWARD);
//                        clawPosition--;
//                        sleep(300);
//                    }
//                }//end if
//            }//end loop
//            while (gamepad1.a) {
//                testServo.setPower(1);

            }//end loop

            idle();

        }//end while(opModeIsActive)


    }//end runOpMode
}//end class
