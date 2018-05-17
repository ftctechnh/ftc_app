package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by guberti on 11/2/2017.
 */
@TeleOp(name="Direct Drive TeleOp", group="Nullbot")
public class DirectTeleOp extends MainTeleOp {
    @Override
    public void runOpMode() {
        nonrelativeDriveModeEnabled = false;
        accelTime = 250;
        super.runOpMode();
    }
}