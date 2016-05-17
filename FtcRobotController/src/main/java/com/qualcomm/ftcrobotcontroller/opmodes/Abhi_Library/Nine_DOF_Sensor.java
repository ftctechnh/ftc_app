package com.qualcomm.ftcrobotcontroller.opmodes.Abhi_Library;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

import com.qualcomm.ftcrobotcontroller.opmodes.Abhi_Library.Bno055;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by abhin on 4/16/2016.
 */

public class Nine_DOF_Sensor {
    //Read Values
    DcMotor a,b,c,d;
    Bno055 SensorDOF;
    int eulerX = SensorDOF.eulerX();
    int eulerY = SensorDOF.eulerY();
    int eulerZ = SensorDOF.eulerZ();

    public void Nine_DOF_Sensor(HardwareMap hardwareMap,String deviceName,DcMotor am,DcMotor bm,DcMotor cm,DcMotor dm) {
        SensorDOF = new Bno055(hardwareMap,deviceName);
        this.a = am;
        this.b = bm;
        this.c = cm;
        this.d = dm;
        //Start Initilization
        SensorDOF.init();
        //Second Stage
        SensorDOF.init_loop();
    }

    public void moveClock (int angle, float motorPower){
        int previousAngle = eulerZ;
        int currentAngle = eulerZ;
        int delta = Math.abs(previousAngle - currentAngle);
        a.setPower(motorPower);
        b.setPower(motorPower);
        c.setPower(motorPower);
        d.setPower(motorPower);
        while (delta < angle){
            currentAngle = eulerZ;
            delta = Math.abs(previousAngle - currentAngle);
            previousAngle = currentAngle;
        }
        a.setPower(0);
        b.setPower(0);
        c.setPower(0);
        d.setPower(0);
    }
    public void moveCounterClock (int angle, float motorPower){
        int previousAngle = eulerZ;
        int currentAngle = eulerZ;
        int delta = Math.abs(previousAngle - currentAngle);
        a.setPower(-motorPower);
        b.setPower(-motorPower);
        c.setPower(-motorPower);
        d.setPower(-motorPower);
        while (delta < angle){
            currentAngle = eulerZ;
            delta = Math.abs(previousAngle - currentAngle);
            previousAngle = currentAngle;
        }
        a.setPower(0);
        b.setPower(0);
        c.setPower(0);
        d.setPower(0);
    }


}
