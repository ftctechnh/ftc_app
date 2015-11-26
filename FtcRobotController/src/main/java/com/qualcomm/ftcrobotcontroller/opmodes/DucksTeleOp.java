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
public class DucksTeleOp extends OpMode {
    static int WINCH=1;
    static int WINCHPIVOT=2;
    static int WINCHWHEEL=1;
    static int RIGHT=2;
    static int LEFT=1;
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        DcMotorController driveMC=hardwareMap.dcMotorController.get("driveMC");
        DcMotorController winchMC=hardwareMap.dcMotorController.get("winchMC");
        DcMotorController wheelMC=hardwareMap.dcMotorController.get("wheelMC");

        Servo climbersLeft=hardwareMap.servo.get("climbersleft");
        Servo climbersRight=hardwareMap.servo.get("climbersright");

//left wheel
        if(gamepad1.left_stick_y>.05 && driveMC.getMotorPower(LEFT)!=gamepad1.left_stick_y){
            driveMC.setMotorPower(LEFT,Math.pow(gamepad1.left_stick_y, 2));
            telemetry.addData("updating",1);
        }else if(gamepad1.left_stick_y<.05 && driveMC.getMotorPower(LEFT)!=gamepad1.left_stick_y) {
            driveMC.setMotorPower(LEFT,-Math.pow(gamepad1.left_stick_y, 2));
            telemetry.addData("updating", 1);
        } else if(Math.abs(gamepad1.left_stick_y)<.05 && driveMC.getMotorPower(LEFT)!=0){
            driveMC.setMotorPower(LEFT,0);
            telemetry.addData("updating", 1);
        }
//right wheel
        if(gamepad1.right_stick_y>.05 && driveMC.getMotorPower(RIGHT)!=gamepad1.right_stick_y){
            driveMC.setMotorPower(RIGHT,-Math.pow(gamepad1.right_stick_y,2));
            telemetry.addData("updating", 1);
        } else if(gamepad1.right_stick_y<.05 && driveMC.getMotorPower(RIGHT)!=gamepad1.right_stick_y){
            driveMC.setMotorPower(RIGHT,Math.pow(gamepad1.right_stick_y,2));
            telemetry.addData("updating", 1);
        }else if(Math.abs(gamepad1.right_stick_y)<.05 && driveMC.getMotorPower(RIGHT)!=0){
            driveMC.setMotorPower(RIGHT,0);
            telemetry.addData("updating", 1);
        }
//winch in and out
        if(gamepad1.a && winchMC.getMotorPower(WINCH)!=1){
            winchMC.setMotorPower(WINCH,1);
            wheelMC.setMotorPower(WINCHWHEEL, .2);
            telemetry.addData("updating", 1);
        } else if(gamepad1.b && winchMC.getMotorPower(WINCH)!=-1){
            winchMC.setMotorPower(WINCH, -1);
            wheelMC.setMotorPower(WINCHWHEEL,-1);
            telemetry.addData("updating", 1);
        } else if(!gamepad1.a && !gamepad1.b && winchMC.getMotorPower(WINCH)!=0){
            winchMC.setMotorPower(WINCH, 0);
            wheelMC.setMotorPower(WINCHWHEEL,0);
            telemetry.addData("updating", 1);
        }
//winch up and down
        if(gamepad1.y && winchMC.getMotorPower(WINCHPIVOT)!=.2){
            winchMC.setMotorPower(WINCHPIVOT,.2);
            telemetry.addData("updating", 1);
        } else if(gamepad1.x && winchMC.getMotorPower(WINCHPIVOT)!=-.1){
            winchMC.setMotorPower(WINCHPIVOT,-.1);
            telemetry.addData("updating", 1);
        } else if(!gamepad1.y && !gamepad1.x && winchMC.getMotorPower(WINCHPIVOT)!=0) {
            winchMC.setMotorPower(WINCHPIVOT,0);
            telemetry.addData("updating", 1);
        }
        //right climbers up
        if(gamepad1.dpad_up){
            climbersRight.setPosition(1);
        }
        //right climbers down
        if(gamepad1.dpad_right){
            climbersRight.setPosition(.25);
        }
        //left climbers up
        if(gamepad1.dpad_down){
            climbersLeft.setPosition(.3);
        }
        //left climbers down
        if(gamepad1.dpad_left){
            climbersLeft.setPosition(1);
        }
    }
    @Override
    public void stop(){

    }
}
