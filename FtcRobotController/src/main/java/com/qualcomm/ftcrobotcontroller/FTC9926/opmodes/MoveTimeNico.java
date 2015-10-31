package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Nicolas Bravo on 10/30/15.
 */
public class MoveTimeNico extends HardwareMap9926{

    int move_state = 0;

    @Override
    public void init() {
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
                m_hand_position(0.2);

//                if (getRuntime() > 5){
//                    Motor1.setPower(0);
//                    Motor2.setPower(0);
                    move_state++;
//                }
                break;

            case 2:
                m_hand_position(0.4);
  //              Motor1.setPower(1);
  //              Motor2.setPower(0);
 //               if (getRuntime() > 1.5)
 //               {
  //                  Motor1.setPower(0);
  //                  Motor2.setPower(0);
                    move_state++;
 //               }
                break;

            case 3:
                m_hand_position(.8);
  //              Motor1.setPower(1);
  //              Motor2.setPower(1);
  //              if (getRuntime() > 5){
  //                  Motor1.setPower(0);
  //                  Motor2.setPower(0);
                    move_state++;
  //              }
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

    }

    @Override
    public void stop() {

    }
}
