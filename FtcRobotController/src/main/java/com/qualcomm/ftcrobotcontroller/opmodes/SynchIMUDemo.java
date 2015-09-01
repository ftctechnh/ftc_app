package com.qualcomm.ftcrobotcontroller.opmodes;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.IBNO055IMU;

/**
 * SynchIMUDemo gives a short demo on how to use the BNO055 Inertial Motion Unit (IMU) from AdaFruit.
 * http://www.adafruit.com/products/2472
 */
public class SynchIMUDemo extends SynchronousOpMode
    {
    IBNO055IMU imu;
    
    @Override public void main() throws InterruptedException
        {
        imu = ClassFactory.createAdaFruitBNO055IMU(hardwareMap.i2cDevice.get("imu"));

        waitForStart();
        
        while (opModeIsActive())
            {
            
            
            telemetry.dashboard.update();
            idle();
            }
        }
    }
