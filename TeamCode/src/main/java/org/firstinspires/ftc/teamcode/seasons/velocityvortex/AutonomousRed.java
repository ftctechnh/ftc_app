package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by ftc6347 on 10/30/16.
 */
@Autonomous(name = "Red 2 ball, park", group = "autonomous")
public class AutonomousRed extends LinearOpMode {

    private ZoidbergHardware robot;

    private enum RobotState {
        INITIAL, INITIAL_BACKWARD, FIRST_LAUNCH_PARTICLE, RUN_INTAKE,
    }

    private int state = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ZoidbergHardware(hardwareMap);

        robot.getDoor3().setPosition(0.25); // open the intake door

//        waitForStart();
        while (!isStarted() || isStopRequested()) {
            readRangeSensors();

//            if(robot.getGyroSensor().isCalibrating()) {
//                telemetry.addData("gyro sensor calibrating", "yes");
//            } else {
//                telemetry.addData("gyro sensor calibrating", "no");
//            }
            telemetry.update();

            idle();
        }

        while(opModeIsActive()) {
            readRangeSensors();

            switch (state) {
                case 999:
                    robot.stopRobot();

                    break;
                case 800:
                    robot.driveBackward(0.5);

                    break;
                case 0:
                    robot.getRuntime().reset();

                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
                        readRangeSensors();
                        idle();
                    }

                    state = 1;

                    break;

                case 1:
                    robot.getRuntime().reset();

                    while(opModeIsActive() && robot.getRuntime().milliseconds() < 15000) {
                        idle();
                    }

                    robot.getRuntime().reset();

                    while(robot.getRuntime().milliseconds() < 1000) {
                        robot.driveBackward(0.5);
                    }

                    robot.stopRobot();

                    state = 2;
                    break;

                case 2:
                    launchParticle();
                    state = 4;

                    break;
                case 4:

                    robot.getRuntime().reset();

                    // open intake door
                    robot.getDoor3().setPosition(0.25);

                    while(robot.getRuntime().milliseconds() < 3000) {
                        robot.getIntakeMotor().setPower(-1);
                    }

                    robot.stopRobot();
                    robot.getIntakeMotor().setPower(0);

                    state = 5;
                    break;

                case 5:

                    launchParticle();

                    state = 6;

                    break;
                case 6:
                    robot.getRuntime().reset();

                    while(robot.getRuntime().milliseconds() < 2000) {
                        robot.driveBackward(0.5);
                    }

                    robot.stopRobot();

                    state = 7;

                    break;

                case 7:
                    robot.getRuntime().reset();
                    while(robot.getRuntime().milliseconds() < 1000) {
                        robot.driveLeft(0.5);
                    }

                    state = 999;

                    break;

            }
        }
    }

    private void launchParticle() {
        robot.getRuntime().reset();
        while(robot.getRuntime().milliseconds() < 1000) {
            // drive the motor past the black stripe for 0.25 seconds
            robot.getLauncherMotor().setPower(1);
        }

        while (robot.getDiskOds().getRawLightDetected() > 2) {
            // run the launcher motor at a slower speed to find the black stripe
            robot.getLauncherMotor().setPower(0.3);
        }

        robot.getLauncherMotor().setPower(0);


    }

    private void readRangeSensors() {
        telemetry.addData("state", this.state);
        telemetry.addData("front range", robot.getFrontRange().cmUltrasonic());
        telemetry.addData("left range", robot.getLeftRange().cmUltrasonic());

//        telemetry.addData("gyro",robot.getGyroSensor().getIntegratedZValue());
        telemetry.addData("timer",this.time);
        telemetry.update();
    }

}
