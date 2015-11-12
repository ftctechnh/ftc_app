package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Dan on 11/12/2015.
 */

public class DucksAtonomous extends LinearOpMode {
    DcMotor left= hardwareMap.dcMotor.get("left");
    DcMotor right= hardwareMap.dcMotor.get("right");
    DcMotor winch=hardwareMap.dcMotor.get("winch");
    DcMotor winchpivot=hardwareMap.dcMotor.get("winchpivot");
    DcMotor winchwheel=hardwareMap.dcMotor.get("winchwheel");
    GyroSensor gyro=hardwareMap.gyroSensor.get("gyro");

    public void turnLeft(int degrees, double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading-degrees;
        if(targetHeading<0){
            targetHeading=+360;
        }
      while(gyro.getHeading()<targetHeading-3 || gyro.getHeading()>targetHeading+3){
          left.setPower(-speed);
          right.setPower(-speed);
      }
        left.setPower(0);
        right.setPower(0);
    }

    public void turnRight(int degrees,double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading+degrees;
        if(targetHeading>360){
            targetHeading=-360;
        }
        while(gyro.getHeading()<targetHeading-3 || gyro.getHeading()>targetHeading+3){
            left.setPower(speed);
            right.setPower(speed);
        }
        left.setPower(0);
        right.setPower(0);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        gyro.calibrate();
        turnRight(90,.5);
        turnLeft(90, .5);
    }
}
