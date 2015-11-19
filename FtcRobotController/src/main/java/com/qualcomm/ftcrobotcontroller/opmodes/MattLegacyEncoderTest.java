package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class MattLegacyEncoderTest extends OpMode {

    final static double DRIVE_POWER_RATE = 0.005;

    DcMotorController motorController3;
    DcMotor motor6;

    // 1. Create class variable
    HiTechnicMotorController hMotorController3;

    /**
     * Constructor
     */
    public MattLegacyEncoderTest() {

    }

    @Override
    public void init() {

        motor6 = hardwareMap.dcMotor.get("motor_6");
        //motor6.setDirection(DcMotor.Direction.REVERSE);

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

        // Matt: finger positions
        final double FingerBasePower = -0.5;
        final int FingerTolerance = 100;
        final int FingerStartPos = 0;
        final int FingerMiddlePos = -500;
        final int FingerDownPos = -1000;
        int currFingerPosition = 0;
        double fingerPower = 0.0;


        try {
// 7. read encoder value
            currFingerPosition = hMotorController3.getMotor2Encoder();
        } catch (Exception ex) {
            currFingerPosition = -1;
        }

        if (gamepad1.dpad_up && (Math.abs(FingerStartPos - currFingerPosition) > FingerTolerance)) {
            int fingerDirection = FingerStartPos - currFingerPosition > 0 ? -1 : 1;
            fingerPower = fingerDirection * FingerBasePower;
        } else if (gamepad1.dpad_right && (Math.abs(FingerMiddlePos - currFingerPosition) > FingerTolerance)) {
            int fingerDirection = FingerMiddlePos - currFingerPosition > 0 ? -1 : 1;
            fingerPower = fingerDirection * FingerBasePower;
        } else if (gamepad1.dpad_down && (Math.abs(FingerDownPos - currFingerPosition) > FingerTolerance)) {
            int fingerDirection = FingerDownPos - currFingerPosition > 0 ? -1 : 1;
            fingerPower = fingerDirection * FingerBasePower;
        } else {  // no dpad buttons pressed
            fingerPower = 0.0;
        }
        hMotorController3.setMotor2Power(fingerPower);

        // Matt: when colored buttons are pressed, use encoder target
        if (gamepad1.y) {
            motor6.setTargetPosition(FingerStartPos);
            motor6.setPower(-FingerBasePower);
        } else if (gamepad1.b) {
            motor6.setTargetPosition(FingerMiddlePos);
            motor6.setPower(FingerBasePower);
        } else if (gamepad1.a) {
            motor6.setTargetPosition(FingerDownPos);
            motor6.setPower(FingerBasePower);
        }

        telemetry.addData("pos", "Pos6: "+currFingerPosition);

// 9. call process method
        hMotorController3.process();
    }

    @Override
    public void stop() {

    }

}