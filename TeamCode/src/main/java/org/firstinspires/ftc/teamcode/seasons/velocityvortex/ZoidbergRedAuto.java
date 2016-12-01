package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ftc6347 on 10/30/16.
 */
@Autonomous(name = "Zoidberg Red", group = "competition autonomous")
public class ZoidbergRedAuto extends LinearOpMode {

    private ZoidbergHardware robot;

    private int state = 0;

    private int frontRange;
    private int leftRange;

    private I2cDeviceSynch frontRangeReader;
    private I2cDeviceSynch leftRangeReader;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ZoidbergHardware(hardwareMap);

        frontRangeReader = new I2cDeviceSynchImpl(robot.getFrontRangeSensor(),
                ZoidbergHardware.FRONT_RANGE_SENSOR_I2C_ADDR, false);

        leftRangeReader = new I2cDeviceSynchImpl(robot.getLeftRangeSensor(),
                ZoidbergHardware.LEFT_RANGE_SENSOR_I2C_ADDR, false);

        frontRangeReader.engage();
        leftRangeReader.engage();

        robot.getDoor3().setPosition(0.25); // open the intake door

//        waitForStart();
        while (!isStarted() || isStopRequested()) {
            readRangeSensors();

            if(robot.getGyroSensor().isCalibrating()) {
                telemetry.addData("gyro sensor calibrating", "yes");
            } else {
                telemetry.addData("gyro sensor calibrating", "no");
            }
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

                        // stop everything
                        robot.stopRobot();
                        robot.getLauncherMotor().setPower(0);

                        idle();
                    }

                    state = 1;

                    break;

                case 1:
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

                    while(robot.getRuntime().milliseconds() < 2000) {
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
        byte[] frontCache = frontRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
        byte[] leftCache = leftRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);

        this.frontRange = frontCache[0] & 0xFF;
        this.leftRange = leftCache[0] & 0xFF;

        telemetry.addData("state", this.state);
        telemetry.addData("front range", frontRange);
        telemetry.addData("left range", leftRange);

        telemetry.addData("gyro",robot.getGyroSensor().getIntegratedZValue());
        telemetry.addData("timer",this.time);
        telemetry.update();
    }

}
