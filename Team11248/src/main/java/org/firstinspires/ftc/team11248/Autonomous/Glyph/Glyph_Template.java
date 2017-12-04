package org.firstinspires.ftc.team11248.Autonomous.Glyph;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team11248.Robot11248;

/**
 * Created by Tony_Air on 11/29/17.
 */

public class Glyph_Template extends LinearOpMode{

    private int stage = 0;
    private int TICKS_TO_DOWN = 400;
    private int TICKS_UP = 20;
    @Override
    public void runOpMode() throws InterruptedException {

        Robot11248 robot = new Robot11248(hardwareMap, telemetry);
        robot.init();
        robot.calibrateGyro();
        robot.activateColorSensors();

        robot.vuforia.init();
        robot.vuforia.activateTracking();

        waitForStart();


        while (opModeIsActive() && !isStopRequested()) {

            switch (stage){

                case 1:


                    break;


                case 2:

                    break;



                case 3:

                    break;



                case 4:

                    break;


                case 5:

                    break;


            }
        }



    }
}
