package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by ftc6347 on 11/30/16.
 */
@Autonomous(name = "Blue beacons", group = "autonomous")
public class BeaconsAutonomousBlue extends LinearOpMode {

    private ZoidbergHardware robot;

    private int state = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ZoidbergHardware(hardwareMap);

        while (!isStarted() || isStopRequested()) {
            readRangeSensors();
            idle();
        }

        while(opModeIsActive()) {
            readRangeSensors();

            switch(state) {
                case 999:
                    robot.stopRobot();
                    break;
                case 0:
                    robot.getRuntime().reset();

                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
                        idle();
                    }

                    state = 1;
                    break;
                case 1:

                    robot.getRuntime().reset();

                    while(robot.getRuntime().milliseconds() < 2500) {
                        robot.getBackLeftDrive().setPower(0.5);
                        robot.getFrontRightDrive().setPower(-0.5);
                    }

                    robot.stopRobot();

                    state = 2;

                case 2:
                    while(opModeIsActive() && robot.getOds3().getLightDetected() < 0.15) {
                        robot.getBackLeftDrive().setPower(0.2);
                        robot.getFrontRightDrive().setPower(-0.2);
                    }

                    state = 3;
                    break;
                case 3:
                    robot.stopRobot();
                    robot.getRuntime().reset();

                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 750) {
                        robot.driveRight(0.2);
                    }

                    robot.stopRobot();

                    state = 4;
                    break;
                case 4:
                    while(opModeIsActive() && robot.getOds3().getLightDetected() < 0.15) {
                        robot.driveLeft(0.15);
                    }
                    state = 5;
                    break;
                case 5:
                    if(robot.getFrontRange().readUltrasonic() >= 5) {
                        robot.driveForward(0.2);
                    } else {
                        robot.getRuntime().reset();
                        while(robot.getRuntime().milliseconds() < 250) {
                            robot.driveBackward(0.2);
                        }

                        // pause for the beacon to change color
                        robot.getRuntime().reset();
                        while(robot.getRuntime().milliseconds() < 500) {
                            idle();
                        }

                        if(robot.getColorSensor().red() > 0) {

                            robot.stopRobot();

                            robot.getRuntime().reset();
                            while(robot.getRuntime().milliseconds() < 5000) {
                                idle();
                            }

                            while(robot.getFrontRange().readUltrasonic() >= 5) {
                                robot.driveForward(0.2);
                            }

                            while(robot.getFrontRange().readUltrasonic() < 6) {
                                robot.driveBackward(0.2);
                            }
                        }

                        robot.getRuntime().reset();
                        while(robot.getRuntime().milliseconds() < 2700) {
                            robot.driveLeft(0.2);
                        }

                        robot.getRuntime().reset();
                        while(robot.getRuntime().milliseconds() < 500) {
                            robot.pivotLeft(0.2);
                        }

                        robot.getRuntime().reset();
                        while(robot.getRuntime().milliseconds() < 500) {
                            robot.driveBackward(0.5);
                        }

                        robot.stopRobot();

                        state = 6;
                    }
                    break;
                case 6:
                    // launch particle
                    robot.launchParticle();

                    state = 7;
                    break;
                case 7:
                    // raise the intake door
                    robot.getDoor3().setPosition(0.25);

                    // run intake
                    robot.getRuntime().reset();
                    while(robot.getRuntime().milliseconds() < 2000) {
                        robot.getIntakeMotor().setPower(-1);
                    }
                    robot.getIntakeMotor().setPower(0);

                    state = 8;
                    break;
                case 8:
                    robot.launchParticle();

                    state = 999;
                    break;
            }
        }
    }

    private void readRangeSensors() {
        telemetry.addData("state", this.state);
        telemetry.addData("front range", robot.getFrontRange().readUltrasonic());
        telemetry.addData("left range", robot.getLeftRange().readUltrasonic());

        telemetry.addData("red color", robot.getColorSensor().red());
        telemetry.addData("blue color", robot.getColorSensor().blue());

        telemetry.addData("ods3", robot.getOds3().getLightDetected());
        telemetry.addData("launcher ods", robot.getLauncherOds().getLightDetected());

//        telemetry.addData("gyro", robot.getGyroSensor().getIntegratedZValue());
        telemetry.addData("timer",this.time);
        telemetry.update();
    }
}
