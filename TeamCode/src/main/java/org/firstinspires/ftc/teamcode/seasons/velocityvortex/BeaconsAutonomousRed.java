package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by ftc6347 on 11/30/16.
 */
@Autonomous(name = "Red beacons", group = "autonomous")
public class BeaconsAutonomousRed extends LinearOpMode {

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

                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 2500) {
                        robot.getFrontLeftDrive().setPower(0.5);
                        robot.getBackRightDrive().setPower(-0.5);
                    }

                    robot.stopRobot();

                    state = 2;

                case 2:
                    while(opModeIsActive() && robot.getOds3().getLightDetected() < 0.15) {
                        robot.getFrontLeftDrive().setPower(0.2);
                        robot.getBackRightDrive().setPower(-0.2);
                    }

                    state = 3;
                    break;
                case 3:
                    robot.stopRobot();
                    robot.getRuntime().reset();

                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 750) {
                        robot.driveLeft(0.2);
                    }

                    robot.stopRobot();

                    state = 4;
                    break;
                case 4:
                    while(opModeIsActive() && robot.getOds3().getLightDetected() < 0.15) {
                        robot.driveRight(0.15);
                    }
                    state = 5;
                    break;
                case 5:
                    if(robot.getFrontRange().cmUltrasonic() >= 5) {
                        robot.driveForward(0.2);
                    } else {
                        robot.getRuntime().reset();
                        while(opModeIsActive() && robot.getRuntime().milliseconds() < 250) {
                            robot.driveBackward(0.2);
                        }

                        // pause for the beacon to change color
                        robot.getRuntime().reset();
                        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
                            idle();
                        }

                        if(robot.getColorSensor().blue() > 0) {

                            robot.stopRobot();

                            robot.getRuntime().reset();
                            while(opModeIsActive() && robot.getRuntime().milliseconds() < 5000) {
                                idle();
                            }

                            while(opModeIsActive() && robot.getFrontRange().cmUltrasonic() >= 5) {
                                robot.driveForward(0.2);
                            }

                            while(opModeIsActive() && robot.getFrontRange().cmUltrasonic() < 6) {
                                robot.driveBackward(0.2);
                            }
                        }

                        robot.getRuntime().reset();
                        while(opModeIsActive() && robot.getRuntime().milliseconds() < 3000) {
                            robot.driveRight(0.2);
                        }

                        robot.getRuntime().reset();
                        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
                            robot.pivotRight(0.2);
                        }

                        robot.getRuntime().reset();
                        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
                            robot.driveBackward(0.5);
                        }

                        robot.stopRobot();

                        state = 6;
                    }
                    break;
                case 6:
                    // launch particle
                    robot.getRuntime().reset();
                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 900) {
                        robot.getLauncherMotor().setPower(1);
                    }

                    robot.getLauncherMotor().setPower(0);

                    while(opModeIsActive() && robot.getDiskOds().getRawLightDetected() > 2) {
                        robot.getLauncherMotor().setPower(0.3);
                    }

                    robot.getLauncherMotor().setPower(0);

                    state = 7;
                    break;
                case 7:
                    // raise the intake door
                    robot.getDoor3().setPosition(0.25);

                    // run intake
                    robot.getRuntime().reset();
                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 2000) {
                        robot.getIntakeMotor().setPower(-1);
                    }
                    robot.getIntakeMotor().setPower(0);

                    state = 8;
                    break;
                case 8:
                    // launch particle
                    robot.getRuntime().reset();
                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 900) {
                        robot.getLauncherMotor().setPower(1);
                    }

                    while(opModeIsActive() && robot.getDiskOds().getLightDetected() > 2) {
                        robot.getLauncherMotor().setPower(0.3);
                    }

                    robot.getLauncherMotor().setPower(0);

                    state = 999;
                    break;
            }
        }
    }

    private void readRangeSensors() {
        telemetry.addData("state", this.state);
        telemetry.addData("front range", robot.getFrontRange().cmUltrasonic());
        telemetry.addData("left range", robot.getLeftRange().cmUltrasonic());

        telemetry.addData("red color", robot.getColorSensor().red());
        telemetry.addData("blue color", robot.getColorSensor().blue());

        telemetry.addData("ods3", robot.getOds3().getLightDetected());
        telemetry.addData("launcher ods", robot.getLauncherOds().getLightDetected());

//        telemetry.addData("gyro", robot.getGyroSensor().getIntegratedZValue());
        telemetry.addData("timer",this.time);
        telemetry.update();
    }
}
