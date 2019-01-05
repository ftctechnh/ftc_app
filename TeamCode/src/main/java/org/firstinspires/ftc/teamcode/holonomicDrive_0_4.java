package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="holonomicDrive variable Orbit", group="Testing")
public class holonomicDrive_0_4 extends LinearOpMode
{
    Bogg robot;

    private boolean auto;
    private boolean orbit;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, gamepad2, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        waitForStart();

        while (opModeIsActive())
        {
            robot.spinEffector();

            if(gamepad1.x)
                orbit = true;
            else if(gamepad1.y)
                orbit = false;

            robot.manualDriveVarOrbit(gamepad1, gamepad1, orbit);

            if(gamepad1.dpad_up)
            {
                robot.setBrake(true);
            }
            else if(gamepad1.dpad_down)
            {
                robot.setBrake(false);
            }

            if(gamepad1.start)
            {
                robot.updateRadius();
                robot.driveEngine.resetDistances();
            }

            if(gamepad1.left_bumper)
            {
                robot.dropMarker(Bogg.Direction.Left);
            }
            else if(gamepad1.right_bumper)
            {
                robot.dropMarker(Bogg.Direction.Right);
            }

            if(gamepad2.start) {
                auto = true;
                robot.updateRadius();
                robot.driveEngine.resetDistances();
            }
            else if(gamepad2.left_stick_button || gamepad2.right_stick_button)
                auto = false;

            robot.manualEffect();
            if(auto)
                robot.autoEffect();



            robot.manualLift();

            // Display the current value
            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            telemetry.addData("touchBottom", robot.sensors.touchBottomIsPressed());
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());
            telemetry.addData("fixed distance", robot.sensors.getFixed());
            telemetry.addData("mobile distance", robot.sensors.getMobile());

            telemetry.update();
            idle();
        }
    }

}

