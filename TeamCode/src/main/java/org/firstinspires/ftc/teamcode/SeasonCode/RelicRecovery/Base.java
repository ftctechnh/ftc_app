package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;


/**
 * Main base for the Relic Recovery Robot
 */
public class Base extends RobotBase
{
    @SuppressWarnings("WeakerAccess")
    public Drivetrain drivetrain = new Drivetrain();

    @Override
    public void init(HardwareMap HW)
    {
        drivetrain.init(this);
    }
}
