package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import org.firstinspires.ftc.teamcode.EeyoreHardware;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;


@TeleOp(name="Basic Bot", group="Iterative Opmode")  // @Autonomous(...) is the other common choice


public class BeaconFinderAuto extends LinearOpMode {
    EeyoreHardware robot = new EeyoreHardware();

    int color = 0;
    int teamColor = 3; //Not zero because I don't want color and teamColor to be equal initially

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        //We need to determine what team we are on currently
        while(!gamepad1.a) //Keep checking until the driver presses a to confirm his team selection
        {
            if ( gamepad1.b) //If the driver pushes b, set the team color to blue
            {
                teamColor = 1;
            }
            if (gamepad1.x) //If the driver pushes x, set the team color to red
            {
                teamColor = -1;
            }
            telemetry.addData("Team Color is:", teamColor);
            telemetry.update();
        }
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        telemetry.addData("Status", "Running...");
        telemetry.update();
        reachBeacon();//TODO: Code to reach the beacons
        pushBeacons();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running...");
            telemetry.update();


            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }

    public void pushBeacons() {
        //Now that we are in front of the first beacon, we need to figure out the color
        robot.init(hardwareMap);
        int ColorBlue = 0;
        int ColorRed = 0;
        if (Color.blue(ColorBlue) > Color.red(ColorRed)) {
            color = 1; //Beacon is blue
        } else if (Color.blue(ColorBlue) < Color.red(ColorRed)) {
            color = -1; //Beacon is red
        } else {
            color = 0; //Beacon color is unknown for some reason (This should NEVER happen, unless red and blue are exactly equal
        }
        if (color == teamColor)//If the side of the beacon we are viewing is our color
        {
            //Push this side so we can score
        }
        else if (color == -1)
        {
            //Push the other side to score
        }
    }

    public void moveRobot(int speed, int time)
    {
        robot.r1.setPower(speed);
        robot.r2.setPower(speed);
        robot.l1.setPower(speed);
        robot.l2.setPower(speed);


    }
    public void reachBeacon()
    {

    }
}
