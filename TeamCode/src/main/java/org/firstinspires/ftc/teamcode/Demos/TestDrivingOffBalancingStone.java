package org.firstinspires.ftc.teamcode.Demos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CompleteOldAutonomous;
import org.firstinspires.ftc.teamcode.NullbotHardware;

/**
 * Created by guberti on 1/9/2018.
 */
@Autonomous(name="DEMO Drive off stone", group="Demo")
public class TestDrivingOffBalancingStone extends CompleteOldAutonomous {
    NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        waitForStart();

        while (opModeIsActive()) {
            for (DcMotor m : robot.motorArr) {
                m.setTargetPosition(5000);
                m.setPower(0.5);
            }
        }

        waitUntilMovementsComplete();

    }
}