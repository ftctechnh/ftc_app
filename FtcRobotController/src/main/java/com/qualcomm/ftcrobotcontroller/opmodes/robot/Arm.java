package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class Arm{
    public DcMotor motor;
    double motorForwardSpeed = 0.5;
    double motorStoppedSpeed = 0.0;
    double motorBackwardSpeed = -0.5;

    public Servo servo;
    double servoUpwardSpeed = 0.6;
    double servoStoppedSpeed = 0.5;
    double servoDownwardSpeed = 0.4;

    public Arm(){
    }

    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("armMotor");
        servo = hardwareMap.servo.get("armServo");

        servo.setPosition(servoStoppedSpeed);
    }
}
