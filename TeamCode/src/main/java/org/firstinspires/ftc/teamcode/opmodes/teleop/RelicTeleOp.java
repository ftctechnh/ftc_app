package org.firstinspires.ftc.teamcode.opmodes.teleop;

import org.firstinspires.ftc.teamcode.opmodes.RelicBaseOp;

/**
 * Created by Derek on 12/7/2017.
 */

public class RelicTeleOp extends RelicBaseOp {
    @Override
    public void init() {
        super.init();

    }

    @Override
    public void loop() {
        super.loop();
        driveInfo.setFromNorm();
    }
}
