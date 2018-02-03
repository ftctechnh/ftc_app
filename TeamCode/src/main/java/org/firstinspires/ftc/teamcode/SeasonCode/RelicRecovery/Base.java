package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.directcurrent.season.relicrecovery.jewelarm.JewelArm;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.RelicGrabber.RelicGrabber;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Lift.Lift;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.RelicExtender.RelicExtender;

import static java.lang.Thread.sleep;


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

    /** Relic Extender component of our Relic Recovery robot */
    public RelicExtender relicExtender = new  RelicExtender();

    /** Relic Grabber component of our Relic Recovery robot */
    public RelicGrabber relicGrabber = new RelicGrabber();

    /** Jewel Arm component of our Relic Recovery robot */
    public JewelArm jewelArm = new JewelArm();

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
    public void init(HardwareMap HW , LinearOpMode OPMODE)
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
        relicExtender.init(this);
        relicGrabber.init(this);
        jewelArm.init(this);
        imu.init(this , "imu" , params);


        // Flipping IMU axis
        byte AXIS_MAP_CONFIG_BYTE = 0x6;
        byte AXIS_MAP_SIGN_BYTE = 0x1;

        imu.write8(BNO055IMU.Register.OPR_MODE ,BNO055IMU.SensorMode.CONFIG.bVal & 0x0F);

        try
        {
            sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }


        imu.write8(BNO055IMU.Register.AXIS_MAP_CONFIG ,AXIS_MAP_CONFIG_BYTE & 0x0F);
        imu.write8(BNO055IMU.Register.AXIS_MAP_SIGN ,AXIS_MAP_SIGN_BYTE & 0x0F);
        imu.write8(BNO055IMU.Register.OPR_MODE , BNO055IMU.SensorMode.IMU.bVal &0x0F);

        try
        {
            sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }


        // Dependency setting
        drivetrain.setDependencies(imu);
        glyphGrabber.setDependencies();
    }


    /**
     * Stops the Relic Recovery robot.
     *
     * So imaging you're going down the interstate when suddenly a brick wall materializes in front
     * of you. That's basically what this is supposed to do, except with no injuries.
     *
     * Remember, safety FIRST ;)
     */
    @Override
    public void stop()
    {
        drivetrain.stop();
        lift.stop();
        glyphGrabber.stop();
        relicExtender.stop();
        relicGrabber.stop();
        jewelArm.stop();
    }
}
