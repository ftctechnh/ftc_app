package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="  camera_test", group="Testing")
public class camera_test extends LinearOpMode
{
    Camera camera;

    @Override
    public void runOpMode()
    {
        camera = new Camera(hardwareMap, telemetry, false, true);
        waitForStart();

        int goldPosition = -1;
        while (opModeIsActive())
        {
            int g = camera.getGoldPosition();
            telemetry.addData("gold position", g);
            if(g != -1)
                goldPosition = g;
            telemetry.addData("saved", goldPosition);
            telemetry.update();
            idle();
        }
    }
}

