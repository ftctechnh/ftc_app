package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Kaitlin and Jeremy on 10/6/15.
 * 10/8/15      Edited and added to code
 * 10/11/15     Edit and finish pseudocode
 * 11/22/15     Actually make the code work
 * 11/25/15     Work on moving the robot
 **/
public class AutonomousMountainClimbing extends OpMode //extends LinearOpMode
{
        float speed = 0.75f;
        DcMotor frontRight;
        DcMotor frontLeft;
        DcMotor backRight;
        DcMotor backLeft;
        DcMotor right;
        DcMotor left;

    public void init()
    {
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        right.setDirection(DcMotor.Direction.REVERSE);
        left.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);backLeft.setDirection(DcMotor.Direction.FORWARD);
    }

    public void start()
    {
        /**
        moveStraightForward(24.0f, 1.0f, true);
        **/
        right.setPower(speed);
        left.setPower(speed);
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);


        /**
        spinOnCenter(45.0f, .25f, true);
        **/
        //stuff


        /**
         moveStraightForward(24.0f, 1.0f, true);
         **/
        right.setPower(speed);
        left.setPower(speed);
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);


        /**
         spinOnCenter(90.0f, .25f, true);
         **/
        //stuff


        /**
         moveStraightForward(28.0f, 1.0f, true);
         **/
        right.setPower(speed);
        left.setPower(speed);
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);
    }

    @Override
    public void loop()
    {
    }
   // public stayCentered()
    {
        //resetGyro(); //reset;

        //tries to get proper angle

        /*
        //makes sure it doesn't run into the side
        if (leftTouchSensor == pushed)
        {
            spinOnCenter(3.0f, .25f, false);
        }
        if (rightTocuhSensor == pushed)
        {
            spinOnCenter(3.0f, .25f, true);
        }
        offCentered();
        resetGyro();
        spinOnCenter(180.0f,1.0f,true);
        //move up mountain

        int maxGyro;
        maxGyro = comment;//ENTER THE MAX ANGLE WE SHOULD GO UP
        int time = 0;   // Time for counting down how much time robot is on x angle added by jeremy
        if(readGyro<maxAngle)
        {
            moveStraightForward(1.0f, 1.0f, false);
            time ++; // adds 1 second per x amount of time
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {     //added by jeremy, for wait command
                Thread.currentThread().interrupt(); // found online, not sure if it works or is stable
            }
        }
        else if (time == //time of optimal angle time) // used to stop robo
            // t once it is at x angle for a certain amount of time, I think its good for angle fluctuations
            // added by jeremy, delete if needed
            {
                    driveStop;
        }
        //use gyro to detect a change in angle and stop once it reaches a certain angle
        //add something to stop robot at the top of the mountain^^^^
        // ^ use a sensor to stop the robot once it reaches a certain angle maybe?
    }
    //defines red
    public boolean red()
    {
        //light sensor detects red and the red value
        return true;
    }
    public void offCentered()
    {
        while (noRedDetected)
        {
            //use a gyro before it gets to ramp to center possibly
            if (notCentered == true) //tries to center robot
            {
                //possible for loop action?????????
                // int rAngle = how many loops it does
                int rightNum;
                int leftNum;

                while (detectRedandNotCentered == true)
                {
                    spinOnCenter(1f, .25f, false);
                }
                rightNum = readGyro();
                //int lAngle = how may times the loop does
                while (notDetectRed)
                {
                    spinOnCenter(1f, .25f, true);
                }
                while (detectRed == true)
                {
                    spinOnCenter(1f, .25f, true);
                }
                leftNum = readGyro();
                double center = ((double) (leftNum + rightNum) / 2);
                while (readGyro < center)
                {
                    spinOnCenter(1f, .25f, false);
                }
                //detect distance from middle wall using sensor
                //detect distance from outside red wall using sensor
                //use distances to center the robot on the mountain
            }
        }
    }*/
      //  return;
    }
}