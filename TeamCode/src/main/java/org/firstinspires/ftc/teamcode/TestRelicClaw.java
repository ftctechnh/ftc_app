package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Test relic claw", group="Diagnostics")
public class TestRelicClaw extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        waitForStart();

        while (opModeIsActive()) {
            robot.openRelicClaw();
            robot.sleep(1000);
            robot.closeRelicClaw();
            robot.sleep(1000);
        }
    }
}
