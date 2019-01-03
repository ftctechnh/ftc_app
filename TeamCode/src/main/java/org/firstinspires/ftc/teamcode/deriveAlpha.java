package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="  find alpha values", group="Testing")
public class deriveAlpha extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();

        double lastTime = 0;
        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive())
        {
            doNormalStuff();

            double t = timer.seconds();
            double clockTime = t - lastTime;
            lastTime = t;

            //This should give us the alpha needed to reach 95% in 3 seconds.
            double alpha3 = 1 - Math.pow(.05, clockTime/3);

            double alpha5 = 1 - Math.pow(.05, clockTime/5);
            double alpha2 = 1 - Math.pow(.05, clockTime/2);

            // Display the current value
            telemetry.addData("clockTime: ", clockTime);
            telemetry.addData("derivedAlpha3: ", alpha3);
            telemetry.addData("derivedAlpha5: ", alpha5);
            telemetry.addData("derivedAlpha2: ", alpha2);
            telemetry.addData("alpha: ", robot.getAlpha());
            telemetry.update();
            idle();
        }
    }

    private void doNormalStuff()
    {
        if(gamepad1.right_stick_x != 0 )
        {
            robot.manualRotate(gamepad1);
        }
        else
        {
            robot.manualDriveAutoCorrect(gamepad1);
        }

        if(gamepad1.dpad_up)
        {
            robot.setBrake(true);
        }
        else if(gamepad1.dpad_down)
        {
            robot.setBrake(false);
        }


        if(gamepad1.left_bumper)
        {
            robot.dropMarker(Bogg.Direction.Left);
        }
        else if(gamepad1.right_bumper)
        {
            robot.dropMarker(Bogg.Direction.Right);
        }


        robot.manualLift();
    }
}

