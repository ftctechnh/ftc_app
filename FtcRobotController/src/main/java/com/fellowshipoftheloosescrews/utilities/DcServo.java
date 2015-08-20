package com.fellowshipoftheloosescrews.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Thomas on 8/13/2015.
 *
 * Makes a Dc motor act like a servo, so we can set its position.
 * This will constantly track the position of the motor.
 *
 * TODO: See if it works with the old controllers?
 */
public class DcServo {
    private DcMotor motor;
    private PID pidController;

    private Thread jobThread;
    private Runnable servoJob;

    private boolean isRunning;

    private double motorEncoderCPR;
    private int sleepMs = 10;

    public DcServo(DcMotor motor, double cpr) {
        this.motor = motor;
        motorEncoderCPR = cpr;
        pidController = new PID(1, 1, 1, 0);
    }

    public void start()
    {
        servoJob = new ServoJob();
        jobThread = new Thread(servoJob);
        isRunning = true;
        jobThread.start();
    }

    public void stop()
    {
        isRunning = false;
    }

    public void setTarget(double target)
    {
        pidController.setTarget(target);
    }

    public class ServoJob implements Runnable
    {
        @Override
        public void run() {
            while(isRunning)
            {
                double currentPosition = motor.getCurrentPosition() / motorEncoderCPR;
                double pidOutput = pidController.calculate(currentPosition);
                motor.setPower(Range.clip(pidOutput, -1, 1));

                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
