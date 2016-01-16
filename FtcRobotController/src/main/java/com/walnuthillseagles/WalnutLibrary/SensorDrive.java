package com.walnutHillsEagles.WalnutLibrary;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
@Deprecated
public class SensorDrive extends SimpleDrive {
    private GyroSensor gyro;
    private AccelerationSensor accel;
    private boolean gyroOnly;

    //Value used when motors need to be realigned
    public static final double MOTORADJUSTMENTPOW = 0.8;
    //Gyro only
    public SensorDrive(IncMotor myLeft, IncMotor myRight, GyroSensor myGyro){
        super(myLeft,myRight);
        gyro = myGyro;
        gyroOnly = true;
    }

    //Add an Accelerameter
    public SensorDrive(IncMotor myLeft, IncMotor myRight,
                       GyroSensor myGyro,AccelerationSensor myAccel){
        this(myLeft, myRight, myGyro);
        accel = myAccel;
        //Will override the previous "true" value
        gyroOnly = false;
    }
    //Stuff users can access
    //public void linearDrive(int inches, ){
    //
    //}
    //Stuff I worry about
}
