package RicksCode.Bill_VV;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ftc8045 on 10/22/2017.
 */

public class RelicArm {
    public Servo relicServo;

    public void init(HardwareMap hardwareMap) {
        relicServo = hardwareMap.servo.get("relicjaw");

        jawOpen();
        jawClosed();
    }

    public void jawOpen() {
        relicServo.setPosition(35.0/256.0);
    }

    public void jawClosed() {
        relicServo.setPosition(170.0/256.0);
    }



}
