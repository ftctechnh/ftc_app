package org.firstinspires.ftc.teamcode.TestCode.CoreTest;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Core.RobotBase;


class TestBase extends RobotBase
{
    TestComponent component = new TestComponent();

    @Override
    public void init(HardwareMap HW)
    {
        hardware = HW;
        component.init(this);
    }
}
