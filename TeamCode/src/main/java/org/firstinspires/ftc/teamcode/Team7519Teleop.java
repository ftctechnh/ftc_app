package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by E-6420 on 11/28/2017.
 */
@TeleOp(name = "7519Teleop", group = "7519")
public class Team7519Teleop extends LinearOpMode{

    private CRServo testServo;
    private DcMotor motorLift;
    int clawPosition=0;
    @Override
    public void runOpMode() throws InterruptedException
    {

        //Declare hardwareMap here, example below
        motorLift = hardwareMap.dcMotor.get("motorLift");
        testServo = hardwareMap.crservo.get("testServo");


        //Include any code to run only once here
        waitForStart();

        while(opModeIsActive())
        {

            //Include commands to run on controller presses here

            //Manual Lift Control w/Trigger
            if (gamepad1.left_trigger > 0){
                motorLift.setDirection(DcMotorSimple.Direction.REVERSE);
                motorLift.setPower (gamepad1.left_trigger);
            }//end if
            if (gamepad1.right_trigger > 0){
                motorLift.setDirection(DcMotorSimple.Direction.FORWARD);
                motorLift.setPower (gamepad1.right_trigger);
            }//end if

            //Claw Control (A-Move Claw, B-Switch Direction)
            while (gamepad1.a==false){
                testServo.setPower(0);
                if (gamepad1.b==true) {
                    if (clawPosition == 0) {
                        testServo.setDirection(CRServo.Direction.REVERSE);
                        clawPosition++;
                        sleep(300);
                    }
                    else  {
                        testServo.setDirection(CRServo.Direction.FORWARD);
                        clawPosition--;
                        sleep(300);
                    }
                }//end if
            }//end loop
            while (gamepad1.a) {
                testServo.setPower(1);

            }//end loop

            idle();

        }//end while(opModeIsActive)


    }//end runOpMode
}//end class
