package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by dimpledhawan on 2/13/16.
 */
public class EncoderMotorTask
{
    private String name;
    private DcMotor motor;
    private double power;
    private LinearOpMode opMode;

    private double targetEncoderValue;
    private boolean running;

    public EncoderMotorTask(LinearOpMode opMode, DcMotor motor)
    {
        this.opMode = opMode;
        this.motor = motor;
        //running = false;
    }


    public void startMotor(String name, double power, double targetEncoderValue, Direction direction) throws InterruptedException
    {
        resetEncoder();
        waitForEncoderToReset();

        running = true;
        this.targetEncoderValue = targetEncoderValue;

        double powerWithDirection = (direction == Direction.MOTOR_FORWARD) ? power : -power;

        motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        opMode.waitForNextHardwareCycle();

        if (motor != null)
        {
            motor.setPower(powerWithDirection);
        }
    }

    public boolean targetReached()
    {
        boolean reached = false;

        if (motor != null)
        {
            int position = Math.abs(motor.getCurrentPosition());
            //opMode.getTelemetryUtil().addData(name + ": B - Current position", position);

            //choosing within 3 ( close enough)
            if (position >= this.targetEncoderValue - 3)
            {
                reached = true;
                //opMode.telemetry.addData(name + ": Target Reached ", position);
            }
        }
        return reached;
    }


    public void stop() throws InterruptedException
    {
        //opMode.telemetry.addData(name + ": Stoping motor ", "stop");
        running = false;
        motor.setPower(0);

        resetEncoder();
        waitForEncoderToReset();

    }

    public boolean isRunning()
    {
        return running;
    }


    private void resetEncoder() throws InterruptedException
    {
        if (motor != null)
        {
            motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }

    private void waitForEncoderToReset() throws InterruptedException
    {
        while (motor.getCurrentPosition() != 0)
        {
            opMode.waitForNextHardwareCycle();
        }

    }
}
