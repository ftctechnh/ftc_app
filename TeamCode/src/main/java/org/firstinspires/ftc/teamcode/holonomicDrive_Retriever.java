package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="holonomicDrive Retriever", group="Testing")
public class holonomicDrive_Retriever extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;
    private ElapsedTime timer;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
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
                double trueX = robot.driveEngine.trueX;
                double trueY = robot.driveEngine.trueY;
                double angle = robot.driveEngine.spinAngle();
                double newX =  trueX * Math.cos(-angle) - trueY * Math.sin(-angle);
                double newY =  trueX * Math.sin(-angle) + trueY * Math.cos(-angle);
                if(robot.driveEngine.moveOnPath(new double[]{-newX, -newY}))
                    runBack = false;
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
            telemetry.addData("touchBottom", robot.sensors.touchBottomIsPressed());
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());

            robot.update();
            idle();
        }
    }
}

