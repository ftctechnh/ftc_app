package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by spmce on 11/6/2016.
 */
public class VelocityVortexMap extends VelocityVortexHardware {
    public void map() {
        // Hardware Map
        // ADD HARDWARE MAP HERE;
        // DcMotors - Map
        mFL = hardwareMap.dcMotor.get("fl");
        mFR = hardwareMap.dcMotor.get("fr");
        mFR.setDirection(DcMotorSimple.Direction.REVERSE);
        mBL = hardwareMap.dcMotor.get("bl");
        mBR = hardwareMap.dcMotor.get("br");
        mBR.setDirection(DcMotorSimple.Direction.REVERSE);
        mSweeper = hardwareMap.dcMotor.get("swpr");
        sLeftBeacon = hardwareMap.servo.get("sLeftButt");
        sRightBeacon = hardwareMap.servo.get("sRightButt");
        sLeftBeacon.setPosition(initLeftBeacon);
        sRightBeacon.setPosition(initRightBeacon);
        // Servos - Map
        //sLeftBeacon = hardwareMap.servo.get("sLeftButt");
        //sRightBeacon = hardwareMap.servo.get("sRightButt");
        // Sensors - Map
        //touch = hardwareMap.touchSensor.get("touch");
        //color1 = hardwareMap.colorSensor.get("color1");
        //light1 = hardwareMap.lightSensor.get("light1");
        //light2 = hardwareMap.lightSensor.get("light2");
        //gyro = hardwareMap.gyroSensor.get("gyro");
        //range = hardwareMap.
    }
}
