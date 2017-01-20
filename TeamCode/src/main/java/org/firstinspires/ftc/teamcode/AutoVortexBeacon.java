package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.modules.AccelerationIntegrator;
import org.firstinspires.ftc.teamcode.modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.modules.State;
import org.firstinspires.ftc.teamcode.modules.StateMachine;

import java.util.Locale;

@Autonomous
public class AutoVortexBeacon extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private ColorSensor color;

    private StateMachine main;
    private StateMachine searchForBeacon;
    private StateMachine pushBeacon;

    private double delay = 0;

    private boolean blue = false;

    private double heading = 0;

    private StateMachine shooter;

    @Override
    public void init() {
        robot.init(hardwareMap);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new AccelerationIntegrator();

        robot.imu = hardwareMap.get(BNO055IMU.class, "imu");
        robot.imu.initialize(parameters);

        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooter = new StateMachine(
                new State("off") {
                    @Override
                    public void run() {
                        robot.shooter.setPower(0);
                    }
                },
                new State("on") {
                    @Override
                    public void run() {
                        robot.shooter.setPower(HardwareVortex.SHOOTER_POWER);
                    }
                }
        );

        main = new StateMachine(

                new State("stop") {
                    @Override
                    public void run() {
                        move(0);
                    }
                },

                new State("drive to vortex") {
                    @Override
                    public void run() {
                        move(-0.5);
                        shooter.changeState("on");
                        sendData("shooter start", time);
                        changeState("reach vortex");
                    }
                },

                new State("reach vortex") {
                    @Override
                    public void run() {
                        if (reachedDestination(1750, 3000)) {
                            move(0);
                            changeState("shoot the ball");
                        }
                    }
                },

                new State("shoot the ball") {
                    @Override
                    public void run() {
                        double elapsedTime = getDouble("shooter start") - time;
                        if (elapsedTime > 3.0) {
                            robot.intake.setPower(1);
                            sendData("shooting time", time);
                            changeState("turn to line");
                        }
                    }
                },

                new State("turn to line") {
                    @Override
                    public void run() {
                        double elapsedTime = getDouble("shooting time") - time;
                        if (elapsedTime > 5.0) {
                            robot.intake.setPower(0);
                            shooter.changeState("off");
                            turn(-0.5);
                            changeState("drive tp line");
                        }
                    }
                },

                new State("drive to line") {
                    @Override
                    public void run() {
                        if (turnedDegrees(70, 3000)) {
                            move(0.5);
                            changeState("turn");
                        }
                    }
                },

                new State("turn") {
                    @Override
                    public void run() {
                        if (reachedDestination(1750, 3000)) {
                            turn(1);
                            changeState("drive to wall");
                        }
                    }
                },

                new State("drive to wall") {
                    @Override
                    public void run() {
                        if (turnedDegrees(1000000000, 10000)) {
                            move(0.5);
                            changeState("sense color");
                        }
                    }
                },

                new State("sense color") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 3000)) {
                            changeState("push button");
                        }
                    }
                },

                new State("push button") {
                    @Override
                    public void run() {
                        changeState("back up");
                    }
                },

                new State("back up") {
                    @Override
                    public void run() {
                        move(-0.5);
                        changeState("turn to other line");
                    }
                },

                new State("turn to other line") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 3000)) {
                            turn(-1);
                            changeState("drive to other line");
                        }
                    }
                },

                new State("drive to other line") {
                    @Override
                    public void run() {
                        if (turnedDegrees(90, 2000)) {
                            move(0.5);
                            changeState("turn on other line");
                        }
                    }
                },

                new State("turn to other line") {
                    @Override
                    public void run() {
                        if (reachedDestination(2000, 5000)) {
                            turn(0.5);
                            changeState("drive down other line");
                        }
                    }
                },

                new State("drive down other line") {
                    @Override
                    public void run() {
                        if (turnedDegrees()){

                        }
                    }
                },

        new State("turn to cap ball") {
                    @Override
                    public void run() {

                    }
                },

                new State("drive to ball") {
                    @Override
                    public void run() {

                    }
                }
        );

    }

    @Override
    public void init_loop() {
        if (gamepad1.a) {
            delay = 0;
        }
        else if (gamepad1.b) {
            delay = 2.5;
        }
        else if (gamepad1.x) {
            delay = 5;
        }
        else if (gamepad1.y) {
            delay = 7.5;
        }

        if (gamepad1.left_bumper) {
            blue = true;
        }
        else if (gamepad1.right_bumper) {
            blue = false;
        }
        telemetry.addData("Delay", delay);
        telemetry.addData("Side", blue?"Blue":"Red");
        color = blue?robot.colorRight:robot.colorLeft;
        if (gamepad1.dpad_up) {
            color.enableLed(true);
        }
        else if(gamepad1.dpad_down) {
            color.enableLed(false);
        }
        updateSensors();
    }

    @Override
    public void start() {
        main.sendData("startTime0", time);
        //main.changeState("driveToWall");
        searchForBeacon.start();
        searchForBeacon.sendData("startTime", time);
        searchForBeacon.changeState("start");
    }

    @Override
    public void loop() {
        telemetry.addData("State", main.getActiveState());
        updateSensors();
        robot.generateTelemetry(telemetry, true);
    }

    @Override
    public void stop() {
        main.stop();
    }

    public void move(double power) {
        power *= -1.0;
        robot.frontLeft.setPower(power);
        robot.frontRight.setPower(power);
        robot.backLeft.setPower(power);
        robot.backRight.setPower(power);
    }

    public void turn(double power) {
        power *= -1.0;
        robot.frontLeft.setPower(power);
        robot.frontRight.setPower(-power);
        robot.backLeft.setPower(power);
        robot.backRight.setPower(-power);
    }

    public void resetEncoders(boolean partOne) {
        if (partOne) {
            robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        else {
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public boolean reachedDestination(int target, int timeout) {
        return (robot.frontLeft.getCurrentPosition() >= target && robot.frontRight.getCurrentPosition() >= target && robot.backLeft.getCurrentPosition() >= target && robot.backRight.getCurrentPosition() >= target) || elapsedTime > timeout;
    }

    public boolean turnedDegrees(double degrees, int timeout){
        return true;
    }

    public void updateSensors() {
        telemetry.addData("State", searchForBeacon.getActiveState());
        telemetry.addData("Color", "R: %d G: %d B: %d A: %d", color.red(), color.green(), color.blue(), color.alpha());
        telemetry.addData("Light", robot.light.getLightDetected());
        Orientation angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        heading = angles.firstAngle;
        telemetry.addData("IMU", "heading: %s", String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, heading))));
    }


}
