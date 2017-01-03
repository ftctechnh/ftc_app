package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import org.firstinspires.ftc.teamcode.EeyoreHardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.PretendModernRoboticsUsbDevice;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;



public class BeaconFinderAuto extends LinearOpMode {
    EeyoreHardware robot = new EeyoreHardware();

    int color = 0;
    int teamColor = 3; //Not zero because I don't want color and teamColor to be equal initially

    int xVal, yVal, zVal = 0;     // Gyro rate Values
    int heading = 0;              // Gyro integrated heading
    int angleZ = 0;
    boolean lastResetState = false;
    boolean curResetState  = false;





    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

//        ModernRoboticsI2cGyro Gyro;   // Hardware Device Object
//
//        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
//
//        Gyro.calibrate();
//
//        while (Gyro.isCalibrating())  {
//            Thread.sleep(50);
//            idle()
//          }





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
        try {   //Not sure this portion will work, we need to test it and find out if it's accurate
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }
    public void reachBeacon()
    {
        //This function will get the beacon

    }

    public void GyroAutonomous(int speed, int direction, int time) //Speed is from -1 to 1 and direction is 0 to 360 degrees
    {
        ModernRoboticsI2cGyro Gyro;   // Hardware Device Object
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        Gyro.calibrate();

        int currentHeading;
        long startTime = System.currentTimeMillis(); //Determine the time as we enter the loop

        int error_prior = 0;
        int integral = 0;
        double P = 1;
        double I = 0.1;
        double D = 0.1;

        while(System.currentTimeMillis() - startTime < time) //Loop for the desired amount of time
        {
            currentHeading = Gyro.getHeading();
            int error = direction - currentHeading;
            integral = integral + error;
            int derivative = error-error_prior;
            double pidOutput = P * error + I * integral + D * derivative; //Value will be based off the amount of degrees we've deviated from the target, but there'll be some amount of modification done to this value
            //Now we just need to use this error value to determine our motor speeds
            //We need to determine whether we are turning left or right first
            if (0 < direction - currentHeading)
            {
                double leftPower = speed + (pidOutput * 0.2);
                double rightPower = -speed - (pidOutput * 0.2);
            }
            else
            {
                double leftPower = -speed - (pidOutput * 0.2);
                double rightPower = speed + (pidOutput * 0.2);
            }
        }

    }
}
