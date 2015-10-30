package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Nicolas Bravo on 10/30/15.
 */
public class MoveTimeNico extends OpMode{

    DcMotor Motor1;
    DcMotor Motor2;

    int move_state =0;

    @Override
    public void init() {
        Motor1 = hardwareMap.dcMotor.get("M1");
        Motor2 = hardwareMap.dcMotor.get("M2");
        Motor2.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        switch (move_state)
        {
            case 0:
                /* might need to reset motor */
                move_state++;
                break;

            case 1:
                Motor1.setPower(1);
                Motor2.setPower(1);
                if (getRuntime() > 5){
                    Motor1.setPower(0);
                    Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 2:
                Motor1.setPower(1);
                Motor2.setPower(0);
                if (getRuntime() > 1.5)
                {
                    Motor1.setPower(0);
                    Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 3:
                Motor1.setPower(1);
                Motor2.setPower(1);
                if (getRuntime() > 5){
                    Motor1.setPower(0);
                    Motor2.setPower(0);
                    move_state++;
                }
                break;

            case 4:
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
                }
                break;
        }

    }

    @Override
    public void stop() {

    }
}
