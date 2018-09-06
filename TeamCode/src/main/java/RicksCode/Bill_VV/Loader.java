package RicksCode.Bill_VV;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jmgu3 on 11/7/2016.
 */
public class Loader {
    public Servo servo;
    public Servo servo2;

    public int timeToRaise;
    public int timeToLower;
    private double loaderPos;
    private double loader2Pos;

    public void init(HardwareMap hardwareMap) {
        servo = hardwareMap.servo.get("loader");
        servo2 = hardwareMap.servo.get("loader2");
        lower();

        timeToRaise = 500;
        timeToLower = 2000;

    }

    public void raise() {
        loaderPos = .72;
        updateLoader2Pos();
        servo.setPosition(loaderPos);
        servo2.setPosition(loader2Pos);
        // servo2.setPosition(  /256.0);
    }

    public void lower() {
        loaderPos = .5;
        updateLoader2Pos();
        servo.setPosition(loaderPos);
        servo2.setPosition(loader2Pos);
        // servo2.setPosition(  /256.0);
    }

    public void updateLoader2Pos()
    {
        loader2Pos = -loaderPos + 1.1;
    }
}
