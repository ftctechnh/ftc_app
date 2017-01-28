package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;

import static org.firstinspires.ftc.teamcode.EeyoreHardware.COUNTS_PER_INCH;

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

        teamColor = "BLUE";

        // Calibrate gyro
        robot.gyro.calibrate();

        while(robot.gyro.isCalibrating()) {
            Thread.sleep(50);
            idle();
        }

        Thread.sleep(2000);

        // Initiate camera
        setCameraDownsampling(9);
        startCamera();

        telemetry.addData("Status:", "Initialized (waiting for start)");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        telemetry.addData("Status:", "Moving...");
        telemetry.update();

        // Move off of the wall
        moveStraight(27);
        Thread.sleep(1000);

        // At this point, we can try to score the pre-loaded balls
        shootShooter(1);
        Thread.sleep(800);
        shootShooter(0);
        Thread.sleep(1000);
        shootShooter(1);
        Thread.sleep(1000);
        shootShooter(0);

        // Now we move on to the beacons
        gyroTurn(45);
        moveStraight(20);
        gyroTurn(90);
        moveStraight(40);
        Thread.sleep(1000);
        gyroTurn(0);
        Thread.sleep(1000);
        driveToLine();

        // Press the beacon
        String side = getBeaconSide();

        if(side == "LEFT") {
            pressLeftButton();
        } else if(side == "RIGHT") {
            pressRightButton();
        }

        telemetry.addData("Status:", "Shutting down...");
        telemetry.update();

        stopCamera();

        // Run until the end of the match (driver presses STOP)
        while(opModeIsActive()) {
            telemetry.addData("Status:", "Idling...");
            telemetry.addData("Returned Side:", returnedSide);
            telemetry.addData("Left Color:", leftBeaconColor);
            telemetry.addData("Right Color:", rightBeaconColor);
            telemetry.update();
            idle();
        }
    }

    public void shootShooter(int power) {
        robot.shooter1.setPower(power);
        robot.shooter2.setPower(power);
    }

    public void setDrivePower(double power) {
        robot.l1.setPower(power);
        robot.l2.setPower(power);
        robot.r1.setPower(power);
        robot.r2.setPower(power);
    }

    public void pressLeftButton() throws InterruptedException {
        robot.leftPresser.setPosition(0);
        Thread.sleep(1500);
        robot.leftPresser.setPosition(0.275);
    }

    public void pressRightButton() throws InterruptedException {
        robot.rightPresser.setPosition(0);
        Thread.sleep(1500);
        robot.rightPresser.setPosition(0.275);
    }

    public void gyroTurn(int degree) throws InterruptedException
    {
        int currentDirection = robot.gyro.getHeading();
        double turnMultiplier = 0.05;

        robot.l1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.r2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();

        // First, check to see if we are pointing in the correct direction
        while(Math.abs(degree - currentDirection) > 5) //If we are more than 5 degrees off target, make corrections before moving
        {
            currentDirection = robot.gyro.getHeading();

            int error = degree - currentDirection;
            double speedAdjustment = turnMultiplier * error;

            double leftPower = 0.20 * Range.clip(speedAdjustment, -1, 1);
            double rightPower = 0.20 * Range.clip(-speedAdjustment, -1, 1);

            // Finally, assign these values to the motors
            robot.r1.setPower(rightPower);
            robot.r2.setPower(rightPower);
            robot.l1.setPower(leftPower);
            robot.l2.setPower(leftPower);
        }

        setDrivePower(0);
    }

    public void moveStraight(double inches) throws InterruptedException {
        robot.l1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.l1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        idle();

        int targetLeft = robot.l1.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        int targetRight = robot.r1.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

        setDrivePower(0.25);

        while((robot.l1.getCurrentPosition() < targetLeft) && (robot.r1.getCurrentPosition() < targetRight)) {
            telemetry.addData("Target", "left=" + targetLeft + " " + "right=" + targetRight);
            telemetry.addData("Position", "left=" + robot.l1.getCurrentPosition() + " " + "right=" + robot.r1.getCurrentPosition());
            telemetry.addData("Power", "left=" + robot.l1.getPower() + " " + "right=" + robot.r1.getPower());
            telemetry.update();

            idle();
        }

        setDrivePower(0);
    }

    public void driveToLine() throws InterruptedException {
        while(robot.color.alpha() == 0) {
            setDrivePower(0.1);
            Thread.sleep(100);
            setDrivePower(0);
            telemetry.addData("Sensor Color", robot.color.alpha());
            telemetry.update();
            Thread.sleep(100);
        }

        setDrivePower(0);
    }

    public String getBeaconSide() {
        while (!imageReady()) {
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

        if(left == teamColor) {
            return "LEFT";
        } else if(right == teamColor) {
            return "RIGHT";
        } else {
            return "ERROR";
        }
    }
}
