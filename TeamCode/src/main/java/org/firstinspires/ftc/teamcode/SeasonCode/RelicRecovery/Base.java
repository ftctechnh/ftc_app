package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift.Lift;


/**
 * Main base for the Relic Recovery Robot
 */
@SuppressWarnings("WeakerAccess")
public class Base extends RobotBase
{
    public Drivetrain drivetrain = new Drivetrain();
    public Lift lift = new Lift();


    @Override
    public void init(HardwareMap HW)
    {
        hardware = HW;

        drivetrain.init(this);
        lift.init(this);
    }
}
