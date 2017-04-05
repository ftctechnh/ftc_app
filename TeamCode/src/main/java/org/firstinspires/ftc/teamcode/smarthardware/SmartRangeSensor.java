package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.threading.ProgramFlow;

public class SmartRangeSensor
{
    public final ModernRoboticsI2cRangeSensor rangeSensor;

    public SmartRangeSensor(ModernRoboticsI2cRangeSensor rangeSensor, int i2cAddress)
    {
        this.rangeSensor = rangeSensor;

        rangeSensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));
    }

    public double getVALIDDistCM () throws InterruptedException
    {
        long startTime = 0; //Won't block indefinitely, a half second at most.
        double rangeSensorOutput = 255;
        while ((System.currentTimeMillis() - startTime) < 500 && (rangeSensorOutput >= 255 || rangeSensorOutput <= 0))
        {
            rangeSensorOutput = rangeSensor.cmUltrasonic ();
            ProgramFlow.pauseForSingleFrame ();
        }
        return rangeSensorOutput;
    }

    public boolean returningValidOutput()
    {
        return !(rangeSensor.getDistance (DistanceUnit.CM) < 1.0);
    }
}
