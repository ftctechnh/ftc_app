package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.FilterLib;

/**
 * Created by Noah on 3/19/2018.
 */

@TeleOp(name="Loop Timing")
@Disabled
public class PingCheck extends OpMode {

    long lastMillis = 0;
    long lastStamp = 0;
    boolean gamepadChanged = false;
    FilterLib.MovingWindowFilter filter = new FilterLib.MovingWindowFilter(10);

    public void init() {

    }

    public void start() {
        lastStamp = gamepad1.timestamp;
    }

    public void loop() {
        long nowTime = System.currentTimeMillis();
        filter.appendValue(nowTime - lastMillis);
        telemetry.addData("Loop Time", filter.currentValue());
        telemetry.addData("Last Stamp", lastStamp);
        telemetry.addData("Delta Time", nowTime - lastStamp);
        telemetry.addData("Gamepad Changed", gamepadChanged);
        lastMillis = nowTime;
        if(!gamepadChanged && gamepad1.timestamp != lastStamp) gamepadChanged = true;
        lastStamp = gamepad1.timestamp;
        telemetry.addData("Interval", telemetry.getMsTransmissionInterval());
    }

    public void stop() {

    }
}
