package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;

//This class defines all the specific hardware for a the BACONbot robot.

public class HardwareBACONbot {
    /* Public OpMode members. */
    public DcMotor motor = null;
    public Servo servo = null;
    public ColorSensor colorSensor = null;
    public TouchSensor touchSensor = null;
    public boolean enableLed = true;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public HardwareBACONbot() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Hardware
        motor = hwMap.dcMotor.get("motorGuy");
        colorSensor = hwMap.colorSensor.get("colorSensor");
        servo = hwMap.servo.get("servo0");
        touchSensor = hwMap.touchSensor.get("touchSensor");

        // Set all hardware to default position
        motor.setPower(0);
        servo.setPosition(0);
        motor.setDirection(DcMotor.Direction.REVERSE);

        // Set proper encoder state for all motors
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
}
