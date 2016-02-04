package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Justin on 12/1/2015.
 */
public class AutonomousCommands extends LinearOpMode{
    public static int WINCH=1;
    public static int WINCHPIVOT=2;
    public static int WINCHWHEEL=2;
    public static int RIGHT=1;
    public static int LEFT=1;
    public static int SWEEPER=2;
    public GyroSensor gyro;
    public DcMotorController winchwheelMC;
    public DcMotorController leftsweepMC;
    public DcMotorController rightpivotMC;
    public ColorSensor color;
    @Override
    public void runOpMode() throws InterruptedException {
    }

    public void sleep(long MS){
        long start=System.currentTimeMillis();
        while(System.currentTimeMillis()<start+MS){
            telemetry.addData("waiting","");
        }
    }
    public void goForward(double speed, long MS)throws InterruptedException{
        long startTime=System.currentTimeMillis();
        int startHeading=gyro.getHeading();
        while(System.currentTimeMillis()<startTime+MS){
            int offset=gyro.getHeading()-startHeading;
            if(offset<-180){
                offset=offset+360;
            }
            if(offset>180){
                offset=offset-360;
            }
            telemetry.addData("offset",offset);
            if(Math.abs(offset)<=2 && rightpivotMC.getMotorPower(RIGHT)!=speed) {
                rightpivotMC.setMotorPower(RIGHT, speed);
                leftsweepMC.setMotorPower(LEFT, -speed);
                telemetry.addData("offsetting",0);
            }
            if(2<Math.abs(offset) && Math.abs(offset)<=5){
                telemetry.addData("offsetting",1);
                if(offset<0 && rightpivotMC.getMotorPower(RIGHT)!=speed-.5){
                    rightpivotMC.setMotorPower(RIGHT, speed-.5);
                    leftsweepMC.setMotorPower(LEFT,-speed);
                }
                if(offset>0 && leftsweepMC.getMotorPower(LEFT)!=-speed+.5){
                    rightpivotMC.setMotorPower(RIGHT, speed);
                    leftsweepMC.setMotorPower(LEFT,-speed+.5);
                }
            }
            if(5<Math.abs(offset) && Math.abs(offset)<10){
                telemetry.addData("offsetting",2);
                if(offset<0 && rightpivotMC.getMotorPower(RIGHT)!=speed-.7){
                    rightpivotMC.setMotorPower(RIGHT, speed-.7);
                    leftsweepMC.setMotorPower(LEFT,-speed);
                }
                if(offset>0 && leftsweepMC.getMotorPower(LEFT)!=-speed+.7){
                    rightpivotMC.setMotorPower(RIGHT, speed);
                    leftsweepMC.setMotorPower(LEFT,-speed+.7);
                }
            }
            if(Math.abs(offset)>=10){
                telemetry.addData("offsetting",3);
                if(offset<0 && rightpivotMC.getMotorPower(RIGHT)!=speed-.8){
                    rightpivotMC.setMotorPower(RIGHT, speed-.8);
                    leftsweepMC.setMotorPower(LEFT,-speed);
                }
                if(offset>0 && leftsweepMC.getMotorPower(LEFT)!=-speed+.8){
                    rightpivotMC.setMotorPower(RIGHT, speed);
                    leftsweepMC.setMotorPower(LEFT,-speed+.8);
                }
            }
        }
        leftsweepMC.setMotorPower(LEFT, 0);
        rightpivotMC.setMotorPower(RIGHT, 0);
    }

    public void turnLeft (int degrees, double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading-degrees;
        if(targetHeading<0){
            targetHeading=targetHeading+360;
        }
        while(gyro.getHeading()<targetHeading-2 || gyro.getHeading()>targetHeading+2){
            if(leftsweepMC.getMotorPower(LEFT)!=speed) {
                leftsweepMC.setMotorPower(LEFT, speed);
                rightpivotMC.setMotorPower(RIGHT, speed);
            }
            telemetry.addData("currentheading", gyro.getHeading());
            telemetry.addData("targetHeading", targetHeading);
        }
        leftsweepMC.setMotorPower(LEFT, 0);
        rightpivotMC.setMotorPower(RIGHT, 0);
    }

    public void turnRight (int degrees,double speed){
        int startHeading=gyro.getHeading();
        int targetHeading=startHeading+degrees;
        if(targetHeading>360){
            targetHeading=targetHeading-360;
        }
        while(gyro.getHeading()<targetHeading-2 || gyro.getHeading()>targetHeading+2){
            if(leftsweepMC.getMotorPower(LEFT)!=-speed) {
                leftsweepMC.setMotorPower(LEFT, -speed);
                rightpivotMC.setMotorPower(RIGHT, -speed);
            }
             telemetry.addData("currentheading", gyro.getHeading());
            telemetry.addData("targetHeading", targetHeading);
        }
        leftsweepMC.setMotorPower(LEFT,0);
        rightpivotMC.setMotorPower(RIGHT, 0);
    }

}
