package org.firstinspires.ftc.teamcode.Shane;

/**
 * Created by team on 7/19/2017.
 */

public class MyTestCodeTelemetry extends MyTestCodeHardware {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
    }

    protected void myTelemetry() {
        telemetry.addData("Right Motor Power", rightPower);
        telemetry.addData("Left Motor Power", leftPower);
        telemetry.addData("Lift Motor Power", liftPower);
        telemetry.addData("Arm Motor Power", armPower);
    }
}
