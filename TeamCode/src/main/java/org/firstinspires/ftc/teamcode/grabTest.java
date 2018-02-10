package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by shivaan on 10/02/18.
 */

@TeleOp(name = "grabTest", group = "test")

public class grabTest extends LinearOpMode {

    private DcMotor grabMotor;

    double pos;

    @Override
    public void runOpMode() throws InterruptedException {

        //Grabber
        grabMotor = hardwareMap.get(DcMotor.class, "GDC");

        grabMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.clear();

        waitForStart();

        while (opModeIsActive()) {

            pos = grabMotor.getCurrentPosition();
            telemetry.addData("position:", pos);
            telemetry.update();

            if (gamepad1.dpad_up && !gamepad1.dpad_down){
                grabMotor.setPower(0.2);
            }

            if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                grabMotor.setPower(-0.2);
            }

        }
    }
}
