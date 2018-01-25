package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;


@Autonomous(name = "Test auto")
public class AutoTest extends LinearOpMode
{
    Base _base = new Base();

    @Override
    public void runOpMode() throws InterruptedException
    {
        _base.init(hardwareMap , this);

        waitForStart();

        _base.drivetrain.setState(Drivetrain.State.FORWARD_FAST);

        _base.drivetrain.driveTo.setDestination(40);
        _base.drivetrain.driveTo.runSequentially();

        sleep(10_000);
    }
}
