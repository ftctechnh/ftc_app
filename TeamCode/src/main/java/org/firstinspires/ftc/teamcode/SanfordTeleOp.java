package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Sanford's TeleOp", group="Nullbot")
public class SanfordTeleOp extends MainTeleOp {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = true;
        super.runOpMode();
    }
}