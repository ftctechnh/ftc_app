package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Preston on 10/28/17.
 */
@TeleOp(name = "fauxbot", group = "Testing")
public class fauxbotGlyphTwoServos extends LinearOpMode {

    DcMotor left;
    DcMotor right;
    Servo pincherL;
    Servo pincherR;

    public void runOpMode() {

        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        double temp = 0;

        pincherL = hardwareMap.servo.get("pincherL");
        pincherR = hardwareMap.servo.get("pincherR");
        double pinch= 1;
        pincherL.setPosition(pinch);
        pincherR.setPosition(pinch);

        waitForStart();
        while (opModeIsActive()) {

            if(gamepad1.right_bumper == true) {
                pinch += .05;
                if (pinch > .7)
                {pinch = .7;}
                pincherL.setPosition(pinch);
                pincherR.setPosition(pinch);
            }
            else if(gamepad1.right_trigger > 0) {
                pinch -= .05;
                if (pinch < 0)
                {pinch = 0;}
                pincherL.setPosition(pinch);
                pincherR.setPosition(pinch);
            }



            telemetry.addData("pinch: ", pinch);
            telemetry.update();
            idle();
        }
    }
}
