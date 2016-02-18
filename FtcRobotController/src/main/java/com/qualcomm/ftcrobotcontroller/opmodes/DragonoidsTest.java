package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DragonoidsTest extends OpMode {
    private DcMotor testMotor;
    private final double maxPower = 0.65;
    private final double midPower = 0.30;
    @Override
    public void init() {
        DragonoidsGlobal.init(hardwareMap);
        testMotor = hardwareMap.dcMotor.get("test");
    }
    @Override
    public void loop() {
        if (gamepad1.a){
            testMotor.setPower(midPower);
        }
        else if (gamepad1.x) {
            testMotor.setPower(maxPower);
        }
        else if (gamepad1.y) {
            testMotor.setPower(-midPower);
        }
        else if (gamepad1.b) {
            testMotor.setPower(-maxPower);
        }
        else {
            testMotor.setPower(0);
            DragonoidsGlobal.stopAll();
        }

        this.outputTelemetry();
    }
    private void outputTelemetry() {
        // No telemetry in this test program for the moment
    }
    @Override
    public void stop() {
        // Stop all motors
        DragonoidsGlobal.stopAll();
        super.stop();
    }
}