package org.firstinspires.ftc.teamcode.systems.marker;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

public class Marker extends System {
    private Servo servo;

    public Marker(OpMode opMode) {
        super(opMode, "marker");
        this.servo = map.servo.get("marker");
    }

    public void place() {
        servo.setPosition(0.0);
    }

    public void reset() {
        servo.setPosition(90.0);
    }

}

