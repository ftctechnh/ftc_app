package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by guberti on 11/2/2017.
 */

@TeleOp(name="Henry's TeleOp", group="Nullbot")
public class HenryTeleOp extends MainTeleOp {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        super.runOpMode();
    }
}