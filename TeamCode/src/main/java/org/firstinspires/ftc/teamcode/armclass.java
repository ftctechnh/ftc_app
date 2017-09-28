package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

//This class defines all the specific hardware for a the BACONbot robot.

public class armclass {
    /* Public OpMode members. */

    public DcMotor verticalArmMotor = null;
//    public Servo servo = null;
//    public ColorSensor colorSensor = null;
//    public TouchSensor touchSensor = null;
//    public boolean enableLed = true;




    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public armclass() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Hardware


        verticalArmMotor = hwMap.dcMotor.get("VAM");
//        colorSensor = hwMap.colorSensor.get("colorSensor");
//        servo = hwMap.servo.get("servo0");
//        touchSensor = hwMap.touchSensor.get("touchSensor");

        // Set all hardware to default position
        // Set all hardware to default position

        verticalArmMotor.setPower(0);
//        servo.setPosition(0);

    }
}
