package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
    /** Drivetrain component of our Relic Recovery Robot */
    public Drivetrain drivetrain = new Drivetrain();

    /** Lift component of our Relic Recovery Robot */
    public Lift lift = new Lift();


    /**
     * Initializes Relic Recovery Robot- be sure to place this somewhere in the OpMode before
     * doing anything robot-related.
     *
     * @param HW The hardware mapping variable to use. In an OpMode, just typing in "hardwareMap"
     *           will do the trick.
     *
     * @param OPMODE The OpMode that this base is running in
     */
    @Override
    public void init(HardwareMap HW , OpMode OPMODE)
    {
        super.init(HW , OPMODE);

        drivetrain.init(this);
        lift.init(this);
    }
}
