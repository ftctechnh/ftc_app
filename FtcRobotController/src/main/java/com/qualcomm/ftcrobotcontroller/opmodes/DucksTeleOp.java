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
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.FileInputStream;
import java.util.logging.Handler;

/**
 * Created by Dan on 11/9/2015.
 */
public class DucksTeleOp extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        DcMotor left= hardwareMap.dcMotor.get("left");
        DcMotor right= hardwareMap.dcMotor.get("right");
        DcMotor winch=hardwareMap.dcMotor.get("winch");
        DcMotor winchpivot=hardwareMap.dcMotor.get("winchpivot");
        DcMotor winchwheel=hardwareMap.dcMotor.get("winchwheel");
        Servo climbersLeft=hardwareMap.servo.get("climbersleft");
        Servo climbersRight=hardwareMap.servo.get("climbersright");
//left wheel
        if(gamepad1.left_stick_y>.05){
            left.setPower(Math.pow(gamepad1.left_stick_y, 2));
        }else if(gamepad1.left_stick_y<.05) {
            left.setPower(-Math.pow(gamepad1.left_stick_y, 2));
        } else{
            left.setPower(0);
        }
//right wheel
        if(gamepad1.right_stick_y>.05){
            right.setPower(-Math.pow(gamepad1.right_stick_y,2));
        } else if(gamepad1.right_stick_y<.05){
            right.setPower(Math.pow(gamepad1.right_stick_y,2));
        }else{
            right.setPower(0);
        }
//winch in and out
        if(gamepad1.a){
            winch.setPower(1);
            winchwheel.setPower(.2);
        } else if(gamepad1.b){
            winch.setPower(-1);
            winchwheel.setPower(-1);
        } else{
            winch.setPower(0);
            winchwheel.setPower(0);
        }
//winch up and down
        if(gamepad1.y){
            winchpivot.setPower(.2);
        } else if(gamepad1.x){
            winchpivot.setPower(-.2);
        } else {
            winchpivot.setPower(0);
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
