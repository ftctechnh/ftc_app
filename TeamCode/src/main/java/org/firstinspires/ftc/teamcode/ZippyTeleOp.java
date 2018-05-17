package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Zippy TeleOp", group="Nullbot")
public class ZippyTeleOp extends MainTeleOp {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        accelTime = 1;
        super.runOpMode();
    }
}