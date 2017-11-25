package org.firstinspires.ftc.teamcode.TestCode.CoreTest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;


class TestBase extends RobotBase
{
    TestComponent component = new TestComponent();

    @Override
    public void init(HardwareMap HW , OpMode OPMODE)
    {
        super.init(HW , OPMODE);

        component.init(this);
    }
}
