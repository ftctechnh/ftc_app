package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.HardwareMapper;

/**
 * Created by pmkf2 on 9/1/2018.
 */

public final class REVColorSensor
{
    private RobotBase robot;

    private ColorSensor color;

    //Constructor to create the color sensor

    REVColorSensor(RobotBase myRobot)
    {
        robot = myRobot;
    }

    //Map the color sensor.

    void mapColorSens(final String NAME, final int ADDRESS)
    {
        HardwareMapper mapHelper = new HardwareMapper(robot);

        color = mapHelper.mapColorSensor(NAME, ADDRESS);

    }

    //returns alpha
    int alpha()
    {
        return color.alpha();
    }

    //Returns color that the sensor detects.

}
