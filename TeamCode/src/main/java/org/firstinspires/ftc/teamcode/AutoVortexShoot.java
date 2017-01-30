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
import org.firstinspires.ftc.teamcode.modules.Precision;
import org.firstinspires.ftc.teamcode.modules.State;
import org.firstinspires.ftc.teamcode.modules.StateMachine;

import java.util.Locale;

@Autonomous
public class AutoVortexShoot extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private DcMotor[] leftDrive, rightDrive, driveMotors;

    private StateMachine main;

    private double delay = 0;

    private int path = 1;

    private boolean blue = false, push = true;

    private double heading = 0;

    private StateMachine shooter;

    @Override
    public void init() {
        synchronized (this) {
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

            leftDrive = new DcMotor[]{
                    robot.frontLeft,
                    robot.backLeft
            };
            rightDrive = new DcMotor[]{
                    robot.frontRight,
                    robot.backRight
            };
            driveMotors = new DcMotor[]{
                    robot.frontLeft,
                    robot.frontRight,
                    robot.backLeft,
                    robot.backRight
            };

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
            ).start();

            main = new StateMachine(
                    new State("stop") {
                        @Override
                        public void run() {
                            move(0);
                        }
                    },

                    new State("drive one square") {
                        @Override
                        public void run() {
                            double elapsedTime = time - getDouble("start");
                            if (elapsedTime > delay) {
                                shooter.changeState("on");
                                if (path == 1) {
                                    changeState("path1");
                                }
                                else if (path == 2) {
                                    changeState("path2");
                                }
                            }
                        }
                    },

                    new State("path2") {
                        @Override
                        public void run() {
                            if (reachedDestination(1000, 4000, 0.4)) {
                                move(0);
                                Precision.reset();
                                changeState("shoot the ball");
                            }
                        }
                    },

                    new State("path1") {
                        @Override
                        public void run() {
                            if (reachedDestination(300, 2000, 0.4)) {
                                move(0);
                                Precision.reset();
                                changeState("turn to vortex");
                            }
                        }
                    },

                    new State("turn to vortex") {
                        @Override
                        public void run() {
                            if ((blue && turnedDegrees(-45, 3000, 0.8)) || (!blue && turnedDegrees(45, 3000, 0.8))) {
                                move(0);
                                robot.resetEncoders();
                                Precision.reset();
                                changeState("turn a little left");
                            }
                        }
                    },

                    new State("turn a little left") {
                        @Override
                        public void run() {
                            if (reachedDestination(650, 4000, 0.5)) {
                                Precision.reset();
                                changeState("shoot the ball");
                            }
                        }
                    },

                    new State("shoot the ball") {
                        @Override
                        public void run() {
                            if (turnedDegrees(6, 1500, 0.5)) {
                                robot.intake.setPower(1);
                                sendData("shooting time", time);
                                Precision.reset();
                                changeState("turn a little right");
                            }
                        }
                    },

                    new State("turn a little right") {
                        @Override
                        public void run() {
                            double elapsedTime = time - getDouble("shooting time");
                            if (elapsedTime > 5.0) {
                                move(0);
                                shooter.changeState("off");
                                robot.intake.setPower(0);
                                changeState("push ball");
                            }
                        }
                    },

                    new State("push ball") {
                        @Override
                        public void run() {
                            if (turnedDegrees(-6, 1500, 0.5)) {
                                Precision.reset();
                                if (push) {
                                    changeState("finish");
                                }
                                else {
                                    changeState("stop");
                                }
                            }
                        }
                    },

                    new State("finish") {
                        @Override
                        public void run() {
                            if (reachedDestination(1000, 3000, 1.0)) {
                                move(0);
                            }
                        }
                    }
            ).start();
        }
    }

    @Override
    public void init_loop() {
        if (gamepad1.a) {
            delay = 0;
        }
        else if (gamepad1.b) {
            delay = 5;
        }
        else if (gamepad1.x) {
            delay = 7.5;
        }
        else if (gamepad1.y) {
            delay = 10;
        }

        if (gamepad1.dpad_up) {
            path = 1;
        }
        else if (gamepad1.dpad_down) {
            path = 2;
        }

        if (gamepad1.dpad_left) {
            push = true;
        }
        else if (gamepad1.dpad_right) {
            push = false;
        }

        if (gamepad1.left_bumper) {
            blue = true;
        }
        else if (gamepad1.right_bumper) {
            blue = false;
        }
        telemetry.addData("Delay", delay);
        telemetry.addData("Side", blue?"Blue":"Red");
        telemetry.addData("Path", path);
        telemetry.addData("Push", push);
        updateSensors();
    }

    @Override
    public void start() {
        main.sendData("start", time);
        main.changeState("drive one square");
    }

    @Override
    public void loop() {
        telemetry.addData("State", main.getActiveState());
        updateSensors();
        robot.generateTelemetry(telemetry, true);
    }

    @Override
    public void stop() {
        shooter.changeState("off");
        main.stop();
    }

    public void move(double power) {
        robot.frontLeft.setPower(power);
        robot.frontRight.setPower(power);
        robot.backLeft.setPower(power);
        robot.backRight.setPower(power);
    }

    private boolean reachedDestination(int target, int timeout, double power) {
        return Precision.destinationReached(driveMotors, power, Math.signum(power)*0.16, target, 2.0, 10, timeout);
    }

    private boolean turnedDegrees(double degrees, int timeout, double power) {
        return Precision.angleTurned(leftDrive, rightDrive, heading, power, 0.44, degrees, 2.0, 0.5, timeout);
    }

    private void updateSensors() {
        telemetry.addData("State", main.getActiveState());
        Orientation angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        heading = angles.firstAngle;
        telemetry.addData("IMU", "heading: %s", String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, heading))));
    }


}
