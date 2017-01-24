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
    String leftBeaconColor;
    String rightBeaconColor;
    String returnedSide;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        telemetry.addData("Status:", "Initializing");
        telemetry.update();

        robot.gyro.calibrate();

        setCameraDownsampling(9);
        startCamera();

        Thread.sleep(6000);

        telemetry.addData("Status:", "Initialized (waiting for start)");
        telemetry.addData("Gyro:", "Finished");
        telemetry.update();

        //We need to determine what team we are on currently
        while(!gamepad1.a) //Keep checking until the driver presses a to confirm his team selection
        {
            if( gamepad1.x) //If the driver pushes x, set the team color to blue
            {
                teamColor = "BLUE";
            }

            if(gamepad1.b) //If the driver pushes b, set the team color to red
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

        robot.moveRobotGyro(0.25, 0, 1000); //pull forward off the wall
        Thread.sleep(1000);
        //At this point, we can try to score the pre-loaded balls
        robot.shootShooter(1);
        Thread.sleep(800);
        robot.shootShooter(0);
        Thread.sleep(1000);
        robot.shootShooter(1);
        Thread.sleep(1000);
        robot.shootShooter(0);
        //Now that both balls are scored, proceed to the beacon
        robot.moveRobotGyro(0.25, 90, 1000); //drive diagonally to line up w/ the beacon
        robot.moveRobotGyro(0.25, 0, 0);//Turn to line up on the bacons sideways

        /*Thread.sleep(2000);

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
            GyroMovement(0.3, 90, 800);
        }
        else if (firstBeaconSide == "RIGHT")//We need to push the right side
        {
            GyroMovement(0.3, 90, 750);
        }*/

        stopCamera();

        telemetry.addData("Status:", "Idling...");
        telemetry.addData("Returned Side:", returnedSide);
        telemetry.addData("Left Color:", leftBeaconColor);
        telemetry.addData("Right Color:", rightBeaconColor);
        telemetry.update();

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

        if(left_intensity > right_intensity) {
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
}
