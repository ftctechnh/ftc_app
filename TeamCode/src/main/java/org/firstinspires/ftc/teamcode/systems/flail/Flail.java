package org.firstinspires.ftc.teamcode.systems.flail;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

public class Flail extends System {
    private DcMotor flailMotor;

    public Flail(OpMode opMode) {
        super (opMode,"flail");
        flailMotor = map.dcMotor.get("flail");
    }

    public void start() {
        flailMotor.setPower(1);
    }

    public void stop() {
        flailMotor.setPower(0);
    }
}

