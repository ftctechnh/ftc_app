package org.steelhead.ftc;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Alec Matthews on 9/18/16.
 * Added this class so we don't have to
 * edit a sample one.
 *
 */
public class  HardwareSteelheadTestBot {
    public DcMotor leftMotor       = null;
    public DcMotor rightMotor      = null;
    public Servo    pushRight           = null;
    public Servo    pushLeft           = null;
    public ColorSensor colorSensor = null;

    private String leftMotorName  = "leftMotor";
    private String rightMotorName = "rightMotor";
    private String colorSensorName = "color_sensor";
    private String Servo1Name = "pushRight";
    private String Servo2Name = "pushLeft";






    public void init(HardwareMap aHwMap) {

        leftMotor = aHwMap.dcMotor.get(leftMotorName);
        rightMotor = aHwMap.dcMotor.get(rightMotorName);

        pushRight = aHwMap.servo.get(Servo1Name);
        pushLeft = aHwMap.servo.get(Servo2Name);

        colorSensor = aHwMap.colorSensor.get(colorSensorName);
        colorSensor.enableLed(false);

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pushRight.setPosition(0.0);
        pushLeft.setPosition(0.0);

    }

    public void setLeftMotorName(String newName) {
        leftMotorName = newName;
    }

    public void setRightMotorName(String newName) {
        rightMotorName = newName;
    }

    public void setColorSensorName(String newName) {
        colorSensorName = newName;
    }

    public void setServo1Name(String newName) {
        Servo1Name = newName;
    }

    public void setServo2Name(String newName) {
        Servo2Name = newName;
    }
}
