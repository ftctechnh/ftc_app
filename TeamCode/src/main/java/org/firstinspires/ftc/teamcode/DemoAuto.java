package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;



import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by FTC Team 4799-4800 on 10/13/2016.
 */

@Autonomous(name = "DemoAuto", group = "")
public class DemoAuto extends OpMode {
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    ColorSensor cs;
    ColorSensor cs2;
    Servo buttonPusher;
    double time;
    boolean timeSet = false;
    boolean followTime = false;
    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        cs = hardwareMap.colorSensor.get("cs");
        cs2 = hardwareMap.colorSensor.get("cs2");
        buttonPusher = hardwareMap.servo.get("buttonPusher");



    }

    public void loop() {
        if (Math.abs(6000-motorBackLeft.getCurrentPosition())>100) {
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(1);
            motorFrontLeft.setPower(-1);
            motorFrontRight.setPower(1);
            if (cs.blue()>20) {
                motorBackLeft.setPower(-.5);
                motorBackRight.setPower(1);
                motorFrontLeft.setPower(-.5);
                motorFrontRight.setPower(1);
                followTime = true;
            }
            else if (followTime){
                motorBackLeft.setPower(-1);
                motorBackRight.setPower(.5);
                motorFrontLeft.setPower(-1);
                motorFrontRight.setPower(.5);
            }
        }
        else{
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }

        if (cs2.red()>200 || cs2.blue()>200){
            if (cs2.red()>200)
                buttonPusher.setPosition(-1);
            else
                buttonPusher.setPosition(1);
            if (timeSet == false){
                time = getRuntime();
                timeSet = true;
            }
            if (time != 0 && getRuntime()-time>2) {
                motorBackLeft.setPower(-1);
                motorBackRight.setPower(1);
                motorFrontLeft.setPower(-1);
                motorFrontRight.setPower(1);
            }
        }
    }
}

