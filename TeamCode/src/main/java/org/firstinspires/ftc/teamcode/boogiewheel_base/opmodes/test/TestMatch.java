package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractMatch;

@TeleOp(name = "Test Match", group = "New")
@Disabled

public class TestMatch extends AbstractMatch {

    @Override
    public void InitMatch() {
        SetupMatch("Test Auton", "Test Teleop");
    }

    @Override
    public void StopMatch() {

    }
}
