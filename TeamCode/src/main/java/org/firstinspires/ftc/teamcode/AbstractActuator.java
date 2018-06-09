package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class AbstractActuator {

    public AbstractActuator(){

    }

    /* Initialize standard Hardware interfaces */
    public abstract void init(HardwareMap ahwMap);

    public abstract void stop();
}
