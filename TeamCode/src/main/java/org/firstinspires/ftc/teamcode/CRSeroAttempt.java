package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by E-6420 on 10/31/2017.
 */
@TeleOp(name = "servo", group = "benprojects")
public class CRSeroAttempt extends LinearOpMode {
private CRServo testServo;
int x=0;

    @Override//required statement
    public void runOpMode() throws InterruptedException{
        testServo = hardwareMap.crservo.get("testServo");

        waitForStart();

        while(opModeIsActive()){
            while (gamepad1.dpad_down==false){
                testServo.setPower(0);
                if (gamepad1.dpad_up==true) {
                    if (x == 0) {
                        testServo.setDirection(CRServo.Direction.REVERSE);
                        x++;
                        sleep(300);
                    }
                    else  {
                        testServo.setDirection(CRServo.Direction.FORWARD);
                        x--;
                        sleep(300);
                    }
                }//end if
            }//end loop
            while (gamepad1.dpad_down) {
                testServo.setPower(1);

            }//end loop


        }//end loop
    }//end runOpMode

}//end class
