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
        boolean rotating = false;
        g1 = gamepad1;
        waitForStart();

        while (opModeIsActive())
        {
            if(!robot.dPadOrbit(g1.dpad_left, g1.dpad_right)) //if we're not orbiting
            {
                if(g1.right_stick_x != 0 ) //if we are rotating
                {
                    robot.manualRotate(g1.right_stick_button, g1.right_stick_x);
                    rotating = true;
                }
                else if(rotating){  //if we're not rotating, but the boolean says so
                    rotating = false;
                    robot.driveEngine.resetDistances(); // we start auto correcting again
                }
                else
                {
                    robot.manualDriveAutoCorrect(g1);
                }
            }


            robot.manualBrake(g1.dpad_down, g1.dpad_up);

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

