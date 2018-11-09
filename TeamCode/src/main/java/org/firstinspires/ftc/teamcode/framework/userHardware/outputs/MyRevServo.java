package org.firstinspires.ftc.teamcode.framework.userHardware.outputs;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.boggiewheel_base.hardware.HardwareDevices;
import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;


/**
 * Created by user on 10/1/2018.
 */

public class MyRevServo {
    private HardwareMap hwMap;

    private HardwareDevices hardware;
    private Servo Servo1;
    private Servo Servo2;

    public MyRevServo(){
        // This is an odd way to fix the hardware map issue
        //    where the normal hardware map getter does not work.
        hwMap = AbstractTeleop.getOpModeInstance().hardwareMap;
        Servo1 = hwMap.servo.get("Servo1");
        Servo2 = hwMap.servo.get("Servo2");
        // Put this string on phone
    }
    public void setPosition1(double ServoPosition) {
        Servo1.setPosition(ServoPosition);

    }


    public void setPosition2(double ServoPosition) {
        Servo2.setPosition(ServoPosition);
    }
}
