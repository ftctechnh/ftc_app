package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.provider.Settings;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.EeyoreHardware.COUNTS_PER_INCH;

@Autonomous(name="Beacon Finder", group="Iterative Opmode")  // @Autonomous(...) is the other common choice
public class BeaconFinderAuto extends CameraProcessor {
    EeyoreHardware robot = new EeyoreHardware();

    String teamColor = "NONE"; //Initialized as NONE because I don't want color and teamColor to be equal initially
    String returnedSide;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        telemetry.addData("Status:", "Initializing");
        telemetry.update();

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

        while(!gamepad1.a && (teamColor == "NONE")) {
            if(gamepad1.b) {
                teamColor = "RED";
            } else if(gamepad1.x) {
                teamColor = "BLUE";
            }

            telemetry.addData("Team Color:", teamColor);
            telemetry.update();

            idle();
        }

        telemetry.addData("Status:", "Initialized (waiting for start)");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        telemetry.addData("Status:", "Moving...");
        telemetry.update();

        turnTester();

        /*
        // Move off of the wall
        moveStraight(26, 0.2);
        Thread.sleep(1000);
        gyroTurn(90);
        Thread.sleep(2000);
        gyroTurn(180);

        // At this point, we can try to score the pre-loaded balls
        /*shootShooter(1);
        Thread.sleep(800);
        shootShooter(0);
        Thread.sleep(1000);
        shootShooter(1);
        Thread.sleep(1000);
        shootShooter(0);

        // Now we move on to the beacons
        if(teamColor == "RED") { //We are red
            gyroTurn(315);
            moveStraight(19);
            gyroTurn(270);
            moveStraight(37);
            Thread.sleep(1000);
            gyroTurn(180);
            Thread.sleep(1000);

            robot.l1.setDirection(DcMotor.Direction.FORWARD);
            robot.l2.setDirection(DcMotor.Direction.FORWARD);
            robot.r1.setDirection(DcMotor.Direction.REVERSE);
            robot.r2.setDirection(DcMotor.Direction.REVERSE);

        } else { //We are blue
            gyroTurn(45);
            moveStraight(23);
            gyroTurn(90);
            moveStraight(33);
            Thread.sleep(1000);
            gyroTurn(0);
            Thread.sleep(1000);
        }

        // Press the beacons
        driveToLine();
        Thread.sleep(2000);

        String beacon1 = getBeaconSide();

        if(beacon1 == "LEFT") {
            pressLeftButton();
        } else if(beacon1 == "RIGHT") {
            pressRightButton();
        }

        // Move partly to the second beacon
        moveStraight(32);

        driveToLine();
        Thread.sleep(2000);

        String beacon2 = getBeaconSide();

        if(beacon2 == "LEFT") {
            pressLeftButton();
        } else if(beacon2 == "RIGHT") {
            pressRightButton();
        }

        telemetry.addData("Status:", "Shutting down...");
        telemetry.update();

        stopCamera();*/

        // Run until the end of the match (driver presses STOP)
        while(opModeIsActive()) {
            telemetry.addData("Status:", "Idling...");
            telemetry.addData("Returned Side:", returnedSide);
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

    public void driveLeft(double power) {
        robot.l1.setPower(-power);
        robot.l2.setPower(-power);
        robot.r1.setPower(power);
        robot.r2.setPower(power);
    }

    public void driveRight(double power) {
        robot.l1.setPower(power);
        robot.l2.setPower(power);
        robot.r1.setPower(-power);
        robot.r2.setPower(-power);
    }

    public void pressLeftButton() throws InterruptedException {
        robot.leftPresser.setPosition(0.2);
        Thread.sleep(5000);
        robot.leftPresser.setPosition(0.8);
    }

    public void pressRightButton() throws InterruptedException {
        robot.rightPresser.setPosition(0.2);
        Thread.sleep(5000);
        robot.rightPresser.setPosition(0.8);
    }

    public void turnTester() throws InterruptedException //Method to test the gyro code for pid calibration. Calls a bunch of Gyro Turns at varying degrees.
    {
        gyroTurn(90);
        Thread.sleep(3000);
        gyroTurn(-90);
        Thread.sleep(3000);
        gyroTurn(0);
        Thread.sleep(3000);
        gyroTurn(180);
        Thread.sleep(3000);
        gyroTurn(0);
    }

    public void gyroTurn(int target) throws InterruptedException
    {
        robot.l1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();

        double Kp = 1.25; //2.5 was the ideal time with no I or D
        double Kd = 1.25;
        double heading = robot.gyro.getHeading();
        double buffer = 3;
        double lastLoopTime = System.currentTimeMillis();
        double currentLoopTime = 0;
        double error_prior = 0;

        while(Math.abs(heading - target) > buffer ) {
            heading = robot.gyro.getHeading(); //Get the raw, signed z value
            currentLoopTime = System.currentTimeMillis();
            double iterationTime = currentLoopTime - lastLoopTime;
            /*if(target > heading) {
                power = Range.clip((1 - (Math.round((heading / target) * 100.0) / 100.0)) * 0.2, 0.25, 1);
                driveLeft(power);
            } else {
                power = Range.clip((1 - (Math.round((heading / target) * 100.0) / 100.0)) * 0.2, -1, -0.25);
                driveRight(power);
            }*/

            double error = (target - heading) / 360;
            double derivative = (error - error_prior) / (iterationTime + 0.0000000001);
            double output = Range.clip((Kp * error + Kd * derivative), -0.7, 0.7);

            robot.l1.setPower(output);
            robot.l2.setPower(output);
            robot.r1.setPower(-output);
            robot.r2.setPower(-output);

            telemetry.addData("Power:", output);
            telemetry.addData("Error: ", error);
            telemetry.addData("Derivative: ", derivative);
            telemetry.addData("Heading / Target:", heading + " / " + target);
            telemetry.update();

            if(!opModeIsActive())
            {
                break;
            }
            lastLoopTime = currentLoopTime;
            idle();
        }
        setDrivePower(0);
    }

    public void moveStraight(double inches, double power) throws InterruptedException {
        robot.l1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.l1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.r1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        idle();

        int targetLeft = robot.l1.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
        int targetRight = robot.r1.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

        setDrivePower(power);

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
        while(!imageReady()) {
            telemetry.addData("Camera:", "Waiting for image...");
            telemetry.update();
        }

        Bitmap image = convertYuvImageToRgb(yuvImage, size.width, size.height, 1);

        int left_intensity = 0;

        for(int x = 0; x < image.getWidth() / 2; x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                int pixel_blue = blue(pixel);

                left_intensity += pixel_blue;
            }
        }

        int right_intensity = 0;

        for(int x = image.getWidth() / 2; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                int pixel_blue = blue(pixel);

                right_intensity += pixel_blue;
            }
        }

        String left;
        String right;

        if(left_intensity < right_intensity) {
            left = "BLUE";
            right = "RED";
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