package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BasicChasieHardware;

@Autonomous(name = "Today's Autonomous", group="Auto")

public class TodaysAuto extends LinearOpMode {

    BasicChasieHardware robot = new BasicChasieHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            robot.rightDriveBack.setPower(-1);
            robot.rightDriveFront.setPower(-1);
            robot.leftDriveBack.setPower(-1);
            robot.leftDriveFront.setPower(-1);
            sleep(2000);
            robot.rightDriveBack.setPower(0);
            robot.rightDriveFront.setPower(0);
            robot.leftDriveBack.setPower(0);
            robot.leftDriveFront.setPower(0);
            sleep(99999999);
        }
    }
}
