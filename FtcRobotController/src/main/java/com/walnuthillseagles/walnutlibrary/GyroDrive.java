package com.walnuthillseagles.walnutlibrary;

import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
@Deprecated
public class GyroDrive extends DistanceDrive{
    //Hare
    private GyroSensor gyro;

    //Constatns that allow you to modify behaviors
    //Value used when motors need to be realigned
    public static final double MOTORADJUSTMENTPOW = 0.8;
    //Gyro only
    public GyroDrive(DistanceMotor myLeft, DistanceMotor myRight, double myWidth,
                     GyroSensor myGyro){
        super(myLeft, myRight,myWidth);
        gyro = myGyro;
    }

    //Stuff users can access
    //public void linearDrive(int inches, ){
    //
    //}
    //Stuff I worry about
}
