package org.firstinspires.ftc.teamcode.TestCode.ParallelCommandTest;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilToggle;


@TeleOp(name = "Parallel Command Test" , group = "Prototypes")
@Disabled
@SuppressWarnings("unused")
public class OpTest extends LinearOpMode
{
    private UtilToggle toggleCommand = new UtilToggle();

    private CountCommand command = new CountCommand();

    public void runOpMode() throws InterruptedException
    {
        waitForStart();

        while(opModeIsActive())
        {
            if(toggleCommand.isPressed(gamepad1.a))
            {
                if(command.isRunning())
                {
                    command.stop();
                }
                else
                {
                    command.runParallel();
                }
            }

            telemetry.addData("Input" , "Left Joystick X: " + gamepad1.left_stick_x);
            telemetry.update();
        }
    }
}
