package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Kaden on 2/20/18.
 */
@Autonomous(name = "testing auto glyphs stuff", group = "test")
public class AutoGlyphsTest extends LinearOpMode {
    AutoGlyphs cv;
    Phone phone;
    public void runOpMode() throws InterruptedException {
        phone = new Phone(hardwareMap, telemetry);
        cv = new AutoGlyphs(hardwareMap, telemetry);
        waitForStart();
        phone.getMark();
        sleep(2000);
        cv.enable();
        while (opModeIsActive()) {
            telemetry.addData("X pos", cv.getXOffset());
            telemetry.addData("Y Pos", cv.getYPos());
            telemetry.update();
        }
    }

}
