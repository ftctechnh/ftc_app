package org.firstinspires.ftc.teamcode.PIDTesting;

import com.qualcomm.hardware.motors.NeveRest40Gearmotor;

/**
 * Created by guberti on 10/21/2017.
 */

public abstract class PIDTestInterface {

    PIDTestInterface() {}
    PIDTestInterface(NeveRest40Gearmotor m) {}
    public abstract void setPower(double d);
}
