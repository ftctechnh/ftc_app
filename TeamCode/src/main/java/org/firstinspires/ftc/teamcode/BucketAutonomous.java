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
public class BucketAutonomous extends LinearOpMode {
    EeyoreHardware robot = new EeyoreHardware();

    int teamColor = 3; //Not zero because I don't want color and teamColor to be equal initially

    int xVal, yVal, zVal = 0;     // Gyro rate Values
    int heading = 0;              // Gyro integrated heading
    int angleZ = 0;
    boolean lastResetState = false;
    boolean curResetState  = false;

    GyroSensor Gyro;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        telemetry.addData("Gyro Calibration:", "Running");
        telemetry.update();
        Gyro.calibrate();
        try {
            Thread.sleep(10000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        telemetry.addData("Gyro Calibration:", "Finished");
        telemetry.update();





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
        GyroMovement(0.3, 0, 750); //pull forward off the wall
        GyroMovement(0.3, 20, 0);



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            idle();
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


        int currentDirection = Gyro.getHeading();
        double turnMultiplier = 0.05;
        double driveMultiplier = 0.03;


        //First, check to see if we are pointing in the correct direction
        while(Math.abs(targetDirection - currentDirection) > 5) //If we are more than 5 degrees off target, make corrections before moving
        {
            currentDirection = Gyro.getHeading();

            int error = targetDirection - currentDirection;
            double speedAdjustment = turnMultiplier * error;

            double leftPower = .5 * Range.clip(speedAdjustment, -1, 1);
            double rightPower = .5 * Range.clip(-speedAdjustment, -1, 1);

            //Finally, assign these values to the motors

            robot.r1.setPower(rightPower);
            robot.r2.setPower(rightPower);
            robot.l1.setPower(leftPower);
            robot.l2.setPower(leftPower);
            String currentMode = "Turning";
            telemetry.addData("Current Mode", currentMode);
            telemetry.addData("PID Output", driveMultiplier);
            telemetry.addData("Gyro output", currentDirection);
            telemetry.addData("Heading", Gyro.getHeading());

            telemetry.update();
        }

        //Now we can move forward, making corrections as needed

        long startTime = System.currentTimeMillis(); //Determine the time as we enter the loop

        while(System.currentTimeMillis() - startTime < time) //Loop for the desired amount of time
        {
            currentDirection = Gyro.getHeading();
            int error = targetDirection - currentDirection;
            double speedAdjustment = driveMultiplier * error;

            double leftPower = Range.clip(speed + speedAdjustment, -1, 1);
            double rightPower = Range.clip(speed - speedAdjustment, -1, 1);

            //Finally, assign these values to the motors
            robot.r1.setPower(rightPower);
            robot.r2.setPower(rightPower);
            robot.l1.setPower(leftPower);
            robot.l2.setPower(leftPower);

            String currentMode = "Moving Forward";
            telemetry.addData("Current Mode", currentMode);
            telemetry.addData("rightPower", rightPower);
            telemetry.addData("leftPower", leftPower);
            telemetry.addData("PID Output", speedAdjustment);
            telemetry.addData("Gyro output", currentDirection);
            telemetry.update();
        }
        robot.r1.setPower(0);
        robot.r2.setPower(0);
        robot.l1.setPower(0);
        robot.l2.setPower(0);

    }
    public void GyroTest()
    {
        ModernRoboticsI2cGyro Gyro;   // Hardware Device Object
        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        Gyro.calibrate();
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        while(true)
        {
            telemetry.addData("Gyro Output", Gyro.getHeading());
            telemetry.update();
        }
    }

}
