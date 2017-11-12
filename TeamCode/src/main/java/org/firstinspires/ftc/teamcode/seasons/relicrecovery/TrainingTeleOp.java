package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 *
 * Created by daniel on 11/12/17.
 */

public class TrainingTeleOp extends LinearOpMode {

    private DcMotor fLeft;
    private DcMotor bLeft;
    private DcMotor fRight;
    private DcMotor bRight;
    @Override
    public void runOpMode() throws InterruptedException {
        //set deadzones
        gamepad1.setJoystickDeadzone(.02f);

        //initiate motors
        fLeft = hardwareMap.dcMotor.get("fl");
        fRight = hardwareMap.dcMotor.get("fr");
        bLeft = hardwareMap.dcMotor.get("bl");
        bRight = hardwareMap.dcMotor.get("bl");

        waitForStart();
        double speedLeft;
        double speedRight;

        while (opModeIsActive()) {
            speedLeft = gamepad1.left_stick_x;
            speedRight = gamepad1.right_stick_x;

            fLeft.setPower(speedLeft);
            bLeft.setPower(speedLeft);
            fRight.setPower(speedRight);
            bRight.setPower(speedRight);

        }

    }
}