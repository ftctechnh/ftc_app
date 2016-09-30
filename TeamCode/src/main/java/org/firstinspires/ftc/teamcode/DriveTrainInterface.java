package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 9/25/2016.
 */
public interface DriveTrainInterface
{
    void init(HardwareMap hardwareMap);
    void drive();
    void driveStraight(int Distance);
    void spin(float Degree);
}


