package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * <p>
 *      Here's our robot for FTC Velocity Vortex. This is where the core components come together.
 *      Things pertaining to the entirety of the robot that isn't an OpMode go here.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * <p>
 *      Hardware checklist (implementation is decided upon in their respective classes):
 *      1. A Drivetrain <br>
 *      2. A Harvester <br>
 *      3. A Shooter <br>
 *      4. A Lift <br>
 *      5. A Cap Ball Spool <br>
 *      6. Sensors <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
final class Robot
{
    HardwareMap hardware = null;                            // Used for hardware mapping

    Drivetrain drivetrain = null;                           // Creates a drivetrain
    Drivetrain.Power drivePower = new Drivetrain.Power();   // Creates the Power helper class
    Harvester harvester = null;                             // Creates a harvester
    Shooter shooter = null;                                 // Creates a shooter
    Lift lift = null;                                       // Creates a lift
    Spool spool = null;                                     // Creates a spool

    private final String _DEFAULT_NAME = "MyRobot";         // The default name of our robot
    private String _name = null;                            // The name of our robot


    /**
     * Constructor- Sets the robot name to the default name
     */
    Robot()
    {
        _name = _DEFAULT_NAME;
    }


    /**
     * Constructor- Sets the robot name to the passed-in string
     */
    Robot(final String NAME)
    {
        _name = NAME;
    }


    /**
     * Accessor- returns the value of the robot's name.
     *
     * @return The value of the robot's name.
     */
    String name()
    {
        return _name;
    }


    /**
     * Initializes the robot by stopping motors, resetting servo positions, and mapping hardware.
     *
     * @param HW Hardware mapping object used to hardware map. Use the HardwareMap object
     *           "hardwareMap" provided for us by the SDK in class "OpMode" (DON'T FORGET THIS)
     */
    void init(final HardwareMap HW)
    {
        hardware = HW;                  // Copy the value of the hardware map to be stored locally

        // Robot drivetrain
        drivetrain = new Drivetrain(this);
        drivetrain.mapDrivetrain();
        drivetrain.mapSensors();
        drivetrain.stop();

        // Robot harvester
        harvester = new Harvester(this);
        harvester.mapHardware();
        harvester.stop();

        // Robot shooter
        shooter = new Shooter(this);
        shooter.mapHardware();
        shooter.stop();

        // Robot lift
        lift = new Lift(this);
        lift.mapHardware();
        lift.stop();

        // Robot spool
        spool = new Spool(this);
        spool.mapHardware();
        spool.stop();
    }
}