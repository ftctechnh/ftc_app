package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by Stephen Ogden on 10/9/17.
 * FTC 6128 | 7935
 * FRC 1595
 */
@Disabled
@TeleOp(name = "Two servo", group = "Test")
public class twoservotest extends LinearOpMode {
    @Override
    public void runOpMode() {
        CRServo servo1 = hardwareMap.crservo.get("servo1");
        CRServo servo2 = hardwareMap.crservo.get("servo2");
        telemetry.addData("Status", "Press Start to run program.");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            servo2.setPower(1);
            servo1.setPower(1);
            telemetry.addData("Status", "Running...");
            telemetry.update();
        }
        telemetry.addData("Status", "Done.");
        telemetry.update();
    }
}
