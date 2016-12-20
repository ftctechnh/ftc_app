package org.steelhead.ftc;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Closet_PC on 9/12/2016.
 */
public class MotorSupport {

    private DcMotor dcMotor = null;

    public MotorSupport() {

    }

    public MotorSupport(DcMotor motor) {
        dcMotor = motor;
    }

    public void motorRamp(double maxPower) {
        ElapsedTime delay = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        double power = 0;
        while(power < maxPower) {
            power = (0.002*delay.milliseconds());
            dcMotor.setPower(power);
        }
    }
}
