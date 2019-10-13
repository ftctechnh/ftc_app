package org.firstinspires.ftc.teamcode.Scotts_Things;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Scotts_Things.HardwareFile2019;

import static org.firstinspires.ftc.teamcode.Scotts_Things.HardwareFile2019.*;

@TeleOp(name = "TeleOpTesting 2069")
@Disabled
public class TeleOp2019 extends LinearOpMode {

    HardwareFile2019 robot = new HardwareFile2019();

    @Override
    public void runOpMode() throws InterruptedException {

        robot.mapHardware(hardwareMap);

        telemetry.addData("Welcome Driver(s)", "Hardware Mapping Complete");
        telemetry.update();

        Float leftPower = null;
        Float rightPower = null;

        waitForStart();

        while (opModeIsActive()) {

            leftPower = gamepad1.left_stick_y;
            rightPower = gamepad1.left_stick_y;

            if (gamepad1.left_stick_x > .25) {

                leftPower = Math.abs(gamepad1.left_stick_y);
                rightPower = -1 * Math.abs(gamepad1.left_stick_y);

            } else if (gamepad1.left_stick_x < .25) {

                leftPower = -1 * Math.abs(gamepad1.left_stick_y);
                rightPower = Math.abs(gamepad1.left_stick_y);

            }

            frontLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
            backLeft.setPower(leftPower);
            backRight.setPower(rightPower);

            telemetry.addData("Motors", "Left Power: (%.2f), Right Power: (%.2f)", leftPower, rightPower);
            telemetry.update();

            idle();
        }

    }

}
