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
            telemetry.addData("Wheel Diameter", robot.getOptionsMap().get("wheelDiam"));
            telemetry.addData("Inside Wheel Gearing Ratio", robot.getOptionsMap().get("wheelRatIn"));
            telemetry.addData("Outside Wheel Gearing Ratio", robot.getOptionsMap().get("wheelRatOut"));
            telemetry.addData("Jewel Arm Retract Pos", robot.getOptionsMap().get("jewelKnockerRetract"));
            telemetry.addData("Jewel Arm Extend Pos", robot.getOptionsMap().get("jewelKnockerExtend"));
            telemetry.addData("Relic Grip Open Pos", robot.getOptionsMap().get("relicGripOpen"));
            telemetry.addData("Relic Grip Close Pos", robot.getOptionsMap().get("relicGripClose"));
            telemetry.addData("HDrive Power Min", robot.getOptionsMap().get("hDrivePowerMin"));
            telemetry.addData("HDrive Power Max", robot.getOptionsMap().get("hDrivePowerMax"));
            telemetry.update();
        }
    }
}