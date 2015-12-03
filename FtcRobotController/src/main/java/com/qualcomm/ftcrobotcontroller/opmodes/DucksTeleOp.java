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
    @Override
    public void init() {
        driveMC=hardwareMap.dcMotorController.get("driveMC");
        winchMC=hardwareMap.dcMotorController.get("winchMC");
        wheelMC=hardwareMap.dcMotorController.get("wheelMC");
    }

    @Override
    public void loop() {
        if(System.currentTimeMillis()-LEFTUPDATE>10){
            setLeftPower();
        }
        if(System.currentTimeMillis()-RIGHTUPDATE>10){
            setRightPower();
        }
        setWinchPower();
        setPivotPower();
        setClimbersPosition();

        telemetry.addData("UPDATES",UPDATES);
        telemetry.addData("climbersLeft",climbersLeft.getPosition());
        telemetry.addData("climbersRight", climbersRight.getPosition());
    }
    @Override
    public void stop(){

    }
}
