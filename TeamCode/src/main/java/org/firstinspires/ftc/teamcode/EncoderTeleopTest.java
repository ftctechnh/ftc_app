package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Recharged Orange on 10/18/2018.
 */

@TeleOp(name = "Encoder Teleop Test")

public class EncoderTeleopTest extends superClass {

    public void runOpMode() {

        initialization(true);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.x && gamepad1.dpad_down) {
                leftBack.setPower(-1);


            }
            else if (gamepad1.x && gamepad1.dpad_up){
                leftBack.setPower(1);
            }

            if (gamepad1.y && gamepad1.dpad_down) {
                leftFront.setPower(-1);


            }
            else if (gamepad1.y && gamepad1.dpad_up){
                leftFront.setPower(1);
            }

            if (gamepad1.b && gamepad1.dpad_down) {
                rightBack.setPower(-1);


            }
            else if (gamepad1.b && gamepad1.dpad_up){
                rightBack.setPower(1);
            }

            if (gamepad1.a && gamepad1.dpad_down) {
               rightFront.setPower(-1);


            }
            else if (gamepad1.a && gamepad1.dpad_up){
                rightFront.setPower(1);
            }
            else {
                leftBack.setPower(-0);
                leftFront.setPower(0);
                rightFront.setPower(0);
                rightBack.setPower(0);
            }

            if (touchSensor.isPressed()){
                telemetry.addLine("touch Sensor is pressed");
            }
            else {
                telemetry.addLine("touch Sensor is not pressed");
            }

            telemetry.addData("leftBack", leftBack.getCurrentPosition());
            telemetry.addData("leftFront",leftFront.getCurrentPosition());
            telemetry.addData("rightBack", rightBack.getCurrentPosition());
            telemetry.addData("rightFront", rightFront.getCurrentPosition());

            telemetry.update();
        }


    }
}
