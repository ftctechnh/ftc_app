package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This class is a simple testing program to test any code on.
 */
@TeleOp(name = "Test Op", group = "test")
public class TestOp extends LinearOpMode {
    private RelicRecoveryRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        this.robot = new RelicRecoveryRobot(this);

        waitForStart();
        telemetry.addData("Status", "TestOp Running");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addData("JsonData", robot.getOptionsMap().get("wheelDiam"));
            telemetry.update();
        }
    }
}