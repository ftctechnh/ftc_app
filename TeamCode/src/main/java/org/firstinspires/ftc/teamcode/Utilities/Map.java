package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by spmycp on 11/13/2017.
 */

public class Map extends OpMode {

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }

    public DcMotor motor(DcMotor motor, String name) {return m(motor,name,false);}
    public DcMotor revMotor(DcMotor motor, String name) {return m(motor,name,true);}
    public Servo servo(Servo servo, String name, double position) {return s(servo,name,position,false);}
    public Servo revServo(Servo servo, String name, double position) {return s(servo,name,position,true);}
    public CRServo crservo(CRServo crservo, String name, double power) {return crs(crservo,name,power,false);}
    public CRServo revCrservo(CRServo crservo, String name, double power) {return crs(crservo,name,power,true);}

    private DcMotor m(DcMotor motor, String name, boolean ifReverse) {
        try {
            motor = hardwareMap.dcMotor.get(name);
            if (ifReverse)
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map motor", name);
        }
        return motor;
    }
    private Servo s(Servo servo, String name, double position, boolean ifReverse) {
        try {
            servo = hardwareMap.servo.get(name);
            if (ifReverse)
                servo.setDirection(Servo.Direction.REVERSE);
            servo.setPosition(position);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map servo", name);
        }
        return servo;
    }
    private CRServo crs(CRServo crservo, String name, double power, boolean ifReverse) {
        try {
            crservo = hardwareMap.crservo.get(name);
            if (ifReverse)
                crservo.setDirection(DcMotorSimple.Direction.REVERSE);
            crservo.setPower(power);
        } catch (Exception opModeException) {
            telemetry.addData("Can't map crservo", name);
        }
        return crservo;
    }
}
