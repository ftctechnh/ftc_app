package org.firstinspires.ftc.teamcode.Year_2017_18.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Year_2017_18.Robot.RobotHardware;

@Autonomous(name = "AutoDab", group="AutoMode")
public class AutoDab extends LinearOpMode
{
    RobotHardware hardware = new RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException
    {
        hardware.init(hardwareMap);
        telemetry.addData("Status", "The robot has successfully initialized! ");
        telemetry.update();
        waitForStart();
        telemetry.addData("Status", "The robot has started running!");
        telemetry.update();

        while (opModeIsActive())
        {
            hardware.leftClaw.setPosition(0);
            hardware.rightClaw.setPosition(1);
            sleep(1000);
            hardware.leftClaw.setPosition(1);
            hardware.rightClaw.setPosition(0);
            sleep(1000);
        }
    }
}