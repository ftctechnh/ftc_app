package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 10/15/2017.
 */

public class TestSpinPivot extends LinearOpMode
{
    private TankBase robot;

    public void runOpMode() throws InterruptedException
    {
        robot = new TankBase(hardwareMap);
        waitForStart();

    }
}
