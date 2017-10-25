package org.firstinspires.ftc.teamcode.TestCode.SpoolsPrototype;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;


public class Base extends RobotBase
{
    Spools spools = new Spools();

    public void init(final HardwareMap HW)
    {
        hardware = HW;

        spools.init(this);
    }
}
