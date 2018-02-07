package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import android.text.method.Touch;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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
    DcMotor retractor;
    Servo left;
    Servo right;
    DigitalChannel retracted;
    DigitalChannel armUp;
    DigitalChannel armDown;
    //
    PengwinFin pengwinFin;
    //</editor-fold>
    //
    public PengwinWing(HardwareMap hardwareMap) {
        up = hardwareMap.dcMotor.get("up");
        extend = hardwareMap.dcMotor.get("extend");
        retractor = hardwareMap.dcMotor.get("retractor");
        left = hardwareMap.servo.get("left");
        right = hardwareMap.servo.get("right");
        retracted = hardwareMap.digitalChannel.get("retracted");
        armUp = hardwareMap.digitalChannel.get("armUp");
        armDown = hardwareMap.digitalChannel.get("armDown");
    }
    //
    public void setServos(boolean lefty, boolean righty){
        if (lefty){
            left.setPosition(-1); //closed
        }else {
            left.setPosition(1); //open
        }
        if (righty){
            right.setPosition(1); //closed
        }else {
            right.setPosition(0); //open
        }
    }
    //
    //<editor-fold desc="Arm">
    public void raiseArm(){
        up.setPower(.4);
        while (!armUp.getState()){}
        up.setPower(0);
    }
    //
    public void lowerArm(){
        up.setPower(-.4);
        while (!armDown.getState()){}
        up.setPower(0);
    }
    //
    public void manualArm(double power){
        if(power >= 0 && armUp.getState() || power < 0 && !armDown.getState()) {
            up.setPower(power);
        }else {
            up.setPower(0);
        }
    }
    //
    public void extendArm(double power){
        if (power >= 0 || (retracted.getState() && power <= 0)) {//retracted is reverse
            extend.setPower(power);
            if (power < 0) {
                retractor.setPower(-power * .3);
            }else {
                retractor.setPower(-power * .1);
            }
        }else{
            extend.setPower(0);
            retractor.setPower(0);
        }
    }
    //</editor-fold>
}
//This statement is false.
//Paradox