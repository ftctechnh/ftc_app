package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by shivaan on 10/02/18.
 */

@Disabled
@TeleOp(name = "grabTest", group = "test")

public class grabTest extends LinearOpMode {

    private DcMotor grabMotor;

    double pos;

    @Override
    public void runOpMode() throws InterruptedException {

        //Grabber
        grabMotor = hardwareMap.get(DcMotor.class, "GrabDC");

        grabMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.clear();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                grabMotor.setPower(gamepad2.right_trigger);
            }

            if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                grabMotor.setPower(-gamepad2.right_trigger);
            }

            if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                grabMotor.setPower(0);
            }
        }
    }
}
