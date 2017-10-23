package org.firstinspires.ftc.team2993;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team2993.structural.RobotHardware;


@TeleOp(name="Auto Best", group="test")
public class Autonomous extends LinearOpMode
{
    private RobotHardware robot;

    @Override
    public void runOpMode() throws InterruptedException
    {
        waitForStart();

        robot = new RobotHardware(hardwareMap);
        robot.init();

        ElapsedTime eTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        robot.claw.setPosition(0);
        while(eTime.time() < 1000) {}
        eTime.reset();

        robot.drive(-.5);
        while(eTime.time() < 2000) {}
    }
}
