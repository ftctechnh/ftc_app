package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pramo on 11/15/2017.
 */

public class Dropdown{
    private NormalizedColorSensor jewelSensor;
    private Servo servo;


    public boolean closed = false;
    public boolean isPressed = false;
    private boolean colorCheckFinished = false;
    private NormalizedRGBA colors;

    private final double SERVO_OPEN_POSITION = (174.0/180);
    private final double SERVO_CLOSED_POSITION = (84.0/180);

    public Dropdown(Servo servo, DcMotor motor0, NormalizedColorSensor color) {

        this.servo = servo;
        this.jewelSensor = color;
        colors = jewelSensor.getNormalizedColors();

        //servo.setPosition(SERVO_CLOSED_POSITION);

    }

    public void runDrop() {
       // Drop();
        checkColor();
       // Drop();
    }

    public void Drop() {

        if (closed) {
            servo.setPosition(SERVO_OPEN_POSITION);
            closed = false;
        } else if (colorCheckFinished){
            servo.setPosition(SERVO_CLOSED_POSITION);
            closed = true;
        }
    }

    public void checkColor() {
//        Drop();

//        if (jewelSensor instanceof SwitchableLight) {
//            ((SwitchableLight)jewelSensor).enableLight(true);
//        }

//        if (colors.red >= 100)
//            pushLeft();
//        else
//            pushRight();
        colorCheckFinished = true;
    }
//    public void pushLeft() {
//        motor.setPower(.2);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        motor.setPower(0);
//    }
//    public void pushRight() {
//        motor.setPower(-.2);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        motor.setPower(0);
//    }

}
