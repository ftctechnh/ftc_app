package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;


@TeleOp(name="Basic Bot", group="Iterative Opmode")  // @Autonomous(...) is the other common choice


public class BeaconFinderAuto extends LinearOpMode {
    BasicBotHardware robot = new BasicBotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("Status", "Running...");
        telemetry.update();
        //TODO: Code to reach the beacons

        //Now that we are in front of a beacon, we need to figure out the color
        int color = checkColor();
        if (color == 1)
        {

        }
        else if (color == -1)
        {

        }
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running...");
            telemetry.update();


            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }
    public static int checkColor()
    {
        //Now that we are in front of a beacon, we need to figure out the color
        String BeaconColor = "NULL";
        int ColorBlue = 0;
        int ColorRed = 0;
        if(Color.blue(ColorBlue) > Color.red(ColorRed))
        {
            return 1; //Beacon is blue
        }
        else if(Color.blue(ColorBlue) < Color.red(ColorRed))
        {
            return -1; //Beacon is red
        }
        else
        {
            return 0; //Beacon is ???
        }
    }
    public static void moveRobot(int speed, int distance)
    {

    }

}
