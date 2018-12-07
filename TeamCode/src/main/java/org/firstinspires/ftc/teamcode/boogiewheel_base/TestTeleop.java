package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractTeleop;

@TeleOp(name = "Test Teleop", group = "New")
//@Disabled

public class TestTeleop extends AbstractTeleop {
    @Override
    public void RegisterEvents() {
        addEventHandler("a_down",()->{
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

    }

    @Override
    public void Loop() {

    }

    @Override
    public void Stop() {

    }
}
