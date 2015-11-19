package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class MattHiTechnicMotorControllerOpMode extends OpMode {

    final static double DRIVE_POWER_RATE = 0.005;

    DcMotorController motorController3;
    DcMotor motor6;

    int state = 0;
    int nextState = 0;
    int stateCnt = 0;

    double driveRightPowerTarget = 0.0;
    double driveLeftPowerTarget = 0.0;
    double driveRightPower = 0.0;
    double driveLeftPower = 0.0;

    int motor6Pos = 0;

    // 1. Create class variable
    HiTechnicMotorController hMotorController3;

    /**
     * Constructor
     */
    public MattHiTechnicMotorControllerOpMode() {

    }

    @Override
    public void init() {

        motor6 = hardwareMap.dcMotor.get("motor_6");

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


// 5. set motor 1 power - 0.0 because not connected for example.
            hMotorController3.setMotor1Power(0.0);
// 6. set motor 2 power - ramp up and down
            hMotorController3.setMotor2Power(driveLeftPower);
            try {
// 7. read encoder value
                motor6Pos = hMotorController3.getMotor2Encoder();
            } catch (Exception ex) {

            }

        telemetry.addData("pos", "Pos6: "+motor6Pos);

// 9. call process method
        hMotorController3.process();
    }

    @Override
    public void stop() {

    }

}