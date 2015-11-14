package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.HashMap;

public class DragonoidsAuto extends DragonoidsOpMode {
    @Override
    public void init() {
        super.init();
    }
    @Override
    public void loop() {
        driveMotors.get("right").setPower(0.5);
        driveMotors.get("left").setPower(0.5);

        super.loop();
    }
}