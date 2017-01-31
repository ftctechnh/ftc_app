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
public class AutoVortex extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private DcMotor[] leftDrive, rightDrive, driveMotors;

    private ColorSensor color;

    private StateMachine main;

    //Parameters
    private double delay = 0;
    private boolean blue = false;
    private boolean cornerStart = false;
    private boolean beacon = true;
    private boolean park = false;


    private double heading = 0;

    private StateMachine shooter;

    private StateMachine button;

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

        leftDrive = new DcMotor[] {
                robot.frontLeft,
                robot.backLeft
        };
        rightDrive = new DcMotor[] {
                robot.frontRight,
                robot.backRight
        };
        driveMotors = new DcMotor[] {
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

        button = new StateMachine(
                new State("push") {
                    @Override
                    public void run() {

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
                        if (elapsedTime(getDouble("start")) > delay) {
                            shooter.changeState("on");
                            sendData("shooter start", time);
                            if (cornerStart) {
                                changeState("drive to center line");
                            } else {
                                changeState("middle - reach vortex");
                            }
                        }
                    }
                },

                // MIDDLE SPECIFIC START
                new State("middle - reach vortex") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 4000, 0.4)) {
                            changeState("turn a little left");
                        }
                    }
                },
                // MIDDLE SPECIFIC END

                // CORNER SPECIFIC START
                new State("drive to center line") {
                    @Override
                    public void run() {
                        if (reachedDestination(300, 2000, 0.4)) {
                            changeState("turn to vortex");
                        }
                    }
                },

                new State("turn to vortex") {
                    @Override
                    public void run() {
                        if ((blue && turnedDegrees(-45, 3000, 0.8)) || (!blue && turnedDegrees(45, 3000, 0.8))) {
                            robot.resetEncoders();
                            changeState("corner - reach vortex");
                        }
                    }
                },

                new State("corner - reach vortex") {
                    @Override
                    public void run() {
                        if (reachedDestination(650, 4000, 0.5)) {
                            changeState("turn a little left");
                        }
                    }
                },
                // CORNER SPECIFIC START END

                new State("turn a little left") {
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

                new State("shoot the ball") {
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

                new State("turn a little right") {
                    @Override
                    public void run() {
                        if (turnedDegrees(-6, 1500, 0.5)) {
                            changeState("stop");
                            //changeState("turn to line");
                        }
                    }
                },

                // STUFF UP TO THIS WORKS

                new State("turn to line") {
                    @Override
                    public void run() {
                        if (elapsedTime(getDouble("shooting time")) > 5.0) {
                            robot.intake.setPower(0);
                            shooter.changeState("off");
                            changeState("drive tp line");
                        }
                    }
                },

                new State("drive to line") {
                    @Override
                    public void run() {
                        if (turnedDegrees(70, 3000, -0.5)) {
                            changeState("turn");
                        }
                    }
                },

                new State("turn") {
                    @Override
                    public void run() {
                        if (reachedDestination(1750, 3000, 0.5)) {
                            changeState("drive to wall");
                        }
                    }
                },

                new State("drive to wall") {
                    @Override
                    public void run() {
                        if (turnedDegrees(1000000000, 10000, 1)) {
                            changeState("sense color");
                        }
                    }
                },

                new State("sense color") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 3000, 0.5)) {
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
                        changeState("turn to other line");
                    }
                },

                new State("turn to other line") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 3000, -0.5)) {
                            changeState("drive to other line");
                        }
                    }
                },

                new State("drive to other line") {
                    @Override
                    public void run() {
                        if (turnedDegrees(90, 2000, -1)) {
                            changeState("turn on other line");
                        }
                    }
                },

                new State("turn to other line") {
                    @Override
                    public void run() {
                        if (reachedDestination(2000, 5000, 0.5)) {
                            changeState("drive down other line");
                        }
                    }
                },

                new State("drive down other line") {
                    @Override
                    public void run() {
                        if (turnedDegrees(90, 2000, -0.5)) {
                            changeState("other button");
                        }
                    }
                },

                new State("other button") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 3000, 0.5)) {
                            changeState("back up again");
                        }
                    }
                },

                new State("back up again") {
                    @Override
                    public void run() {
                        changeState("turn to cap ball");
                    }
                },

                new State("turn to cap ball") {
                    @Override
                    public void run() {
                        if (reachedDestination(1000, 3000, -0.5)) {
                            changeState("drive to ball");
                        }
                    }
                },

                new State("drive to ball") {
                    @Override
                    public void run() {
                        if (turnedDegrees(120, 4000, -1)) {
                            changeState("hit ball");
                        }
                    }
                },

                new State("hit ball") {
                    @Override
                    public void run() {
                        if (reachedDestination(2500, 4000, 1)){
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
        main.changeState("drive to vortex");
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
        shooter.changeState("off");
        shooter.stop();
    }

    ///////////////
    // FUNCTIONS //
    ///////////////

    private void move(double power) {
        robot.frontLeft.setPower(power);
        robot.frontRight.setPower(power);
        robot.backLeft.setPower(power);
        robot.backRight.setPower(power);
    }

    private double elapsedTime(double startTime) {
        return time - startTime;
    }

    private boolean reachedDestination(int target, int timeout, double power) {
        return Precision.destinationReached(driveMotors, power, Math.signum(power)*0.125, target, 2.0, 10, timeout);
    }

    private boolean turnedDegrees(double degrees, int timeout, double power) {
        return Precision.angleTurned(leftDrive, rightDrive, heading, power, 0.45, degrees, 2.0, 0.75, timeout);
    }

    private void updateSensors() {
        telemetry.addData("State", main.getActiveState());
        telemetry.addData("Color", "R: %d G: %d B: %d A: %d", color.red(), color.green(), color.blue(), color.alpha());
        telemetry.addData("Light", robot.light.getLightDetected());
        Orientation angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        heading = angles.firstAngle;
        telemetry.addData("IMU", "heading: %s", String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, heading))));
    }


}
