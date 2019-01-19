package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="holonomicDrive AutoCorrect", group="Testing")
public class holonomicDrive_0_1_5 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;
        waitForStart();

        while (opModeIsActive())
        {
            if(!robot.manualRotate(g1.right_stick_button, g1.right_stick_x)) //if we're not rotating
            {
                robot.manualDriveAutoCorrect(g1.left_stick_button, g1.left_stick_x, g1.left_stick_y);
            }
            else //if we are rotating
                robot.driveEngine.resetForward();


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

            telemetry.update();
            robot.update();
            idle();
        }
    }
}

