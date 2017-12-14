/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-09-??

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
package org.firstinspires.ftc.robotcontroller.internal.Core;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;


/**
 * Class used to manage hardware mapping. It holds methods used to map components.
 */
@SuppressWarnings({"unused", "AccessStaticViaInstance", "ConstantConditions", "WeakerAccess"})
public final class HardwareMapper
{
    // Robot base object we use to hardware map
    private RobotBase _robot;


    /**
     * Constructor- assigns robot to passed in robot
     *
     * @param myRobot Robot object to be passed in
     */
    public HardwareMapper(RobotBase myRobot)
    {
        _robot = myRobot;
    }


    /**
     * Attempts to map a motor with the provided name. Result is returned, allowing an assignment
     * operator (=) to map the desired motor. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the DcMotor as it appears in the configuration file
     * @param DIRECTION Direction of the motor
     *
     * @return Returns a DcMotor, mapped if succeeded. Driver station error results from failure.
     */
    public DcMotor mapMotor(final String NAME , final DcMotorSimple.Direction DIRECTION)
    {
        DcMotor myMotor;                // Motor to be mapped

        myMotor = _robot.hardware.dcMotor.get(NAME);
        myMotor.setDirection(DIRECTION);

        return myMotor;
    }


    /**
     * Maps a servo with provided name. Result is returned, allowing assignment operator (=) to
     * map to the desired servo. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the servo as it appears in the configuration file
     * @param DIRECTION Direction of the servo
     *
     * @return Mapped servo. Driver station error results from failure.
     */
    public Servo mapServo(final String NAME , final Servo.Direction DIRECTION)
    {
        Servo myServo;                              // Servo to be mapped

        myServo = _robot.hardware.servo.get(NAME);
        myServo.setDirection(DIRECTION);

        return myServo;
    }


    /**
     * Maps a continuous rotation servo with provided name. Result is returned, allowing assignment
     * operator (=) to map to the desired servo. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the continuous rotation servo as it appears in the configuration file
     * @param DIRECTION Direction of the continuous rotation servo
     *
     * @return Mapped continuous rotation servo. Driver station error results from failure.
     */
    public CRServo mapCRServo(final String NAME , final CRServo.Direction DIRECTION)
    {
        CRServo myServo;                            // Continuous rotation servo to be mapped

        myServo = _robot.hardware.crservo.get(NAME);
        myServo.setDirection(DIRECTION);

        return myServo;
    }


    /**
     * Attempts to map a Modern Robotics Integrating Gyroscope. Also attempts to set its heading
     * mode to cartesian. Result is returned, allowing an assignment operator to map the desired
     * gyro. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Gyro as it appears in the configuration file
     * @param MODE Heading mode of the Gyro (mathematical or navigational)
     * @param ADDRESS Address of the Gyro sensor as defined by the Core Device Discovery utility
     *
     * @return Returns a gyro, mapped if succeeded. Driver station error results from failure.
     */
    public ModernRoboticsI2cGyro mapMRGyro(final String NAME ,
        final ModernRoboticsI2cGyro.HeadingMode MODE , final int ADDRESS)
    {
        ModernRoboticsI2cGyro myGyro;               // MR gyro to be mapped

        myGyro = (ModernRoboticsI2cGyro)_robot.hardware.gyroSensor.get(NAME);
        myGyro.setHeadingMode(MODE);
        myGyro.setI2cAddress(I2cAddr.create8bit(ADDRESS));

        return myGyro;
    }


    /**
     * Attempts to map a Modern Robotics Integrating Gyroscope. Also attempts to set its heading
     * mode to cartesian. Result is returned, allowing an assignment operator to map the desired
     * gyro. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Gyro as it appears in the configuration file
     * @param MODE Heading mode of the Gyro (mathematical or navigational)
     *
     * @return Returns a gyro, mapped if succeeded. Driver station error results from failure.
     */
    public ModernRoboticsI2cGyro mapMRGyro(final String NAME ,
        final ModernRoboticsI2cGyro.HeadingMode MODE)
    {
        return mapMRGyro(NAME , MODE , ModernRoboticsI2cGyro.ADDRESS_I2C_DEFAULT.get8Bit());
    }


    /**
     * Attempts to map a Modern Robotics Range sensor. Result is returned, allowing an assignment
     * operator to map the desired range sensor. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Range sensor as it appears in the configuration file
     * @param ADDRESS Address of the Range sensor as defined by the Core Device Discovery utility
     *
     * @return Returns a range sensor, mapped if succeeded. Driver station error results from
     *         failure
     */
    public ModernRoboticsI2cRangeSensor mapMRRange(final String NAME , final int ADDRESS)
    {
        ModernRoboticsI2cRangeSensor myRange;           // MR Range to be mapped

        myRange = (ModernRoboticsI2cRangeSensor)_robot.hardware.get(NAME);
        myRange.setI2cAddress(I2cAddr.create8bit(ADDRESS));

        return myRange;
    }


