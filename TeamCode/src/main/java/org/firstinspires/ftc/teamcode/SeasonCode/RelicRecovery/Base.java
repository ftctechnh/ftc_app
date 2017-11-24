package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift.Lift;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber;


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

    /** Glyph grabber component of our Relic Recover robot */
    public GlyphGrabber glyphGrabber = new GlyphGrabber();

    /** Built in IMU in the Rev module */
    public REVIMU imu = new REVIMU();


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

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();

        // IMU parameters
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = true;
        params.loggingTag = "IMU";

        // Basic component initialization
        drivetrain.init(this);
        lift.init(this);
        glyphGrabber.init(this);
        imu.init(this , "imu" , params);

        // Dependency setting
        drivetrain.setDependencies(imu);
    }
}
