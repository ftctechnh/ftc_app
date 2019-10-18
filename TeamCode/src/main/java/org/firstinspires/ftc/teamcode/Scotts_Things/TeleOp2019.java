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
        Float max = null;


        waitForStart();

        while (opModeIsActive()) {

            Float drive = - gamepad1.left_stick_y;
            Float turn = gamepad1.right_stick_y;

            leftPower = drive + turn;
            rightPower = drive - turn;

            max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            if (max > 1.0)
            {
                leftPower /= max;
                rightPower /= max;
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
