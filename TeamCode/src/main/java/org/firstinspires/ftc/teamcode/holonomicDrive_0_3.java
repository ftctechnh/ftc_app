package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="holonomicDrive Experimental Two Gamepads", group="Testing")
public class holonomicDrive_0_3 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;
    Gamepad g2;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, gamepad2, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;
        g2 = gamepad2;
        waitForStart();

        while (opModeIsActive())
        {
            if(!robot.dPadOrbit(g1.dpad_left, g1.dpad_right)) //if not orbiting
                robot.manualCurvy(g1, g2);

            robot.manualBrake(g1.dpad_down, g1.dpad_up);

            if(g1.left_bumper)
            {
                robot.dropMarker(Bogg.Direction.Left);
            }
            else if(g1.right_bumper)
            {
                robot.dropMarker(Bogg.Direction.Right);
            }


            robot.manualLift(g1.y, g1.a);

            // Display the current value
            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            telemetry.addData("touchBottom", robot.sensors.touchBottomIsPressed());
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());

            telemetry.update();
            idle();
        }
    }

}

