package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="holonomicDrive Curvy", group="Testing")
public class holonomicDrive_0_2 extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        waitForStart();

        Gamepad g1 = gamepad1;
        Gamepad g2 = gamepad2;

        while (opModeIsActive())
        {
            robot.manualCurvy(g1, g1);

            if(g1.dpad_down)
                robot.setBrake(Bogg.Direction.On);
            else if(g1.dpad_up)
                robot.setBrake(Bogg.Direction.Off);


            if(g1.left_bumper)
                robot.dropMarker(Bogg.Direction.Left);
            else if(g1.right_bumper)
                robot.dropMarker(Bogg.Direction.Right);


            robot.manualLift(g1.y, g1.a);

            robot.manualEffect(g2); //we can always change the arm manually

            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            telemetry.addData("back encoder inches", robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
            telemetry.addData("touchBottom", robot.sensors.touchBottomIsPressed());
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());

            telemetry.update();
            robot.update();
            idle();
        }
    }
}

