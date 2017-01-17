package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;

import java.lang.reflect.Array;

@Autonomous(name="Beacon Finder", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
public class BeaconFinderAuto extends CameraProcessor {
    EeyoreHardware robot = new EeyoreHardware();

    String teamColor = "NONE"; //Not zero because I don't want color and teamColor to be equal initially

    GyroSensor Gyro;
    ColorSensor color_sensor;
    String leftBeaconColor;
    String rightBeaconColor;
    String returnedSide;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        telemetry.addData("Status:", "Initializing");
        telemetry.update();

        Gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        telemetry.addData("Gyro:", "Running");
        telemetry.update();

        Gyro.calibrate();

        setCameraDownsampling(9);
        startCamera();

        Thread.sleep(6000);

        telemetry.addData("Status:", "Initialized (waiting for start)");
        telemetry.addData("Gyro:", "Finished");
        telemetry.update();

        //We need to determine what team we are on currently
        while(!gamepad1.a) //Keep checking until the driver presses a to confirm his team selection
        {
            if ( gamepad1.x) //If the driver pushes x, set the team color to blue
            {
                teamColor = "BLUE";
            }

            if (gamepad1.b) //If the driver pushes b, set the team color to red
            {
                teamColor = "RED";
            }

            telemetry.addData("Team Color:", teamColor);
            telemetry.update();
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        telemetry.addData("Status:", "Moving...");
        telemetry.update();
        if(teamColor == "BLUE") //We are blue team

        {
            GyroMovement(0.25, 0, 1050); //pull forward off the wall
            Thread.sleep(1000);
            //At this point, we can try to score the pre-loaded balls
            robot.shooter1.setPower(1);
            robot.shooter2.setPower(1);
            Thread.sleep(800);
            robot.shooter1.setPower(0);
            robot.shooter2.setPower(0);
            Thread.sleep(1000);
            robot.shooter1.setPower(1);
            robot.shooter2.setPower(1);
            Thread.sleep(1000);
            robot.shooter1.setPower(0);
            robot.shooter2.setPower(0);
            GyroMovement(0.25, 0, 750);
            GyroMovement(0.25, 90, 200);
            GyroMovement(0.25, 0, 500);
            /*
            //Now that both balls are scored, proceed to the beacon
            GyroMovement(0.25, 45, 1850); //drive diagonally to line up w/ the beacon
            GyroMovement(0.3, 90, 750); //pull closer to teh beacon so we can

            Thread.sleep(2000);

            telemetry.addData("Status:", "Detecting beacon color...");
            telemetry.update();

            String firstBeaconSide = getBeaconSide();
            returnedSide = firstBeaconSide;
            telemetry.addData("Returned Side:", returnedSide);
            telemetry.addData("Left Color:", leftBeaconColor);
            telemetry.addData("Right Color:", rightBeaconColor);
            telemetry.update();

            if (firstBeaconSide == "LEFT") //We need to push the left side
            {
                GyroMovement(0.2, 0, 1000);
                Thread.sleep(1000);
                GyroMovement(0.3, 90, 800);
                Thread.sleep(1000);
            }
            else if (firstBeaconSide == "RIGHT")//We need to push the right side
            {
                GyroMovement(0.3, 90, 800);
                Thread.sleep(1000);
            }

            stopCamera();

            telemetry.addData("Status:", "Idling...");
            telemetry.addData("Returned Side:", returnedSide);
            telemetry.addData("Left Color:", leftBeaconColor);
            telemetry.addData("Right Color:", rightBeaconColor);
            telemetry.update();
       */
        }
        else //We are RED team
        {
            GyroMovement(0.25, 0, 1000); //pull forward off the wall
            Thread.sleep(1000);
            //At this point, we can try to score the pre-loaded balls
            robot.shooter1.setPower(1);
            robot.shooter2.setPower(1);
            Thread.sleep(800);
            robot.shooter1.setPower(0);
            robot.shooter2.setPower(0);
            Thread.sleep(1000);
            robot.shooter1.setPower(1);
            robot.shooter2.setPower(1);
            Thread.sleep(1000);
            robot.shooter1.setPower(0);
            robot.shooter2.setPower(0);
            GyroMovement(0.25, 0, 750);
            Thread.sleep(1000);
            GyroMovement(0.25, 90, 200);
            Thread.sleep(1000);
            GyroMovement(0.25, 0, 1000);
            Thread.sleep(1000);
            /*
            //Now that both balls are scored, proceed to the beacon
            GyroMovement(0.25, 315, 1450); //drive diagonally to line up w/ the beacon
            GyroMovement(0.3, 270, 750); //pull closer to teh beacon so we can determine color

            Thread.sleep(2000);

            telemetry.addData("Status:", "Detecting beacon color...");
            telemetry.update();

            String firstBeaconSide = getBeaconSide();
            returnedSide = firstBeaconSide;
            telemetry.addData("Returned Side:", returnedSide);
            telemetry.addData("Left Color:", leftBeaconColor);
            telemetry.addData("Right Color:", rightBeaconColor);
            telemetry.update();

            if (firstBeaconSide == "LEFT") //We need to push the left side
            {
                GyroMovement(0.2, 0, 750);
                GyroMovement(0.3, 270, 800);
            }
            else if (firstBeaconSide == "RIGHT")//We need to push the right side
            {
                GyroMovement(0.3, 280, 750);
            }

            stopCamera();

            telemetry.addData("Status:", "Idling...");
            telemetry.addData("Returned Side:", returnedSide);
            telemetry.addData("Left Color:", leftBeaconColor);
            telemetry.addData("Right Color:", rightBeaconColor);
            telemetry.update();
            */
        }


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status:", "Idling...");
            telemetry.addData("Returned Side:", returnedSide);
            telemetry.addData("Left Color:", leftBeaconColor);
            telemetry.addData("Right Color:", rightBeaconColor);
            telemetry.update();
            idle();
        }
    }

    public String getBeaconSide() {
        while(!imageReady()) {
            telemetry.addData("Camera:", "Waiting for image...");
            telemetry.update();
        }

        Bitmap image = convertYuvImageToRgb(yuvImage, size.width, size.height, 1);

        int left_intensity = 0;

        for(int x = 0; x < image.getWidth() / 2; x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                int pixel_red = red(pixel);
                int pixel_blue = blue(pixel);

                if(pixel_red > 200 && pixel_blue < 200) {
                    left_intensity += pixel_blue;
                }
            }
        }

        int right_intensity = 0;

        for(int x = image.getWidth() / 2; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                int pixel_red = red(pixel);
                int pixel_blue = blue(pixel);

                if(pixel_red > 200 && pixel_blue < 200) {
                    right_intensity += pixel_blue;
                }
            }
        }

        String left;
        String right;

        if(left_intensity < right_intensity) {
            left = "BLUE";
            right = "RED";
            leftBeaconColor = left;
            rightBeaconColor = right;
        } else {
            left = "RED";
            right = "BLUE";
        }

        if(left == teamColor)
        {
            return "LEFT";
        }
        else if (right == teamColor)
        {
            return "RIGHT";
        }
        else {
            return "ERROR";
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

    public void GyroMovement(double speed, int targetDirection, int time) //Speed is from -1 to 1 and direction is 0 to 360 degrees
    {


        int currentDirection = Gyro.getHeading();
        double turnMultiplier = 0.05;
        double driveMultiplier = 0.03;

        long turnStartTime = System.currentTimeMillis();
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
