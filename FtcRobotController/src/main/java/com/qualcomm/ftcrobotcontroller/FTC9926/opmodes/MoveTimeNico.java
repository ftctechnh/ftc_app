package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Nicolas Bravo on 10/30/15.
 */
public class MoveTimeNico extends Telemetry9926{

    int move_state = 0;
    Servo Servo1;
    double SM1_Position;


    @Override
    public void init() {
        Servo1 = hardwareMap.servo.get("SM1");

    }

    @Override
    public void start() {

    }

    @Override public void loop()
    {


        switch (move_state)
        {
            case 0:
                /* might need to reset motor */
                move_state++;
                break;

            case 1:
                //m_hand_position(0.2);

                if (getRuntime() > 5){
//                    Motor1.setPower(0);
//                    Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 2:
                //a_hand_position();
                SM1_Position = 0.2;
                Servo1.setPosition(SM1_Position);

                //              Motor1.setPower(1);
  //              Motor2.setPower(0);
                if (getRuntime() > 15)
                {
  //                  Motor1.setPower(0);
  //                  Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 3:
                SM1_Position = 0.3;
                Servo1.setPosition(SM1_Position);
 //               m_hand_position(.8);
  //              Motor1.setPower(1);
  //              Motor2.setPower(1);
                if (getRuntime() > 20){
  //                  Motor1.setPower(0);
  //                  Motor2.setPower(0);
                    move_state++;
                }
                break;
            default:
                break;


    /*        case 4:
                Motor1.setPower(-1);
                Motor2.setPower(-1);
                if (getRuntime() > 5){
                    Motor1.setPower(0);
                    Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 5:
                Motor1.setPower(-1);
                Motor2.setPower(0);
                if (getRuntime() > 1.5)
                {
                    Motor1.setPower(0);
                    Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 6:
                Motor1.setPower(-1);
                Motor2.setPower(-1);
                if (getRuntime() > 5){
                    Motor1.setPower(0);
                    Motor2.setPower(0);
                    move_state++;
                }*/

                //break;
        }

        UpdateTelemetry();
        telemetry.addData("11", "State: " + move_state);
        telemetry.addData("12", "Time: " + getRuntime());
        telemetry.addData("13","Servo: " + SM1_Position);
    }

    @Override
    public void stop() {

    }
}
