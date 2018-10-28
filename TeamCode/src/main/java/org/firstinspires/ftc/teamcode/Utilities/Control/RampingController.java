package org.firstinspires.ftc.teamcode.Utilities.Control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guberti on 5/11/2018.
 */

public class RampingController {
    final static int MS_BETWEEN_UPDATES = 1;
    RampingDcMotor[] rampMotors;
    ElapsedTime timeSinceLastUpdate;
    Timer scheduler;

    public RampingController(DcMotor[] motorArr, double accelTime) {
        rampMotors = new RampingDcMotor[motorArr.length];
        for (int i = 0; i < motorArr.length; i++) {
            rampMotors[i] = new RampingDcMotor(motorArr[i], accelTime);
        }
        timeSinceLastUpdate = new ElapsedTime();
        scheduler = new Timer();
        scheduler.schedule(new RampTask(), 0, MS_BETWEEN_UPDATES);
    }

    public void setMotorPowers(double[] powers) {
        if (powers.length != rampMotors.length) {
            throw new NumberFormatException("Incorrect number of motor powers given!");
        }

        for (int i = 0; i < powers.length; i++) {
            rampMotors[i].setPower(powers[i]);
        }
    }

    public void quit() {
        scheduler.cancel();
        scheduler.cancel();
    }

    class RampTask extends TimerTask {
        public void run() {
            double msSinceUpdate = timeSinceLastUpdate.milliseconds();
            timeSinceLastUpdate.reset();
            for (RampingDcMotor rampMotor : rampMotors) {
                rampMotor.ramp(msSinceUpdate);
            }
        }
    }
}
