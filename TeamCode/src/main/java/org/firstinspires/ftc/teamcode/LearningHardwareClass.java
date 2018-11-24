package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//This class defines all the specific hardware for our robot.

public class LearningHardwareClass {
    /* Public OpMode members. */
    public DcMotor myMotor = null;

    /* Define hardwaremap */
    HardwareMap hardwareMap = null;

    /* Constructor */
    public LearningHardwareClass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {

        // Save reference to Hardware map
        hardwareMap = ahwMap;

        // Get color sensor from xml/configuration
        myMotor = hardwareMap.dcMotor.get("MM");

        // Set hardward to it's default position
        myMotor.setPower(0);
    }
}