    /**
     * Attempts to map a Modern Robotics Range sensor. Result is returned, allowing an assignment
     * operator to map the desired range sensor. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Range sensor as it appears in the configuration file
     *
     * @return Returns a range sensor, mapped if succeeded. Driver station error results from
     *         failure.
     */
    public ModernRoboticsI2cRangeSensor mapMRRange(final String NAME)
    {
        return mapMRRange(NAME , ModernRoboticsI2cRangeSensor.ADDRESS_I2C_DEFAULT.get8Bit());
    }


    /**
     * Attempts to map a Modern Robotics color sensor. Result is returned, allowing an assignment
     * operator to map the desired color sensor. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Color sensor as it appears in the configuration file
     * @param ADDRESS Address of the Color sensor as defined by the Core Device Discovery utility
     *
     * @return Returns a color sensor, mapped if succeeded. Driver station error results from
     *         failure.
     */
    public ModernRoboticsI2cColorSensor mapMRColor(final String NAME , final int ADDRESS)
    {
        ModernRoboticsI2cColorSensor myColor;           // MR color to be mapped

        myColor = (ModernRoboticsI2cColorSensor)_robot.hardware.colorSensor.get(NAME);
        myColor.setI2cAddress(I2cAddr.create8bit(ADDRESS));


        return myColor;
    }

    /**
     * Attempts to map a Modern Robotics color sensor. Result is returned, allowing an assignment
     * operator to map the desired color sensor. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Color sensor as it appears in the configuration file
     *
     * @return Returns a color sensor, mapped if succeeded. Driver station error results from
     *         failure.
     */
    public ModernRoboticsI2cColorSensor mapMRColor(final String NAME)
    {
        return mapMRColor(NAME , ModernRoboticsI2cColorSensor.ADDRESS_I2C_DEFAULT.get8Bit());
    }


    /**
     * Attempts to map a Modern Robotics ODS. Result is returned, allowing an assignment
     * operator to map the desired ODS. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the ODS as it appears in the configuration file
     *
     * @return Returns a ODS, mapped if succeeded. Driver station error results from failure.
     */
    public ModernRoboticsAnalogOpticalDistanceSensor mapMRODS(final String NAME)
    {
        ModernRoboticsAnalogOpticalDistanceSensor myODS;        // MR ODS to be mapped

        myODS = (ModernRoboticsAnalogOpticalDistanceSensor)
                _robot.hardware.opticalDistanceSensor.get(NAME);

        return myODS;
    }


    /**
     * Attempts to map a Modern Robotics Touch sensor. Result is returned, allowing an assignment
     * operator to map the desired range sensor. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the Touch sensor as it appears in the configuration file
     *
     * @return Returns a touch sensor, mapped if succeeded. Driver station error results from
     *         failure.
     */
    public ModernRoboticsTouchSensor mapMRTouch(final String NAME)
    {
        ModernRoboticsTouchSensor myTouch;          // MR touch to be mapped

        myTouch = (ModernRoboticsTouchSensor)_robot.hardware.touchSensor.get(NAME);

        return myTouch;
    }


    /**
     * Attempts to map a Lego Ultrasonic sensor. Result is returned, allowing an assignment
     * operator to map the desired ultrasonic sensor. Driver station displays error if mapping
     * failed.
     *
     * @param NAME Name of the Ultrasonic sensor as it appears in the configuration file
     *
     * @return Returns a ultrasonic sensor, mapped if succeeded. Driver station error results from
     *         failure.
     */
    public UltrasonicSensor mapLegoUltra(final String NAME)
    {
        UltrasonicSensor myUltra;                   // LEGO ultrasonic to be mapped

        myUltra = _robot.hardware.ultrasonicSensor.get(NAME);

        return myUltra;
    }


    /**
     * Attempts to map the REV Module IMU. Result is returned, allowing an assignment
     * operator to map the desired IMU. Driver station displays error if mapping failed.
     *
     * @param NAME Name of the IMU as it appears in the configuration file
     * @param PARAMETERS IMU parameters, initialized properly
     *
     * @return Returns an IMU sensor, mapped if succeeded. Driver station error results from
     *         failure.
     */
    public BNO055IMU mapREVIMU(final String NAME , final BNO055IMU.Parameters PARAMETERS)
    {
        BNO055IMU myIMU;                            // REV Internal IMU to be mapped

        myIMU = _robot.hardware.get(BNO055IMU.class , NAME);
        myIMU.initialize(PARAMETERS);

        return myIMU;
    }
}
