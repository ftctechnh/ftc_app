package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by spmycp on 11/13/2017.
 */
public class Map {
    // -------------------------- Objects ---------------------------
    HardwareMap hardwareMap = null;
    Telemetry telemetry = null;
    // ------------------------ Constructor -------------------------
    public Map(final HardwareMap map, final Telemetry tele) {
        hardwareMap = map;
        telemetry = tele;
    }
    // ----------------------- Public Methods -----------------------
    // ---------------- DcMotors ----------------
    public DcMotor motor(String name) {
        return m(name,false);
    }
    public DcMotor revMotor(String name) {
        return m(name,true);
    }
    // ------------ Standard Servos -------------
    public Servo servo(String name, double position) {
        return s(name,position,false);
    }
    public Servo revServo(String name, double position) {
        return s(name,position,true);
    }
    // ------- Continuous Rotation Servos -------
    public CRServo crservo(String name) {
        return crs(name,false);
    }
    public CRServo revCrservo(String name) {
        return crs(name,true);
    }

    public ColorSensor colorSensor(String name) {
        ColorSensor colorSensor = null;
        try {
            colorSensor = hardwareMap.get(ColorSensor.class,name);
        } catch (Exception e) {
            telemetry.addData("sColor-distance", "well I tried");
        }
        return colorSensor;
    }

    // ---------------------- Private Methods -----------------------
    // ---------------- DcMotors ----------------
    private DcMotor m(String name, boolean ifReverse) {
        DcMotor motor = null;
        try {
            motor = hardwareMap.dcMotor.get(name);
            if (ifReverse)
                motor.setDirection(DcMotorSimple.Direction.REVERSE);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            telemetry.addData("Can't map motor", name);
        }
        return motor;
    }
    // ------------ Standard Servos -------------
    private Servo s(String name, double position, boolean ifReverse) {
        Servo servo = null;
        try {
            servo = hardwareMap.servo.get(name);
            if (ifReverse)
                servo.setDirection(Servo.Direction.REVERSE);
            servo.setPosition(position);
        } catch (Exception e) {
            telemetry.addData("Can't map servo", name);
        }
        return servo;
    }
    // ------- Continuous Rotation Servos -------
    private CRServo crs(String name, boolean ifReverse) {
        CRServo crservo = null;
        try {
            crservo = hardwareMap.crservo.get(name);
            if (ifReverse)
                crservo.setDirection(DcMotorSimple.Direction.REVERSE);
        } catch (Exception e) {
            telemetry.addData("Can't map crservo", name);
        }
        return crservo;
    }
}
