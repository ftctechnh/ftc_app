package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.reflect.Array;

/**
 * Created by Dan on 12/2/2015.
 */
public class TeleOpCommands extends OpMode {
    public static int WINCH=1;
    public static int WINCHPIVOT=2;
    public static int WINCHWHEEL=1;
    public static int RIGHT=2;
    public static int LEFT=1;
    public int UPDATES=0;
    public GyroSensor gyro;
    public DcMotorController winchMC;
    public DcMotorController driveMC;
    public DcMotorController wheelMC;
    public Servo climbersLeft;
    public Servo climbersRight;
    public ColorSensor color;
    public long LEFTUPDATE=0;
    public long RIGHTUPDATE=0;

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        // index should be positive.
        if (index < 0) {
            index = -index;
        }
        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }
        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        // return scaled value.
        return dScale;
    }
    public void setLeftPower(){
        if(gamepad1.left_stick_y>0 && driveMC.getMotorPower(LEFT)!=scaleInput(gamepad1.left_stick_y)){
            driveMC.setMotorPower(LEFT,scaleInput(gamepad1.left_stick_y));
            UPDATES+=1;
            LEFTUPDATE=System.currentTimeMillis();
        }else if(gamepad1.left_stick_y<0 && driveMC.getMotorPower(LEFT)!=-scaleInput(gamepad1.left_stick_y)) {
            driveMC.setMotorPower(LEFT,-scaleInput(gamepad1.left_stick_y));
            UPDATES+=1;
            LEFTUPDATE=System.currentTimeMillis();
        }
    }

    public void setRightPower(){
        if(gamepad1.right_stick_y>0 && driveMC.getMotorPower(RIGHT)!=-scaleInput(gamepad1.right_stick_y)){
            driveMC.setMotorPower(RIGHT,-scaleInput(gamepad1.right_stick_y));
            UPDATES+=1;
            RIGHTUPDATE=System.currentTimeMillis();
        } else if(gamepad1.right_stick_y<-.05 && driveMC.getMotorPower(RIGHT)!=scaleInput(gamepad1.right_stick_y)){
            driveMC.setMotorPower(RIGHT,scaleInput(gamepad1.right_stick_y));
            UPDATES+=1;
            RIGHTUPDATE=System.currentTimeMillis();
        }
    }
    public void setWinchPower(){
        if(gamepad1.a && winchMC.getMotorPower(WINCH)!=1){
            winchMC.setMotorPower(WINCH,1);
            wheelMC.setMotorPower(WINCHWHEEL, .2);
            UPDATES+=1;
        } else if(gamepad1.b && winchMC.getMotorPower(WINCH)!=-1){
            winchMC.setMotorPower(WINCH, -1);
            wheelMC.setMotorPower(WINCHWHEEL,-1);
            UPDATES+=1;
        } else if(!gamepad1.a && !gamepad1.b && winchMC.getMotorPower(WINCH)!=0){
            winchMC.setMotorPower(WINCH, 0);
            wheelMC.setMotorPower(WINCHWHEEL,0);
            UPDATES+=1;
        }
    }
    public void setPivotPower(){
        if(gamepad1.y && winchMC.getMotorPower(WINCHPIVOT)!=.2){
            winchMC.setMotorPower(WINCHPIVOT,.2);
            UPDATES+=1;
        } else if(gamepad1.x && winchMC.getMotorPower(WINCHPIVOT)!=-.1){
            winchMC.setMotorPower(WINCHPIVOT,-.1);
            UPDATES+=1;
        } else if(!gamepad1.y && !gamepad1.x && winchMC.getMotorPower(WINCHPIVOT)!=0) {
            winchMC.setMotorPower(WINCHPIVOT,0);
            UPDATES+=1;
        }
    }
    public void setClimbersPosition(){
        //right climbers up
        if(gamepad1.dpad_down && Math.abs(climbersRight.getPosition()-1)>.1){
            climbersRight.setPosition(1);
            UPDATES+=1;
        }
        //right climbers down
        if(gamepad1.dpad_right && Math.abs(climbersRight.getPosition() - .25)>.1){
            climbersRight.setPosition(.25);
            UPDATES+=1;
        }
        //left climbers up
        if(gamepad1.dpad_up && Math.abs(climbersLeft.getPosition() - .3)>.1){
            climbersLeft.setPosition(.3);
            UPDATES+=1;
        }
        //left climbers down
        if(gamepad1.dpad_left && Math.abs(climbersLeft.getPosition()-1)>.1){
            climbersLeft.setPosition(1);
            UPDATES+=1;
        }
    }
    @Override
    public void init() {
    }
    @Override
    public void loop() {
    }

}
