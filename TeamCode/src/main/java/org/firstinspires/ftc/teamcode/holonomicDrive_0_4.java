package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="holonomicDrive Variable Orbit", group="Testing")
public class holonomicDrive_0_4 extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;
    Gamepad g2;

    private boolean auto = false;
    private boolean orbit = false;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        g1 = gamepad1;
        g2 = gamepad2;
        waitForStart();

        while (opModeIsActive())
        {
            if(g1.x)
                orbit = true;
            else if(g1.b)
                orbit = false;

            robot.manualDriveVarOrbit(g1, g1, orbit);

            if(g1.dpad_down)
                robot.setBrake(Bogg.Direction.On);
            else if(g1.dpad_up)
                robot.setBrake(Bogg.Direction.Off);

            if(g1.left_bumper)
                robot.dropMarker(Bogg.Direction.Left);
            else if(g1.right_bumper)
                robot.dropMarker(Bogg.Direction.Right);


            if(g2.start || g1.start) { //for either one we reset the orbit
                robot.updateRadius();
                robot.driveEngine.resetDistances();

                if(g2.start)    //gamepad 2 starts the auto movement
                    auto = true;
            }
            else if(g2.left_stick_button)
                auto = false;
            if(g2.right_stick_button)

            if(robot.manualEffect(g2)) //we can always change the arm manually
                auto = false;
            if(auto)
                robot.flipUp(); //we only do it autonomously when auto == true


            robot.manualLift(g1.y, g1.a);

            // Display the current value
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

