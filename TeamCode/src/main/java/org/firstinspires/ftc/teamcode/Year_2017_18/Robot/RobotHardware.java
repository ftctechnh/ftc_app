package org.firstinspires.ftc.teamcode.Year_2017_18.Robot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import javax.lang.model.type.NullType;

/**
 * The class to control and store all the robot hardware.
 * @version 2
 */
@Disabled
public class RobotHardware
{
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public DcMotor pulleyArm = null;
    public DcMotor relicArm = null;
    public Servo sensorArm = null;
    public Servo leftClaw = null;
    public Servo rightClaw = null;
    public Servo colorRotate = null;
    public Servo relicClaw = null;
    public ColorSensor colorSensor = null;
    public ElapsedTime runTime = new ElapsedTime();


    private HardwareMap hwMap =  null;

    public RobotHardware() {}

    /**
     * Initializes the TileRunnerRobot with a hardware map and then init the hardware.
     * @param ahMap The hardwareMap
     */
    public RobotHardware(HardwareMap ahMap)
    {
        hwMap = ahMap;
        init();
    }

    /**
     * Initializes the RobotHardare and inits all the hardware devices.
     * @param ahwMap The OpMode HardareMap to get hardware from
     */
    public void init(HardwareMap ahwMap)
    {
        hwMap = ahwMap;

        // Init our devices
        leftDrive = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");
        pulleyArm = hwMap.get(DcMotor.class, "pulley_arm");
        relicArm = hwMap.get(DcMotor.class, "relic_arm");
        leftClaw = hwMap.get(Servo.class, "left_claw");
        rightClaw = hwMap.get(Servo.class, "right_claw");
        sensorArm = hwMap.get(Servo.class, "sensor_arm");
        colorRotate = hwMap.get(Servo.class, "color_rotate");
        colorSensor = hwMap.get(ColorSensor.class, "color_sensor");
        relicClaw = hwMap.get(Servo.class, "relic_claw");

        // Setup default directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        leftClaw.setDirection(Servo.Direction.FORWARD);
        rightClaw.setDirection(Servo.Direction.REVERSE);
    }

    /**
     * Initializes the hardware if the hardware map is already defined
     */
    public void init()
    {
        if(hwMap == null) throw new NullPointerException();

        // Init our devices
        leftDrive = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");
        pulleyArm = hwMap.get(DcMotor.class, "pulley_arm");
        leftClaw = hwMap.get(Servo.class, "left_claw");
        rightClaw = hwMap.get(Servo.class, "right_claw");
        sensorArm = hwMap.get(Servo.class, "sensor_arm");
        colorRotate = hwMap.get(Servo.class, "color_rotate");
        colorSensor = hwMap.get(ColorSensor.class, "color_sensor");


        // Setup default directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        leftClaw.setDirection(Servo.Direction.FORWARD);
        rightClaw.setDirection(Servo.Direction.REVERSE);
    }

    /**
     * Stops the two drive motors
     */
    public void stopDrive()
    {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }
}