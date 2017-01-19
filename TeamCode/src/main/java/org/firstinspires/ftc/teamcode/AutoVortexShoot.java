package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.modules.State;
import org.firstinspires.ftc.teamcode.modules.StateMachine;

@Autonomous
public class AutoVortexShoot extends OpMode {
    private HardwareVortex robot = new HardwareVortex();

    private StateMachine main;

    private StateMachine shooter;

    private double delay = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);

        robot.resetEncoders();

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
                        robot.leftLift.setPosition(HardwareVortex.LEFT_LIFT_INIT);
                        robot.rightLift.setPosition(HardwareVortex.RIGHT_LIFT_INIT);
                    }
                },
                new State("driveToVortex") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime0");
                        if (elapsedTime > delay) {
                            MecanumDrive.straight(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight, 0.3);
                            sendData("startTime1", time);
                            sendData("startTime2", time);
                            shooter.changeState("on");
                            changeState("reachedVortex");
                        }
                    }
                },
                new State("reachedVortex") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime1");
                        int target = 1700;
                        if ((robot.frontLeft.getCurrentPosition() >= target && robot.frontRight.getCurrentPosition() >= target && robot.backLeft.getCurrentPosition() >= target && robot.backRight.getCurrentPosition() >= target) || elapsedTime > 2.90) {
                            double power = 0.0;
                            robot.frontLeft.setPower(power);
                            robot.frontRight.setPower(power);
                            robot.backLeft.setPower(power);
                            robot.backRight.setPower(power);
                            changeState("shoot");
                        }
                    }
                },
                new State("shoot") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime2");
                        if (elapsedTime > 7.0) {
                            robot.intake.setPower(1.0);
                            sendData("startTime3", time);
                            changeState("stopShooter");
                        }
                    }
                },
                new State("stopShooter") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime3");
                        if (elapsedTime > 5.0) {
                            shooter.changeState("off");
                            robot.intake.setPower(0.0);
                            MecanumDrive.turn(robot.frontLeft, robot.frontRight, robot.backLeft, robot.backRight, 1.0);
                            sendData("StartTime4", time);
                            changeState("nudgeLeft");
                        }
                    }
                },
                new State("nudgeLeft") {
                    @Override
                    public void run() {
                        double elapsedTime = time - getDouble("startTime4");
                        if (elapsedTime > 2.0) {
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
            delay = 5;
        }
        else if (gamepad1.x) {
            delay = 7.5;
        }
        else if (gamepad1.y) {
            delay = 10;
        }

        telemetry.addData("Delay", delay);
    }

    @Override
    public void start() {
        shooter.start();
        main.sendData("startTime0", time);
        main.changeState("driveToVortex");
    }

    @Override
    public void loop() {
        telemetry.addData("State", main.getActiveState());
        robot.generateTelemetry(telemetry, true);
    }

    @Override
    public void stop() {
        shooter.stop();
        main.stop();
    }
}
