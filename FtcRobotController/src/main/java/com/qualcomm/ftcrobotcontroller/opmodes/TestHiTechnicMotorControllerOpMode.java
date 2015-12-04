package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TestHiTechnicMotorControllerOpMode extends OpMode {

    final static double DRIVE_POWER_RATE = 0.005;

    DcMotor driveMotorRight1;
    DcMotor driveMotorRight2;
    DcMotor driveMotorLeft1;
    DcMotor driveMotorLeft2;
    DcMotorController motorController3;
    DcMotor motor5;
    DcMotor motor6;

    int state = 0;
    int nextState = 0;
    int stateCnt = 0;

    double driveRightPowerTarget = 0.0;
    double driveLeftPowerTarget = 0.0;
    double driveRightPower = 0.0;
    double driveLeftPower = 0.0;

    int motor1Pos = 0;
    int motor2Pos = 0;
    int motor6Pos = 0;

    // 1. Create class variable
    HiTechnicMotorController hMotorController3;

    /**
     * Constructor
     */
    public TestHiTechnicMotorControllerOpMode() {

    }

    @Override
    public void init() {
        driveMotorLeft1 = hardwareMap.dcMotor.get("motor_3");
        driveMotorLeft2 = hardwareMap.dcMotor.get("motor_4");
        driveMotorRight1 = hardwareMap.dcMotor.get("motor_1");
        driveMotorRight2 = hardwareMap.dcMotor.get("motor_2");
        motor5 = hardwareMap.dcMotor.get("motor_5");
        motor6 = hardwareMap.dcMotor.get("motor_6");

        driveMotorLeft1.setDirection(DcMotor.Direction.REVERSE);
        driveMotorLeft2.setDirection(DcMotor.Direction.REVERSE);

        motorController3 = motor6.getController();

// 2. Instantiate class with motorController reference
        hMotorController3 = new HiTechnicMotorController(motorController3);
// 3. Turn on debug logging - Use the log viewer on robot controller to see logs.
        hMotorController3.setDebugLog(true);
// 4. Initialize encoder connection - for this example we only had an encoder on motor port 2.
        hMotorController3.resetMotor2Encoder();
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        nextState = state;
        switch(state){
            case 0:
                driveLeftPowerTarget = 0.0;
                driveRightPowerTarget = 0.0;
                if(stateCnt > 500){
                    nextState = 1;
                }
                break;
            case 1:
                driveLeftPowerTarget = 0.5;
                driveRightPowerTarget = 0.0;
                if(stateCnt > 500){
                    nextState = 2;
                }
                break;
            case 2:
                driveLeftPowerTarget = -0.5;
                driveRightPowerTarget = 0.0;
                if(stateCnt > 500){
                    nextState = 3;
                }
                break;
            case 3:
                driveLeftPowerTarget = 0.5;
                driveRightPowerTarget = 0.0;
                if(stateCnt > 500){
                    nextState = 4;
                }
                break;
            case 4:
                driveLeftPowerTarget = -0.5;
                driveRightPowerTarget = 0.0;
                if(stateCnt > 500){
                    nextState = 50;
                }
                break;
            case 50:
                driveLeftPowerTarget = 0.0;
                driveRightPowerTarget = 0.0;
                break;
        }

        if(nextState != state){
            state = nextState;
            stateCnt = 0;
        }
        else {
            stateCnt++;
        }

// ramp actual power

        if (driveLeftPowerTarget < driveLeftPower) {
            driveLeftPower -= DRIVE_POWER_RATE;
            if (driveLeftPower < driveLeftPowerTarget)
                driveLeftPower = driveLeftPowerTarget;
        } else {
            driveLeftPower += DRIVE_POWER_RATE;
            if (driveLeftPower > driveLeftPowerTarget)
                driveLeftPower = driveLeftPowerTarget;
        }

        if (driveRightPowerTarget < driveRightPower) {
            driveRightPower -= DRIVE_POWER_RATE;
            if (driveRightPower < driveRightPowerTarget)
                driveRightPower = driveRightPowerTarget;
        } else {
            driveRightPower += DRIVE_POWER_RATE;
            if (driveRightPower > driveRightPowerTarget)
                driveRightPower = driveRightPowerTarget;
        }

        if (state >= 0) {
            driveMotorLeft1.setPower(driveLeftPower);
            driveMotorLeft2.setPower(driveLeftPower);
            driveMotorRight1.setPower(driveRightPower);
            driveMotorRight2.setPower(driveRightPower);
// 5. set motor 1 power - 0.0 because not connected for example.
            hMotorController3.setMotor1Power(0.0);
// 6. set motor 2 power - ramp up and down
            hMotorController3.setMotor2Power(driveLeftPower);
            try {
                motor1Pos = driveMotorLeft1.getCurrentPosition();
                motor2Pos = driveMotorLeft2.getCurrentPosition();
// 7. read encoder value
                motor6Pos = hMotorController3.getMotor2Encoder();
            } catch (Exception ex) {

            }
        }

        telemetry.addData("state", "st: " + String.format("%3d", state) + ", stCnt: " + String.format("%3d", stateCnt));
        telemetry.addData("lPwr", "lPwrTgt: "+Double.toString(driveLeftPowerTarget) + ", lPwr: " + Double.toString(driveLeftPower));
// 8. display encoder value on telemetry
        telemetry.addData("pos", "lPos1: "+motor1Pos+", lPos2: "+motor2Pos+", Pos6: "+motor6Pos);

// 9. call process method
        hMotorController3.process();
    }

    @Override
    public void stop() {

    }

}