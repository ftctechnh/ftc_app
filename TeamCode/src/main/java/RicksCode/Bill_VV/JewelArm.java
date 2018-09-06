package RicksCode.Bill_VV;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jmgu3 on 11/7/2016.
 */
public class JewelArm {
    public Servo rightServo;
    public Servo leftServo;

    public void init(HardwareMap hardwareMap) {
        rightServo = hardwareMap.servo.get("rightPusher");
        leftServo = hardwareMap.servo.get("leftPusher");
        rightIn();
        leftIn();
    }

    public void rightIn() {
        rightServo.setPosition(35.0/256.0);
    }

    public void rightOut() {
        rightServo.setPosition(170.0/256.0);
    }

    public void leftIn() {
        leftServo.setPosition(235.0/256.0);
    }

    public void leftOut() {
        leftServo.setPosition(100.0/256.0);
    }


}
