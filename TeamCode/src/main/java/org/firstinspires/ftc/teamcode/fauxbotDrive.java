package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by jodasue on 10/28/17.
 * Majorly edited by prestonraab on 1/22/19
 */
@TeleOp(name = "fauxbotDrive", group = "Testing")
public class fauxbotDrive extends LinearOpMode {

    Bogg robot;

    public void runOpMode() {
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Fauxbot);
        Gamepad g1 = gamepad1;

        waitForStart();
        while (opModeIsActive()) {

            robot.manualCurvy(g1.left_stick_button, g1.left_stick_x, g1.left_stick_y, g1.right_stick_x);

            telemetry.update();
            idle();
        }
    }
}
