package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.Looper;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcRobotControllerService;
import com.qualcomm.ftccommon.Restarter;
import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.FileInputStream;
import java.util.logging.Handler;

/**
 * Created by Dan on 11/9/2015.
 */
public class DucksTeleOp extends TeleOpCommands {
    int UPDATES=0;
    long leftDown;
    long leftUp;
    long rightDown;
    long rightUp;
    @Override
    public void init() {
        driveMC=hardwareMap.dcMotorController.get("driveMC");
        winchMC=hardwareMap.dcMotorController.get("winchMC");
        wheelMC=hardwareMap.dcMotorController.get("wheelMC");
    }

    @Override
    public void loop() {
//left wheel
        if(gamepad1.left_stick_y>0 && driveMC.getMotorPower(LEFT)!=scaleInput(gamepad1.left_stick_y)){
//            driveMC.setMotorPower(LEFT,Math.pow(gamepad1.left_stick_y, 2));
            driveMC.setMotorPower(LEFT,scaleInput(gamepad1.left_stick_y));
            UPDATES+=1;
        }else if(gamepad1.left_stick_y<0 && driveMC.getMotorPower(LEFT)!=-scaleInput(gamepad1.left_stick_y)) {
//            driveMC.setMotorPower(LEFT,-Math.pow(gamepad1.left_stick_y, 2));
            driveMC.setMotorPower(LEFT,-scaleInput(gamepad1.left_stick_y));
            UPDATES+=1;
        }
//right wheel
        if(gamepad1.right_stick_y>0 && driveMC.getMotorPower(RIGHT)!=-scaleInput(gamepad1.right_stick_y)){
//            driveMC.setMotorPower(RIGHT,-Math.pow(gamepad1.right_stick_y,2));
            driveMC.setMotorPower(RIGHT,-scaleInput(gamepad1.right_stick_y));
            UPDATES+=1;
        } else if(gamepad1.right_stick_y<-.05 && driveMC.getMotorPower(RIGHT)!=scaleInput(gamepad1.right_stick_y)){
//            driveMC.setMotorPower(RIGHT,Math.pow(gamepad1.right_stick_y,2));
            driveMC.setMotorPower(RIGHT,scaleInput(gamepad1.right_stick_y));
            UPDATES+=1;
        }
//winch in and out
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
//winch up and down
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
        telemetry.addData("UPDATES",UPDATES);
        telemetry.addData("climbersLeft",climbersLeft.getPosition());
        telemetry.addData("climbersRight", climbersRight.getPosition());
    }
    @Override
    public void stop(){

    }
}
