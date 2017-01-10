package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import org.firstinspires.ftc.teamcode.EeyoreHardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.PretendModernRoboticsUsbDevice;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;
import java.util.concurrent.TimeUnit;

@Autonomous(name="Beacon Finder", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
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

        //reachBeacon();
        //pushBeacons();
        GyroMovement(0.3, 90, 5000); //pull forward off the wall

        ModernRoboticsI2cGyro Gyro;   // Hardware Device Object
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        Gyro.calibrate();




        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            idle();
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
        GyroMovement(0.5, 0, 5000); //pull forward off the wall
    }

    public void GyroMovement(double speed, int targetDirection, int time) //Speed is from -1 to 1 and direction is 0 to 360 degrees
    {
        ModernRoboticsI2cGyro Gyro;   // Hardware Device Object
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        Gyro.calibrate();
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        int currentDirection = Gyro.getHeading();

        int error_prior = 0;
        int integral = 0;
        double P = 1;
        double I = 0.1;
        double D = 0.1;

        //First, check to see if we are pointing in the correct direction
        while(Math.abs(targetDirection - currentDirection) > 5) //If we are more than 5 degrees off target, make corrections before moving
        {
            currentDirection = Gyro.getHeading();
            int error = targetDirection - currentDirection;
            // In order to prevent issues when crossing from 360 to zero or vice versa,
            // we need to adjust error to compensate

            if(error > 360)
            {
                error = error - 360;
            }

            integral = integral + error;
            int derivative = error-error_prior;
            double pidOutput = 0.1 * P * error; //+ I * integral + D * derivative; //Value will be based off the amount of degrees we've deviated from the target, but there'll be some amount of modification done to this value

            //If pidOutput is negative, we need to turn left, and if it's positive we need to turn right

            double leftPower = -pidOutput;
            double rightPower = pidOutput;

            //Finally, assign these values to the motors

            robot.r1.setPower(rightPower);
            robot.r2.setPower(rightPower);
            robot.l1.setPower(leftPower);
            robot.l2.setPower(leftPower);
            String currentMode = "Turning";
            telemetry.addData("Current Mode", currentMode);
            telemetry.addData("rightPower", rightPower);
            telemetry.addData("leftPower", leftPower);
            telemetry.addData("PID Output", pidOutput);
            telemetry.addData("Gyro output", currentDirection);

            telemetry.update();
        }

        //Now we can move forward, making corrections as needed

        long startTime = System.currentTimeMillis(); //Determine the time as we enter the loop

        while(System.currentTimeMillis() - startTime < time) //Loop for the desired amount of time
        {
            currentDirection = Gyro.getHeading();
            int error = targetDirection - currentDirection;

            if(error > 360)
            {
                error = error - 360;
            }

            integral = integral + error;
            int derivative = error-error_prior;
            double pidOutput = P * error; //+ I * integral + D * derivative; //Value will be based off the amount of degrees we've deviated from the target, but there'll be some amount of modification done to this value
            //Now we just need to use this error value to determine our motor speeds
            //We need to determine whether we are turning left or right first
            double leftPower = speed; //+ (-pidOutput * 0.006);
            double rightPower = speed; //+ (pidOutput * 0.006);

            //Finally, assign these values to the motors
            robot.r1.setPower(rightPower);
            robot.r2.setPower(rightPower);
            robot.l1.setPower(leftPower);
            robot.l2.setPower(leftPower);

            String currentMode = "Moving Forward";
            telemetry.addData("Current Mode", currentMode);
            telemetry.addData("rightPower", rightPower);
            telemetry.addData("leftPower", leftPower);
            telemetry.addData("PID Output", pidOutput);
            telemetry.addData("Gyro output", currentDirection);
            telemetry.update();
        }
        robot.r1.setPower(0);
        robot.r2.setPower(0);
        robot.l1.setPower(0);
        robot.l2.setPower(0);

    }
}
