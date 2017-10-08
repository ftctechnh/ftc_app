package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Jeremy on 6/11/2017.
 */
public class TankBase implements TankInterface
{
    private DcMotorImplEx driveLeftOne = null;
    private DcMotorImplEx driveRightOne = null;

    private int encCountsPerRev = 1120; //Based on Nevverest 40 motors
    private float roboDiameterCm = 100; // can be adjusted
    private float wheelCircIn = 4 * (float)Math.PI ; //Circumference of wheels used
    private float wheelCircCm = (float)(10.16 * Math.PI);

    public TankBase(HardwareMap hMap)
    {
        driveLeftOne = (DcMotorImplEx) hMap.dcMotor.get("driveLeftOne");
        driveRightOne = (DcMotorImplEx) hMap.dcMotor.get("driveRightOne");

        driveRightOne.setVelocity(3 * Math.PI, AngleUnit.RADIANS); //Neverrest 40 has 160 RPM; 2.6 rev per second

        resetEncoders();
        driveRightOne.setVelocity(3 * Math.PI, AngleUnit.RADIANS);
        driveLeftOne.setVelocity(3* Math.PI, AngleUnit.RADIANS);
        driveRightOne.setDirection(DcMotorSimple.Direction.FORWARD);
        driveLeftOne.setDirection(DcMotorSimple.Direction.FORWARD);

/*
        driveRightOne.setDirection(DcMotorSimple.Direction.REVERSE);
        driveLeftOne.setDirection(DcMotorSimple.Direction.REVERSE);
*/
        stopAllMotors();
    }

    public void driveStraight_In(float inches)
    {
        float encTarget = encCountsPerRev / wheelCircIn * inches;
        //You get the number of encoder counts per unit and multiply it by how far you want to go

        resetEncoders();
        //Notes: We are using Andymark Neverrest 40
        // 1120 counts per rev

        if(inches < 0)
        {
            driveRightOne.setPower(-1);
            driveLeftOne.setPower(1);

            while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget) {}
        }
        else
        {
            driveRightOne.setPower(1);
            driveLeftOne.setPower(-1);

            while(driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget){}
        }

        stopAllMotors();
    }

    public void driveStraight_Cm(float cm)
    {
        float encTarget = encCountsPerRev / wheelCircCm * cm;

        resetEncoders();
        //Notes: We are using Andymark Neverrest 40
        // 1120 counts per rev

        if(cm < 0)
        {
            driveRightOne.setPower(-1);
            driveLeftOne.setPower(1);

            while (driveLeftOne.getCurrentPosition() < -encTarget && driveRightOne.getCurrentPosition() > encTarget) {}
        }
        else
        {
            driveRightOne.setPower(1);
            driveLeftOne.setPower(-1);

            while(driveLeftOne.getCurrentPosition() > -encTarget && driveRightOne.getCurrentPosition() < encTarget){}
        }

        stopAllMotors();
    }



    public void spin_Right(float degrees)// Right Motor only moves!
    {
        float degToRad = degrees * (float)Math.PI / 180.0f; // converts it to Radians

        float encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter

        resetEncoders();

        if (degrees < 0) //spins clockwise
        {
            driveRightOne.setPower(-1);

            while(driveRightOne.getCurrentPosition() > encTarget){}
        }
        else //spins cc
        {
            driveRightOne.setPower(1);

            while(driveRightOne.getCurrentPosition() < encTarget){}
        }
    }

    public void spin_Left(float degrees)//Left Motor only moves!!!!!!!
    {
        float degToRad = degrees * (float)Math.PI / 180.0f; // converts it to Radians

        float encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm);
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter

        resetEncoders();

        if (degrees > 0) //This spins the robot counterclockwise
        {
            driveLeftOne.setPower(1);

            while(driveLeftOne.getCurrentPosition() < encTarget){}

        }
        else //spins clockwise
        {
            driveLeftOne.setPower(-1);

            while(driveLeftOne.getCurrentPosition() > encTarget){}
        }
    }


    public void pivot(float degrees)//Utilizes two motors at a time; spins in place
    {
        float degToRad = degrees * (float) Math.PI / 180.0f; // converts it to Radians

        float encTarget = (roboDiameterCm / 2 * degToRad) * (encCountsPerRev / wheelCircCm) / 2;
        //To explain, the first set of parenthesis gets the radius of robot and multiplies it by the degrees in radians
        //second set gets encoder counts per centimeter
        //we divide it by two at the end to compensate for using two motors

        resetEncoders();

        //It pivots in the direction of how to unit circle spins
        if (degrees < 0) //Pivot Clockwise
        {
            driveRightOne.setPower(-1);
            driveLeftOne.setPower(-1);

            while (driveLeftOne.getCurrentPosition() > encTarget && driveRightOne.getCurrentPosition() > encTarget) {}

        }
        else //CounterClockwise
        {
            driveRightOne.setPower(1);
            driveLeftOne.setPower(1);

            while (driveLeftOne.getCurrentPosition() < encTarget && driveRightOne.getCurrentPosition() < encTarget) {}
        }
    }

    public int getRightEncoderPos()
    {
        return driveRightOne.getCurrentPosition();
    }

    public int getLeftEncoderPos()
    {
        return driveLeftOne.getCurrentPosition();
    }

    public void driveMotors(float lPow, float rPow)
    {
        driveLeftOne.setPower(lPow);
        driveRightOne.setPower(rPow);
    }

    public void stopAllMotors()
    {
        driveLeftOne.setPower(0);
        driveRightOne.setPower(0);
    }

    private void resetEncoders()
    {
        driveRightOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveLeftOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public DcMotorImplEx getDriveLeftOne()
    {
        return driveLeftOne;
    }

    public DcMotorImplEx getDriveRightOne() {return driveRightOne;}
}
