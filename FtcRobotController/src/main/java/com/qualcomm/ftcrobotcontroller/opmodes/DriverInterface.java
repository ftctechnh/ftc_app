package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Peter on 10/4/2015.
 */
public interface DriverInterface
{
    void moveStraightEncoders(float inches, float speed );
    void pivotTurn(float degrees, float speed, boolean isLeft);
    void spinOnCenter(float degrees,float speed, boolean isLeft );
    void stop();
}
