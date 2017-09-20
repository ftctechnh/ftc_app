package org.firstinspires.ftc.teamcode.VelocityVortex;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by spmce on 11/3/2016.
 */
//@TeleOp (name = "Velocity Vortex TeleOp" , group = "TeleOp")
public class VelocityVortexTeleOp extends VelocityVortexHardware {

    private int pad1Config;
    private boolean isPad1XPressed;
    private boolean isPad1XReleased;
    private boolean isPad1YPressed;
    private boolean isPad1YReleased;
    private double longestTime = 0;

    VelocityVortexTelemetry tele = new VelocityVortexTelemetry();
    //@Override
    public void init() {
        super.init();
        //tele.initTele();
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
        //gyro.calibrate();
        pad1Config = 0;
    }
    public void init_loop() {
        super.init_loop();
        checkConfig();
        telemetry.addData("001101 pad1", pad1Config);
        //tele.initLoopTele();
    }
    //@Override
    public void loop() {
        super.loop();
        /*ElapsedTime time = new ElapsedTime();
        time.startTime();*/
        checkConfig();
        OmniWheelDrive omniWheels = new OmniWheelDrive();
        double[] wheels = new double[4];
        if (pad1Config == 0 || pad1Config == 3) {
            wheels = omniWheels.drive(gamepad1);
        } else if (pad1Config > 0) {
            wheels = omniWheels.drive(gamepad1,gyro);
        }
        // Set Drive Train Power
        leftDrivePower  = wheels[0];
        rightDrivePower = wheels[1];
        backRightPower  = wheels[2];
        backLeftPower   = wheels[3];
        mFL.setPower(leftDrivePower);
        mFR.setPower(rightDrivePower);
        mBR.setPower(backRightPower);
        mBL.setPower(backLeftPower);
        //MySweeper sweeper = new MySweeper();
        //double left = gamepad1.left_trigger;
        //double right = gamepad1.right_trigger;
        //sweeperPower = sweeper.sweep(left,right);
        //sweeperPower = -gamepad1.left_trigger + gamepad1.right_trigger;
        sweeperPower = -gamepad2.left_trigger + gamepad2.right_trigger;
        if (pad1Config >= 2) {
            sweeperPower += -gamepad1.left_trigger + gamepad1.right_trigger;
        }
        mSweeper.setPower(sweeperPower);
        if (gamepad1.a)
            sLeftBeacon.setPosition(.96);
        else
            sLeftBeacon.setPosition(initLeftBeacon);
        if (gamepad1.b)

            sRightBeacon.setPosition(0);
        else
            sRightBeacon.setPosition(initRightBeacon);

        if (gamepad2.a) {
            sLoaderStopper.setPosition(openLoaderStopper);
        }
        if (gamepad2.b)
            sLoaderStopper.setPosition(initLoaderStopper);

        if (gamepad2.x || gamepad1.dpad_up)
            sLoaderStopper.setPosition(openLoaderStopper);
        else {
            sLoaderStopper.setPosition(initLoaderStopper);
        }
        //tele.loopTele();
        //telemetry.addData("Touch",touch.isPressed());
        //telemetry.addData("Color 1 blue",color1.blue());
        //telemetry.addData("Color 1 red",color1.red());
        //shanesTelemetry tele = new shanesTelemetry();
        //launcherPower = -gamepad2.left_trigger + gamepad2.right_trigger;
        launcherPower = 0;
        if (pad1Config >= 2) {
            if (gamepad1.left_bumper)
                launcherPower -= 1;
            if (gamepad1.right_bumper)
                launcherPower += 1;
        }
        if (gamepad2.left_bumper)
            launcherPower -= 1;
        if (gamepad2.right_bumper)
            launcherPower += 1;
        mLauncher.setPower(launcherPower);
        telemetry.addData("001101 pad1", pad1Config);
        motorTele();
        servoTele();
        sensorTele();
        /*if (time.milliseconds() > longestTime) {
            longestTime = time.milliseconds();
        }
        telemetry.addData("time",time.milliseconds());
        telemetry.addData("time-longest", longestTime);
        time.reset();*/
    }
    void checkConfig() {
        if (gamepad1.start) {
            pad1Config = 0;
        }
        if (gamepad1.x) {
            isPad1XPressed = true;
            isPad1XReleased = false;
        }
        if (isPad1XPressed) {
            if (!gamepad1.x) {
                isPad1XReleased = true;
                isPad1XPressed = false;
            }
            if (isPad1XReleased) {
                pad1Config++;
            }
            if (pad1Config == 4) {
                pad1Config = 0;
            }
        }
        if (gamepad1.y) {
            isPad1YPressed = true;
            isPad1YReleased = false;
        }
        if (isPad1YPressed) {
            if (!gamepad1.y) {
                isPad1YReleased = true;
                isPad1YPressed = false;
            }
            if (isPad1YReleased) {
                pad1Config--;
            }
            if (pad1Config == -1) {
                pad1Config = 3;
            }
        }
    }
    void allTele() {
        telemetry.addData("001101", pad1Config);
        motorTele();;
        servoTele();
        //sensorTele();
        //encoderTele();
    }
    void motorTele() {
        telemetry.addData("fl", leftDrivePower);
        telemetry.addData("fr",rightDrivePower);
        telemetry.addData("bl", backLeftPower);
        telemetry.addData("br", backRightPower);
        telemetry.addData("sweeper",sweeperPower);
    }
    void servoTele() {
        telemetry.addData("rightBeacon", rightBeaconPosition);
        telemetry.addData("leftBeacon", leftBeaconPosition);
        telemetry.addData("loaderStopper", loaderStopperPosition);
    }
    void sensorTele() {
        //telemetry.addData("touch", touch.isPressed());
        telemetry.addData("touch double", touch.getValue());
        telemetry.addData("light1", light1.getLightDetected());
        telemetry.addData("light2", light2.getLightDetected());
        telemetry.addData("color1 red", color1.red());
        telemetry.addData("color1 blue", color1.blue());
        telemetry.addData("color2 red", color2.red());
        telemetry.addData("color2 blue", color2.blue());
        telemetry.addData("gyro heading", gyro.getHeading());
        //telemetry.addData("gyro rotate",gyro.getRotationFraction());
        //telemetry.addData("gyro x",gyro.rawX());
        //telemetry.addData("gyro y",gyro.rawY());
        //telemetry.addData("gyro z",gyro.rawZ());
        telemetry.addData("range", range.getDistance(DistanceUnit.MM));
        telemetry.addData("optical distance", od.getLightDetected());
        telemetry.addData("", "");
    }
}
