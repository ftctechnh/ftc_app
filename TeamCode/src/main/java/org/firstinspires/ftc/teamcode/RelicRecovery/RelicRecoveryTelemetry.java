package org.firstinspires.ftc.teamcode.RelicRecovery;

/**
 * Created by Shane on 13-11-2017.
 */

public class RelicRecoveryTelemetry extends RelicRecoveryHardware {

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
    }

    protected void myTelemetry() {
        //motors
        telemetry.addData("Right Motor Power", rightPower);
        telemetry.addData("Left Motor Power", leftPower);
        telemetry.addData("Lift Motor Power", liftPower);
        telemetry.addData("Arm Motor Power", armPower);
        telemetry.addData("Arm Lifter Power", armLifterPwr);

        //servos
        telemetry.addData("Ball Pusher Position", ballPusherPosition);
        telemetry.addData("One Hand Position", oneHandPosition);
        telemetry.addData("CR Hand Position", crHandPosition);
        telemetry.addData("relic grabber position", relicPosition);
        telemetry.addData("ArmLifterS position",armLifterSPosition);
        telemetry.addData("Arm position", armPosition);
    }
}
