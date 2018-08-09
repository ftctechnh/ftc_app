package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

/*
All of our hardware will be mapped here.
*/
public final class HardwareMapper
{
    private RobotBase robot;

    /*
    The following constructor will assign the robot variable to the robot
    passed in as a parameter.
    */
    public HardwareMapper(RobotBase robotObj)
    {
        robot = robotObj;
    }

    /*
    Maps a motor with the name that is passed in as a parameter.
    The motor that will be mapped is returned.
    */
    public DcMotor mapMotor(final String NAME, final DcMotorSimple.Direction DIRECTION)
    {
        DcMotor tempMotor;
        tempMotor = robot.hardwareMap.dcMotor.get(NAME);
        tempMotor.setDirection(DIRECTION);

        return tempMotor;
    }

    /*
    Maps a servo with the name that is passed in as a parameter.
    The servo that will be mapped is returned.
    */
    public Servo mapServo(final String NAME, final Servo.Direction DIRECTION)
    {
        Servo tempServo;

        tempServo = robot.hardwareMap.servo.get(NAME);
        tempServo.setDirection(DIRECTION);

        return tempServo;
    }

    /*
    Maps a CRServo with the name that is passed in as a parameter.
    The CRServo that will be mapped is returned.
    */
    public CRServo mapCRServo(final String NAME, final CRServo.Direction DIRECTION)
    {
        CRServo tempCRServo;

        tempCRServo = robot.hardwareMap.crservo.get(NAME);
        tempCRServo.setDirection(DIRECTION);

        return tempCRServo;
    }
    /*
    Maps a colorsensor with the name and address that are passed in as parameters.
    The servo that will be mapped is returned.
     */
//    public ColorSensor mapColorSensor(final String NAME, final int ADDRESS)
//    {
//        ColorSensor tempColor;
//
//        tempColor = (ColorSensor)robot.hardware.colorSensor.get(NAME);
//        tempColor.setI2cAddress(I2cAddr.create8bit(ADDRESS));
//
//        return tempColor;
//    }

    /*
    Maps a colorsensor with the name and address that are passed in as parameters.
    The servo that will be mapped is returned.
     */
//    public ColorSensor mapColorSensor(final String NAME)
//    {
//
//        return mapColorSensor(NAME,ColorSensor.ADDRESS_I2C_DEFAULT.get8Bit());
//    }

    /*
    Maps a REVIMU with the name and parameters that are passed in as parameters.
    The IMU that will be mapped is returned.
    */
    public BNO055IMU mapREVIMU(final String NAME , final BNO055IMU.Parameters PARAMETERS)
    {
        BNO055IMU tempIMU;                            // REV Internal IMU to be mapped

        tempIMU = robot.hardwareMap.get(BNO055IMU.class , NAME);
        tempIMU.initialize(PARAMETERS);

        return tempIMU;
    }

}
