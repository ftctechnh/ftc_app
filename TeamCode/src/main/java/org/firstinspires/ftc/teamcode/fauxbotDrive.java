package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by jodasue on 10/28/17.
 * Majorly edited by prestonraab on 1/22/19
 */
@TeleOp(name = "fauxbotDrive", group = "Testing")
public class fauxbotDrive extends LinearOpMode {

    Bogg robot;

    public void runOpMode() {
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Fauxbot);

        waitForStart();
        while (opModeIsActive()) {

            robot.manualCurvy(gamepad1, gamepad1);

            telemetry.update();
            idle();
        }
    }
}
