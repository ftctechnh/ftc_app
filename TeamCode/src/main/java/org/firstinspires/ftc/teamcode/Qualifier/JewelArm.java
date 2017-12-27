package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class JewelArm {
    public Servo jewelArmServo;
    public Servo jewelFlickerServo;

    public void init(HardwareMap hardwareMap) {
        jewelArmServo = hardwareMap.servo.get("jewel_arm");
        //jewelFlickerServo = hardwareMap.servo.get("jewelflicker");

        jewelArmUp();

        //jewelflickerCenter();

    }

    public void jewelArmUp() {
//        jewelArmServo.setPosition(0.75);
        jewelArmServo.setPosition(0.75);
    }

    public void jewelArmDown() {
        jewelArmServo.setPosition(0.2);
    }

   /* public void jewelflickerBack() {
        jewelFlickerServo.setPosition(0.0);
    }
    public void jewelflickerCenter() {
        jewelFlickerServo.setPosition(0.5);
    }
    public void jewelflickerForward() {
        jewelArmServo.setPosition(1.0);
    }*/


}
