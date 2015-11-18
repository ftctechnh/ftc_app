package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Ryan on 11/17/2015.
 */
public class DragonoidsTest extends DragonoidsOpMode {
    @Override
    public void init() {
        super.init();
    }
    @Override
    public void loop() {
        climbMotors.get("right").setPower(0.1);
        climbMotors.get("left").setPower(0.1);

        super.loop();
    }
}
