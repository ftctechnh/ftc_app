package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by root on 2/20/18.
 */
@TeleOp(name="testing auto glyphs stuff", group="test")
public class AutoGlyphsTest extends OpMode{
    AutoGlyphs cv = new AutoGlyphs(hardwareMap, telemetry);
    public void init() {
        cv.enable();
    }
    public void loop() {
        telemetry.addData("X pos", cv.getXOffset());
        telemetry.addData("Y Pos", cv.getYOffset());
        telemetry.update();
    }

}
