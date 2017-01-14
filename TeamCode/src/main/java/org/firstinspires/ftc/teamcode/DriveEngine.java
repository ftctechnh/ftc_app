package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

public class DriveEngine
{
    public enum engineMode
    {
        defaultMode,
        pusherMode,
        levelMode, //This is a new drive mode to work on
        directMode,
    }

    private ElapsedTime timer = null;

    DcMotor leftMotor = null;
    DcMotor rightMotor = null;
    Gamepad gamepad = null;

    engineMode mode = engineMode.defaultMode;

    DcMotor.Direction leftDirection = DcMotor.Direction.FORWARD;
    DcMotor.Direction rightDirection = DcMotor.Direction.REVERSE;
    double leftPower = 0;  //[0.0, 1.0]
    double rightPower = 0; //[0.0, 1.0]

    public double inchesBetweenMotors = 14;

    //Variables for pusherMode
    static final int SAMPLE_SIZE = 15;
    List<Float> rightJoyStickValues = new ArrayList();
    List<Float> leftJoyStickValues = new ArrayList();


    public DriveEngine(engineMode mode, HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.mode = mode;
        this.gamepad = gamepad;

        leftMotor  = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

        leftMotor.setDirection(leftDirection);
        rightMotor.setDirection(rightDirection);
        timer = new ElapsedTime();

        for(int i=0; i<SAMPLE_SIZE; i++)
        {
            rightJoyStickValues.add(0f);
            leftJoyStickValues.add(0f);
        }
    }

    public double getLeftPower()
    {
        return leftPower;
    }

    public double getRightPower()
    {
        return rightPower;
    }

    public void setEngineMode(engineMode mode)
    {
        this.mode = mode;
    }

    //Returns pace in seconds per inch, preferably.
    private double getPace(double power){
        return power; //TODO find pace
    }
    //Returns number of second is required to complete a circle at .1 power
    private double getPeriod(){
        return 3.4; //TODO find period
    }

    public void invertDirection()
    {
        if (leftDirection == DcMotor.Direction.FORWARD)
            leftDirection = DcMotor.Direction.REVERSE;
        else
            leftDirection = DcMotor.Direction.FORWARD;

        if (rightDirection == DcMotor.Direction.FORWARD)
            rightDirection = DcMotor.Direction.REVERSE;
        else
            rightDirection = DcMotor.Direction.FORWARD;

        leftMotor.setDirection(leftDirection);
        rightMotor.setDirection(rightDirection);
    }

    private void resetJoyStickSamples(float value)
    {
        for(int i=0; i<SAMPLE_SIZE; i++)
        {
            rightJoyStickValues.set(i, value);
            leftJoyStickValues.set(i, value);
        }
    }

    private void takeJoyStickSample()
    {

        //Copied Preston's code here...
        //Need to review the logic to use absolute power.
        //What we really need to know is when we change
        //direction.  The values of the stick go from -1 to 1...

        if(Math.abs(gamepad.right_stick_y) < Math.abs(rightPower))
            resetJoyStickSamples(gamepad.right_stick_y);

        if(Math.abs(gamepad.left_stick_y) < Math.abs(leftPower))
            resetJoyStickSamples(gamepad.left_stick_y);


        rightJoyStickValues.remove(0);
        rightJoyStickValues.add(gamepad.right_stick_y);

        leftJoyStickValues.remove(0);
        leftJoyStickValues.add(gamepad.left_stick_y);
    }

    private void setEngineToAveragePower()
    {
        takeJoyStickSample();

        float sumRight = 0;
        float sumLeft = 0;
        for(int i=0; i<SAMPLE_SIZE; i++)
        {
            sumRight += rightJoyStickValues.get(i);
            sumLeft += leftJoyStickValues.get(i);
        }
        rightPower = sumRight/SAMPLE_SIZE;
        leftPower = sumLeft/SAMPLE_SIZE;
    }

    public void setEngineToGamePad()
    {
        this.rightPower = gamepad.right_stick_y;
        this.leftPower = gamepad.left_stick_y;
    }

    public void setEngineToPower(double lPower, double rPower)
    {
        rightPower = rPower;
        leftPower = lPower;
    }

    public void setEngineToPower(double power)
    {
        rightPower = power;
        leftPower = power;
    }

    public void setEngineToLevel()
    {
        /*
        This is a new drive mode.  The objective is to try
        another way to gently increase the power as to not loose control.
        Use predefined power levels and increase the power of a short
        period of time.
        Will need constant powers, constant time intervals, and a timer.
         */
    }

    /*
        For Autonomous mode we will need to be able to turn.
        This function will take the degree of the turn we wish to make.
        A good first assumption is that only one wheel is turning at a time.
        A sloping turn will have both wheels moving at different speeds...
    */
    /*
        Assume that straight forward is 0 degrees.
        Positive degrees is turning a steering wheel to the left.
     */
    public void turn(boolean forwards, double degrees)
    {
        double numRevolutions = degrees / 360;
        if(forwards)
        {
            if(degrees >= 0){
                drive(getPeriod() * numRevolutions, 0, .1);
            }
            else{
                drive(getPeriod() * numRevolutions, .1, 0);
            }
        }
        else
        {
            if(degrees >= 0){
                drive(getPeriod() * numRevolutions, 0, -.1);
            }
            else{
                drive(getPeriod() * numRevolutions, -.1, 0);
            }
        }
    }
    public void turn(double degrees){
        turn(true, degrees);
    }

    public void setCircleMotorPower(double radius, double maxPower, boolean turnRight)
    {
        //v1 * r2 = v2 * r1
        double minPower = radius*maxPower/(inchesBetweenMotors+radius);
        if(turnRight)
        {
            //right is greater
            setEngineToPower(minPower, maxPower);
        }
        else
        {
            //left is greater
            setEngineToPower(maxPower, minPower);
        }
    }

    public void drive(double duration, double lPower, double rPower)
    {
        switch (mode)
        {
            case pusherMode:
                setEngineToAveragePower();
                break;

            case directMode:
                setEngineToPower(lPower, rPower);
                break;

            case defaultMode:
                setEngineToGamePad();
                break;

            default:
                leftPower = rightPower = 0f;
        }

        if(duration != 0)
            timer.reset();

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);

        while(duration != 0 && timer.time() < duration);
    }

    public void drive()
    {
        drive(0d, 0d, 0d);
    }

    public void drive(double power)
    {
        drive(0d, power, power);
    }

    public void drive(double duration, double power)
    {
        drive(duration, power, power);
    }

    public void stop()
    {
        leftPower = rightPower = 0f;
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }
}
