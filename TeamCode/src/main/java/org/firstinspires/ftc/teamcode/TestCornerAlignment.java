package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by guberti on 11/16/2017.
 */

@Autonomous(name="FRONT BLUE corner align", group="Testing")
public class TestCornerAlignment extends CompleteAutonomous {
    //NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        robot.color = Alliance.BLUE;

        waitForStart();

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) { m.setPower(0.35); }

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + (int) (ROTATION * 3.5));
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + (int) (ROTATION * 3.5));
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition());
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition());

        log("Ramming wall...");

        waitUntilMovementsComplete();

        //turnToPos(0);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - 800);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - 800);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - 800);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - 800);

        waitUntilMovementsComplete();
        // Move into corner

        robot.backLeft.setPower(0.75);
        robot.backRight.setPower(0);
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0.75);

        // Up and right five wheel rotations
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + (int) (ROTATION * 3.5));
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition());
        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition());
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + (int) (ROTATION * 3.5));

        log("Ramming corner...");

        waitUntilMovementsComplete();
    }
}
