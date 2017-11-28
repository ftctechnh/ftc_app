package org.firstinspires.ftc.team11248;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Tony_Air on 12/11/15.
 */

@TeleOp(name = "Servo Debugger GOOD")
//@Disabled

public class ServoDebug extends OpMode {

    Servo servo1, servo2, servo3, servo4, servo5, servo6, servo7, servo8, servo9, servo10, servo11, servo12;

    int servo = 1;
    double servoPosition = 0;
    double increment = .05;
    boolean prevA, prevDPU, prevDPD;

    @Override
    public void init() {

        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo3 = hardwareMap.servo.get("servo3");
        servo4 = hardwareMap.servo.get("servo4");
        servo5 = hardwareMap.servo.get("servo5");
        servo6 = hardwareMap.servo.get("servo6");
        servo7 = hardwareMap.servo.get("servo7");
        servo8 = hardwareMap.servo.get("servo8");
        servo9 = hardwareMap.servo.get("servo9");
        servo10 = hardwareMap.servo.get("servo10");
        servo11 = hardwareMap.servo.get("servo11");
        servo12 = hardwareMap.servo.get("servo12");

    }

    @Override
    public void loop() {

        if(gamepad1.a && !prevA) servo++;
        prevA = gamepad1.a;

        if(servo == 13) servo = 1;


        if(gamepad1.dpad_down && !prevDPD && servoPosition - increment> 0) servoPosition -= increment;
        prevDPD = gamepad1.dpad_down;

        if(gamepad1.dpad_up && !prevDPU && servoPosition + increment < 1) servoPosition += increment;
        prevDPU = gamepad1.dpad_up;


        switch (servo){
            case 1:
                servo1.setPosition(servoPosition);
                break;

            case 2:
                servo2.setPosition(servoPosition);
                break;

            case 3:
                servo3.setPosition(servoPosition);
                break;

            case 4:
                servo4.setPosition(servoPosition);
                break;

            case 5:
                servo5.setPosition(servoPosition);
                break;

            case 6:
                servo6.setPosition(servoPosition);
                break;

            case 7:
                servo7.setPosition(servoPosition);
                break;

            case 8:
                servo8.setPosition(servoPosition);
                break;

            case 9:
                servo9.setPosition(servoPosition);
                break;

            case 10:
                servo10.setPosition(servoPosition);
                break;

            case 11:
                servo11.setPosition(servoPosition);
                break;

            case 12:
                servo12.setPosition(servoPosition);
                break;


        }

        telemetry.addData("01:", "Servo: " + servo);
        telemetry.addData("02:", "Position: " + servoPosition);
    }

}
