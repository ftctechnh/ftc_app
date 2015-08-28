package com.fellowshipoftheloosescrews.utilities;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Thomas on 8/13/2015.
 *
 * Makes a Dc motor act like a servo, so we can set its position.
 * This will constantly track the position of the motor.
 *
 * NOTE: ONLY WORKS WITH NEW CONTROLLERS
 */
public class DcServo {
    private DcMotor motor;
    private PID pidController;

    private Thread jobThread;
    private ServoJob servoJob;

    private double motorEncoderCPR;
    private int sleepMs = 10;

    private int offset = 0;

    public DcServo(DcMotor motor, double cpr) {
        this.motor = motor;
        motorEncoderCPR = cpr;
        pidController = new PID(1, 0.5, 1, 0);
        calibrate(0);
    }

    public synchronized void calibrate(double position)
    {
        int currentCPR = motor.getCurrentPosition();
        int positionCPR = (int)position * (int)motorEncoderCPR;
        offset = currentCPR - positionCPR;
        pidController.reset();
    }

    public void start()
    {
        servoJob = new ServoJob();
        jobThread = new Thread(servoJob);
        jobThread.start();
    }

    public void stop()
    {
        servoJob.stop();
        Log.d("dcservo", "Stopping");
        motor.setPower(0);
    }

    public void setTarget(double target)
    {
        pidController.setTarget(target);
    }

    public class ServoJob implements Runnable
    {
        public boolean isRunning = false;

        @Override
        public void run() {
            isRunning = true;

            while(isRunning)
            {
                double currentPosition = (motor.getCurrentPosition() - offset) / motorEncoderCPR;
                double pidOutput = pidController.calculate(currentPosition);
                motor.setPower(Range.clip(pidOutput, -1, 1));

                Log.d("error", "" + pidController.getError());

                try {
                    Thread.sleep(sleepMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop()
        {
            isRunning = false;
        }
    }
}
