package org.firstinspires.ftc.teamcode.Utilities;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by spmycp on 11/13/2017.
 */

public class Map {

    HardwareMap map;

    public Map(final HardwareMap MAP)
    {
        map = MAP;
    }


    public DcMotor motor(String name) {return m(map,name,false);}
    public DcMotor revMotor(String name) {return m(map,name,true);}
    //public Servo servo(Servo servo, String name, double position) {return s(servo,name,position,false);}
    ///public Servo revServo(Servo servo, String name, double position) {return s(servo,name,position,true);}
    //public CRServo crservo(CRServo crservo, String name, double power) {return crs(crservo,name,power,false);}
    //public CRServo revCrservo(CRServo crservo, String name, double power) {return crs(crservo,name,power,true);}

    private DcMotor m(HardwareMap hardwareMap, String name, boolean ifReverse) {
        DcMotor motor = null;
        try {
            motor = hardwareMap.dcMotor.get(name);
            if (ifReverse)
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
        } catch (Exception opModeException) {
            //telemetry.addData("Can't map motor", name);
        }
        if(hardwareMap == null)
        {
            //telemetry.addData("Hardware Mapping" , "REEEEEEEEE");

            //Log.e("Hardware Mapping" , "REEEEEEE");
        } else {
            //telemetry.addData("asdasdad","Asd");

            //Log.e("Hardware Mapping" , "no reeeeee");
        }
//        motor.setPower(0);
        return motor;
    }
    /*private Servo s(Servo servo, String name, double position, boolean ifReverse) {
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
    }*/
}
