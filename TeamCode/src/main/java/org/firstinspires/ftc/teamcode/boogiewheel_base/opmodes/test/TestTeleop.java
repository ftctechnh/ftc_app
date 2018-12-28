package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractTeleop;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;

@TeleOp(name = "Test Teleop", group = "New")
@Disabled

public class TestTeleop extends AbstractTeleop {
    @Override
    public void RegisterEvents() {
        addEventHandler("a_down", () -> {
            telemetry.addData("a down");
            telemetry.update();
            return true;
        });
    }

    @Override
    public void UpdateEvents() {

    }

    @Override
    public void Init() {
        telemetry.addData(DoubleTelemetry.LogMode.INFO, "TEST");
        telemetry.update();
    }

    @Override
    public void Loop() {
        telemetry.setLogMode(DoubleTelemetry.LogMode.TRACE);
        telemetry.addData(DoubleTelemetry.LogMode.TRACE, "Trace");
        telemetry.addData(DoubleTelemetry.LogMode.INFO, "Info");
        telemetry.addData(DoubleTelemetry.LogMode.DEBUG, "Debug");
        telemetry.addData(DoubleTelemetry.LogMode.ERROR, "Error");
        telemetry.update();
    }

    @Override
    public void Stop() {

    }
}
