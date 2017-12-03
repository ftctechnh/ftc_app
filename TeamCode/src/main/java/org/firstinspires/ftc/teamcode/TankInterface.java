package org.firstinspires.ftc.teamcode;

/**
 * Created by 111 on 6/11/2017.
 */

public interface TankInterface
{
    //Drive Train Functions
    void driveStraight_In(float inches);
    void driveStraight_Cm(float cm);
    void spin_Right(float degrees);
    void spin_Left(float degrees);
    void pivot(float degrees);
    //Attachments Section
}
