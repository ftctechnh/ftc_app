package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Slippery TeleOp", group="Nullbot")
public class SlipperyTeleOp extends MainTeleOp {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        accelTime = 2000;
        super.runOpMode();
    }
}