package RicksCode.Bill_Adapted;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jmgu3 on 11/7/2016.
 */
public class JewelArm {
    public Servo jewelArmServo;
    public Servo jewelFlickerServo;

    public void init(HardwareMap hardwareMap) {
        jewelArmServo = hardwareMap.servo.get("jewel_arm");
        //jewelFlickerServo = hardwareMap.servo.get("jewelflicker");

        jewelArmUp();

        //jewelflickerCenter();

        //leftIn();
    }

    public void jewelArmUp() {
        jewelArmServo.setPosition(0.85);
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
