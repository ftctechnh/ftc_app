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

    @Override
    public void init() {
        robot.init(hardwareMap);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new AccelerationIntegrator();

        robot.imu = hardwareMap.get(BNO055IMU.class, "imu");
        robot.imu.initialize(parameters);

        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        searchForBeacon = new StateMachine(
                new State("stop") {
                    @Override
                    public void run() {
                        color.enableLed(false);
                    }
                },
                new State("start") {
                    @Override
                    public void run() {
                        color.enableLed(true);
                        robot.light.enableLed(false);
                        move(0.25);
                        changeState("detectWhiteLine");
                    }
                },
                new State("detectWhiteLine") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime");

                        if (robot.light.getLightDetected() > 0.05 || elapsedTime > 5.0) {
                            move(0);
                            resetEncoders(true);
                            sendData("resetEncoders0", time);
                            changeState("resetEncoders0");
                        }
                    }
                },
                new State("resetEncoders0") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("resetEncoders0");
                        if (elapsedTime > 0.75) {
                            resetEncoders(false);
                            move(-0.25);
                            sendData("startTime1", time);
                            changeState("driveToButton0");
                        }
                    }
                },
                new State("driveToButton0") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime1");
                        if (reachedDestination(500, 2.0, elapsedTime)) {
                            move(0);
                        }
                    }
                },
                new State("finished") {
                    @Override
                    public void run() {
                    }
                }
        );

        main = new StateMachine(
                new State("stop") {
                    @Override
                    public void run() {
                        robot.leftLift.setPosition(HardwareVortex.LEFT_LIFT_INIT);
                        robot.rightLift.setPosition(HardwareVortex.RIGHT_LIFT_INIT);
                    }
                },
                new State("driveToWall") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime0");
                        if (elapsedTime > delay) {
                            move(0.35);
                            sendData("startTime1", time);
                            changeState("reachedWall");
                        }
                    }
                },
                new State("reachedWall") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime1");
                        if (reachedDestination(2500, 4.0, elapsedTime)) {
                            searchForBeacon.sendData("startTime", time);
                            searchForBeacon.changeState("start");
                            changeState("beacon0Scored");
                        }
                    }
                },
                new State("beacon0Scored") {
                    @Override
                    public void run() {
                        if (searchForBeacon.getActiveState().equals("scored")) {
                            searchForBeacon.sendData("startTime", time);
                            searchForBeacon.changeState("start");
                            changeState("beacon1Scored");
                        }
                    }
                },
                new State("beacon1Scored") {
                    @Override
                    public void run() {
                        if (searchForBeacon.getActiveState().equals("scored")) {
                            changeState("stop");
                        }
                    }
                }
        ).start();
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

    public boolean reachedDestination(int target, double timeout, double elapsedTime) {
        return (robot.frontLeft.getCurrentPosition() >= target && robot.frontRight.getCurrentPosition() >= target && robot.backLeft.getCurrentPosition() >= target && robot.backRight.getCurrentPosition() >= target) || elapsedTime > timeout;
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
