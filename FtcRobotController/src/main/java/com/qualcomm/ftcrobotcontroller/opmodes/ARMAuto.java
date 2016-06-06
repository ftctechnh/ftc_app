package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ARMAuto extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    enum State { FWD, TURN, STOP}

    @Override
    public void runOpMode() throws InterruptedException {

        boolean endCondition = false;
        State current_state = State.STOP;

        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (!endCondition) {

            //@TODO Update sensor model

            //@TODO Update state conditions

            switch (current_state) {
                case FWD:
                    //@TODO Use PD control to drive straight
                    //@TODO update current state
                    break;
                case TURN:
                    //@TODO Use P control to turn desired angle
                    //@TODO update current state
                    break;
                case STOP:
                    //@TODO stop wheels
                    //@TODO update current state
                    break;
                default:
                    current_state = State.STOP;
                    break;
            }
        }
    }
}

