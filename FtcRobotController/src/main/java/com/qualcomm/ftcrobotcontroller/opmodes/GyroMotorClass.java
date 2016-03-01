package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by dimpledhawan on 2/13/16.
 */
public class GyroMotorClass
{
    private LinearOpMode opMode;
    private DcMotor left;
    private DcMotor right;
    private int targetHeading;
    private GyroSensor gyro;
    private boolean running;
    private Direction currMotor;

    public GyroMotorClass(LinearOpMode opMode, GyroSensor gyro, DcMotor left, DcMotor right)
    {
        this.opMode = opMode;
        this.left = left;
        this.right = right;
        this.gyro = gyro;
        this.running = false;
        this.targetHeading = 0;
    }

    public void startPivotTurn(int degrees, double power, Direction direction) //heading=rotation
    {
        //heading should be relative to current heading, pos or neg

        /*if (Math.abs(targetHeading + currentHeading) < 0)
            this.targetHeading = targetHeading + currentHeading + 360;
        else if (Math.abs(targetHeading + currentHeading) > 359)
            this.targetHeading = (targetHeading + currentHeading - 360);
        else
            this.targetHeading = targetHeading + currentHeading;

        this.running = true;
            if (Direction.MOTOR_RIGHT.equals(direction))
            {
                this.right.setPower(setPower);
                this.currMotor = Direction.MOTOR_RIGHT;
            }
            else
            {
                this.left.setPower(setPower);
                this.currMotor = Direction.MOTOR_LEFT;
            }*/

        this.targetHeading = 45;
        this.currMotor = Direction.MOTOR_LEFT;
        int currentHeading = this.gyro.getHeading();

        this.right.setPower(0);
        this.left.setPower(.65);

        while(Math.abs(currentHeading-targetHeading) <= 3)
        {

        }

        right.setPower(0);
        left.setPower(0);
    }

    public boolean targetReached()
    {
        boolean reached = false;
        if (gyro != null)
        {
            int position = gyro.getHeading();
            //opMode.getTelemetryUtil().addData(name + ": B - Current position", position);

            //choosing within 3 (close enough)
            if (Math.abs(position - this.targetHeading) <= 3)
            {
                reached = true;
            }
        }
        return reached;
    }

    public void stop() throws InterruptedException
    {
        running = false;
        if (this.currMotor == Direction.MOTOR_RIGHT)
        {
            this.right.setPower(0);
        }
        else if (this.currMotor == Direction.MOTOR_LEFT)
        {
            this.left.setPower(0);
        }
    }

    public boolean isRunning()
    {
        return running;
    }
    public int getTargetHeading(){
        return this.targetHeading;
    }


}
