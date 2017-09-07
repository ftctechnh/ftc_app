/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.teamcode.TestCode.SixWheelPrototype;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Core.RobotBase;
import org.firstinspires.ftc.teamcode.TestCode.SixWheelPrototype.Drivetrain.Drivetrain;


/**
 * Base of the 6-wheel prototype- this object will be used as central hub for all components and
 * centralizes hardware mapping.
 */
public class SixWheelBase extends RobotBase
{
    /** The robot's drivetrain object */
    Drivetrain drivetrain = new Drivetrain();


    /**
     * Initializes the robot. Hardware mapping and position setting should be done here
     *
     * @param HW The hardware mapping variable to use. In an OpMode, just typing in "hardwareMap"
     */
    @Override
    public void init(final HardwareMap HW)
    {
        mapHardware(HW);
    }


    /**
     * Maps the robot's hardware.
     *
     * @param HW HardwareMap (not HardwareMapper) object to use- this is OpMode's object. When
     *           calling this method in and op mode, simply pass in "hardwareMap"
     */
    private void mapHardware(final HardwareMap HW)
    {
        hardware = HW;

        drivetrain.init(this);
    }
}
