package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Peter on 10/4/2015.
 */
public interface DriverInterface
{
    void moveStraightEncoders(float inches, float speed );
<<<<<<< HEAD
    void pivotTurn(float degrees, float speed, boolean isLeft);
    void spinOnCenter(float degrees, double speed, boolean isLeft );
=======
    void pivotTurn(float degrees, float speed);
    void spinOnCenter(float degrees,float speed);
>>>>>>> 391f300a6a28d4991caec65135a4be0ccea06f86
    void stop();
}
