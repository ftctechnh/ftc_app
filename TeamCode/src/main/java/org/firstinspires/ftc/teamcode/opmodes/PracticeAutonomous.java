package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/20/2017.
 */
@Autonomous(name="Practice Autonomous", group="Opmode")
public class PracticeAutonomous extends LinearOpMode
{
    Hardware9330 hwMap = new Hardware9330();

    @Override
    public void runOpMode() throws InterruptedException
    {
        hwMap.init(hardwareMap);
        hwMap.rightMotor.setPower(50);
        while (!hwMap.touch.isPressed())
        {

        }
        hwMap.rightMotor.setPower(0);
    }
}
