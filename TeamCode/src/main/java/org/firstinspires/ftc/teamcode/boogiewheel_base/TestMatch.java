package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractMatch;

@Autonomous(name = "Test Match", group = "New")
//@Disabled

public class TestMatch extends AbstractMatch {

    @Override
    public void InitMatch() {
        SetupMatch(new TestAuton(), new BoogieTeleOp());
    }

    @Override
    public void StopMatch() {

    }
}
