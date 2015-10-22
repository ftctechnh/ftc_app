package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ExampleDriveInSquareStateMachine extends OpMode{

    DcMotor leftMotor;
    DcMotor rightMotor;
    ElapsedTime time;

    static final double forwardTime = 1.0;
    static final double turnTime = 1.0;
    int count = 0;

    enum State {drivingStraight, turning, done};
    State state;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        time = new ElapsedTime();
        state = State.drivingStraight;
    }

    @Override
    public void loop() {
        double currentTime = time.time();
        switch(state) {
            case drivingStraight:
                leftMotor.setPower(0.5);
                rightMotor.setPower(0.5);
                if (currentTime > forwardTime) {
                    state = State.turning;
                    time.reset();
                }
                break;
            case turning:
                leftMotor.setPower(0.5);
                rightMotor.setPower(-0.5);
                if (currentTime > turnTime) {
                    count++;
                    if(count < 4) {
                        state = State.drivingStraight;
                    } else {
                        state = State.done;
                    }
                    time.reset();
                }
                break;
            case done:
                leftMotor.setPower(0);
                rightMotor.setPower(0);
        }
    }
}

