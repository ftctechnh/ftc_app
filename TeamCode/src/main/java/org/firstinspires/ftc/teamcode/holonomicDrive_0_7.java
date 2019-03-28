package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="holonomicDrive Retriever", group="Testing")
public class holonomicDrive_0_7 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;
    private ElapsedTime timer;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(0);
        timer = new ElapsedTime();
        g1 = gamepad1;
        boolean runBack = false;
        waitForStart();

        while (opModeIsActive())
        {
            if(g1.x)
                runBack = true;

            if(runBack)
            {
                runBack = !robot.driveEngine.moveOnPath(DriveEngine.Positioning.Absolute,false,
                       new double[]{0,0});
            }
            else
                robot.manualDrive2(g1.left_stick_button,g1.left_stick_x, g1.left_stick_y, g1.right_stick_x);

            if(g1.dpad_down)
                robot.setBrake(Bogg.Direction.On);
            else if(g1.dpad_up)
                robot.setBrake(Bogg.Direction.Off);

            if(g1.left_bumper)
                robot.dropMarker(Bogg.Direction.Left);
            else if(g1.right_bumper)
                robot.dropMarker(Bogg.Direction.Right);


            robot.manualLift(g1.y, g1.a);

            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");

            robot.update();
            idle();
        }
    }
}

