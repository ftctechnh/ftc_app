package com.fellowshipoftheloosescrews.testing;

import com.fellowshipoftheloosescrews.utilities.Util;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Thomas on 8/22/2015.
 */
public class ThreadedMotorTesting extends OpMode
{
    DcMotor motor;

    MotorRunnable r;
    Thread t;

    @Override
    public void init() {
        // gets the motor from the hardware map
        motor = hardwareMap.dcMotor.get("motor1");

        // starts a new thread
        r = new MotorRunnable();
        t = new Thread(r);
    }

    @Override
    public void start()
    {
        t.start();
        r.isRunning = false;
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop()
    {
        r.isRunning = false;
    }

    public class MotorRunnable implements Runnable
    {
        public boolean isRunning = false;

        @Override
        public void run() {
            isRunning = true;

            while(isRunning)
            {
                motor.setTargetPosition((int) Util.ENCODER_NEVEREST_CPR);
                //motor.setPower(-0.5);
                delayThread(1000);
                motor.setTargetPosition(0);
                delayThread(1000);
            }
        }
        void delayThread(long ms)
        {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
