package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by guberti on 11/4/2017.
 */

@Autonomous(name="FRONT RED parking", group="Autonomous")
@Disabled
public class CompRedFrontParking extends CompRedGemOnlyAutonomous {
    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);

        super.runOpMode();

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.2);
        }

        robot.frontLeft.setTargetPosition(3150);
        robot.backLeft.setTargetPosition(3150);
        robot.frontRight.setTargetPosition(3150);
        robot.backRight.setTargetPosition(3150);

        robot.sleep(3000);

        robot.frontLeft.setTargetPosition(robot.frontLeft.getTargetPosition() - 1680);
        robot.backLeft.setTargetPosition(robot.backLeft.getTargetPosition() + 1680);
        robot.frontRight.setTargetPosition(robot.frontRight.getTargetPosition() + 1680);
        robot.backRight.setTargetPosition(robot.backRight.getTargetPosition() - 1680);

        robot.sleep(3000);

        robot.openBlockClaw();
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.2);
        robot.lift.setTargetPosition(0);
        robot.sleep(2000);
    }
}
