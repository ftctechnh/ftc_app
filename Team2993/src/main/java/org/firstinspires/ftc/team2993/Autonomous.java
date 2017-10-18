package org.firstinspires.ftc.team2993;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.team2993.structural.RobotHardware;



@TeleOp(name="Auto", group="test")
public class Autonomous extends LinearOpMode
{
    private RobotHardware robot;

    @Override
    public void runOpMode() throws InterruptedException
    {
        robot = new RobotHardware(hardwareMap);
        robot.init();


    }
}
