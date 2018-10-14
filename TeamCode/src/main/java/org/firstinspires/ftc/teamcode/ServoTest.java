package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by Shreyas on 10/7/18.
 */
@TeleOp
public class ServoTest extends LinearOpMode {
    CRServo spinnerR;
    CRServo spinnerL;

    @Override
    public void runOpMode() throws InterruptedException {
        spinnerR = hardwareMap.crservo.get("spinnerR");
        spinnerL = hardwareMap.crservo.get("spinnerL");

        waitForStart();

        while(opModeIsActive()){
//            spinnerL.setPower((gamepad1.left_trigger - gamepad2.right_trigger));
//            spinnerR.setPower(-(gamepad1.left_trigger - gamepad2.right_trigger));
            spinnerL.setPower(1);
            spinnerR.setPower(-1);`
            telemetry.update();

        }






    }
}
