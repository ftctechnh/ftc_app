package org.firstinspires.ftc.teamcode.SubAssembly.Claimer;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Utilities.GamepadWrapper;

@TeleOp(name = "Claimer Test", group = "Test")
public class ClaimerTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Claimer Test");

        ClaimerControl Claimer = new ClaimerControl();

        GamepadWrapper egamepad1 = new GamepadWrapper(gamepad1);
        GamepadWrapper egamepad2 = new GamepadWrapper(gamepad2);

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            Claimer.init(hardwareMap);

            egamepad1.updateEdge();
            egamepad2.updateEdge();


            if (egamepad1.a.state) {
                
            }
        }
    }
}
