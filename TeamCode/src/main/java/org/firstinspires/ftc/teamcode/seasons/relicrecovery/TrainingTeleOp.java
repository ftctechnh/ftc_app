package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 *
 * Created by daniel on 11/12/17.
 */
@Disabled
@TeleOp(name = "Teleopp", group = "teleop")
public class TrainingTeleOp extends LinearOpMode {

    private DcMotor right;
    private DcMotor left;
    @Override
    public void runOpMode() throws InterruptedException {
        //set deadzones
        gamepad1.setJoystickDeadzone(.02f);

        //initiate motors

      left = hardwareMap.dcMotor.get("l");
      right = hardwareMap.dcMotor.get("r");

        waitForStart();
        double speedLeft;
        double speedRight;

        while (opModeIsActive()) {
            speedLeft = gamepad1.left_stick_x;
            speedRight = gamepad1.right_stick_x;

            left.setPower(speedLeft);
            right.setPower(speedRight);

        }

    }
}
