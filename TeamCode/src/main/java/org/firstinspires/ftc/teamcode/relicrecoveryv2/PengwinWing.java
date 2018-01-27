package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import android.text.method.Touch;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.HardwareSensorMap;
import org.firstinspires.ftc.teamcode.TouchSensorCheck;

/**
 * Created by Nora and Eric on 12/30/2017.
 */

public class PengwinWing{
    //<editor-fold desc="Declare n' stuff">
    DcMotor up;
    DcMotor extend;
    Servo left;
    Servo right;
    TouchSensor retracted;
    TouchSensor armUp;
    TouchSensor armDown;
    //</editor-fold>
    //
    public PengwinWing(HardwareMap hardwareMap) {
        up = hardwareMap.dcMotor.get("up");
        extend = hardwareMap.dcMotor.get("extend");
        left = hardwareMap.servo.get("left");
        right = hardwareMap.servo.get("right");
        retracted = hardwareMap.touchSensor.get("retracted");
        armUp = hardwareMap.touchSensor.get("armUp");
        armDown = hardwareMap.touchSensor.get("armDown");
        up.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //
    public void setServos(boolean lefty, boolean righty){
        if (lefty){
            left.setPosition(.8); //closed
        }else {
            left.setPosition(-1); //open
        }
        if (righty){
            right.setPosition(.8); //closed
        }else {
            right.setPosition(-1); //open
        }
    }
    //
    //<editor-fold desc="Arm">
    public void raiseArm(){
        up.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        up.setTargetPosition(90);
    }
    //
    public void lowerArm(){
        up.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        up.setTargetPosition(0);
    }
    //
    public void manualArm(double power){
        up.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(power >= 0 && !armUp.isPressed() || power < 0 && armDown.isPressed()) {
            up.setPower(power);
        }
    }
    //
    public void extendArm(double power){
        if (power >= 0 || !retracted.isPressed() && power < 0) {
            extend.setPower(power);
        }
    }
    //</editor-fold>
}
//This statement is false.
//Paradox
