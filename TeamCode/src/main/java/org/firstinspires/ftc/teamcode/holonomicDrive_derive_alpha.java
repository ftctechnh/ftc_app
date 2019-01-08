package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="  derive alpha", group="Testing")
public class holonomicDrive_derive_alpha extends LinearOpMode
{
    Bogg robot;

    Gamepad g1 = gamepad1;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();
        g1 = gamepad1;

        double lastClockTime = 0;
        double lastTime = 0;
        double averageClockTime = 0;
        double n = 0;
        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive())
        {
            n += 1;
            doNormalStuff();

            double t = timer.seconds();
            double clockTime = .5 * lastClockTime + .5 * (t - lastTime); // exponential average
            lastTime = t;

            if(averageClockTime == 0)
                averageClockTime = clockTime;
            averageClockTime = (clockTime + n * averageClockTime) / (n+1); //cumulative average


            //This should give us the alpha needed to reach 95% in 3 seconds.
            double alpha3 = 1 - Math.pow(.05, averageClockTime/3);

            double alpha15 = 1 - Math.pow(.05, averageClockTime/1.5);
            double alpha2 = 1 - Math.pow(.05, averageClockTime/2);

            // Display the current value
            telemetry.addData("clockTime: ", clockTime);
            telemetry.addData("averageClockTime: ", averageClockTime);
            telemetry.addData("derivedAlpha1.5: ", alpha15);
            telemetry.addData("derivedAlpha2: ", alpha2);
            telemetry.addData("derivedAlpha3: ", alpha3);
            telemetry.addData("alpha: ", robot.getAlpha());
            telemetry.update();
            lastClockTime = clockTime;

            idle();
        }
    }

    private void doNormalStuff()
    {
        if(!robot.dPadOrbit(g1.dpad_left, g1.dpad_right)) //if we're not orbiting
        {
            if(!robot.manualRotate(g1.right_stick_button, g1.right_stick_x)) //if we're not rotating
            {
                robot.manualDrive(g1.left_stick_button, g1.left_stick_x, g1.left_stick_y);
            }
        }

        robot.manualBrake(g1.dpad_down, g1.dpad_up);


        if(g1.left_bumper)
            robot.dropMarker(Bogg.Direction.Left);
        else if(g1.right_bumper)
            robot.dropMarker(Bogg.Direction.Right);


        robot.manualLift(g1.y, g1.a);
    }
}

