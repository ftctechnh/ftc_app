package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by guberti on 11/16/2017.
 */

@Autonomous(name="FRONT BLUE block placement", group="Testing")
public class TestBlockPlacement extends CompleteAutonomous {
    //NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        robot.color = Alliance.BLUE;

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.closeBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.6);
        robot.lift.setTargetPosition(-2500);
        waitForStart();

        for (DcMotor m : robot.motorArr) { m.setPower(0.35); }

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - ROTATION);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - ROTATION);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - ROTATION);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - ROTATION);
        log("Moving to correct plane...");

        waitUntilMovementsComplete();

        int driveDist = 1520;

        // 1000 ticks left equals roughly 25 CM
        // We need 13 more CM than where 1000 ticks puts us
        // So our initial position will be 1520 ticks
        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - driveDist);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + driveDist);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + driveDist);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - driveDist);

        waitUntilMovementsComplete();

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + 840);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + 840);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + 840);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + 840);

        waitUntilMovementsComplete();
        robot.lift.setTargetPosition(0);

        robot.sleep(1000);
        robot.openBlockClaw();
        robot.sleep(500);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - ROTATION/2);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - ROTATION/2);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - ROTATION/2);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - ROTATION/2);

        waitUntilMovementsComplete();
    }
}
