package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;

/**
 * Created by FnMat on 2/3/2018.
 */

@Autonomous(name = "test")
public class AutoTest extends LinearOpMode
{
    Base base = new Base();

    @Override
    public void runOpMode() throws InterruptedException
    {
        base.init(hardwareMap , this);

        waitForStart();

        base.drivetrain.driveTo.setParams(25 , 1 , 10_000);
        base.drivetrain.driveTo.runSequentially();
        base.drivetrain.driveTo.setParams(-25 , 1 , 10_000);
        base.drivetrain.driveTo.runSequentially();
    }
}
