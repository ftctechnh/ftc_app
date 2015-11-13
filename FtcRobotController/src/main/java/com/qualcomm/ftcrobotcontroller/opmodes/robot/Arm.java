package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class Arm{
    public DcMotor motor;
    public double motorForwardSpeed = 0.5;
    public double motorStoppedSpeed = 0.0;
    public double motorBackwardSpeed = -0.5;

    public Servo servo;
    public double servoUpwardSpeed = 0.6;
    public double servoStoppedSpeed = 0.5;
    public double servoDownwardSpeed = 0.4;

    public Arm(){
    }

    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("armMotor");
        servo = hardwareMap.servo.get("armServo");
        servo.setPosition(servoStoppedSpeed);
    }

}
