package org.firstinspires.ftc.teamcode.TestCode.ParallelCommandTest;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;



public class OpParallelCommandTest extends LinearOpMode
{
    public void runOpMode() throws InterruptedException
    {
        waitForStart();

        while(opModeIsActive())
        {
            telemetry.addData("Input" , "Left Joystick X: " + gamepad1.left_stick_x);
        }
    }
}
