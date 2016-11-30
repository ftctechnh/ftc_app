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

    private int state = 800;

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
            if ( this.time < 2 ){
                state = 800;
            }
            else if (state == 800)
            {
                state = 0;
            }


            switch (state) {
                case 999:
                    readRangeSensors();
                    break;
                case 800:
                    robot.driveBackward(0.5);
                    break;
                case 0:

                    // drive forward for 0.25 seconds

                    robot.stopRobot();

                    state = 1;

                    break;

                case 1:

                    //if viewing black
//                    if(robot.getFrontLightSensor().getLightDetected() < 0.25){
//
//                        robot.getFrontLeftDrive().setPower(0.25);
//                        robot.getBackRightDrive().setPower(-0.25); //drive diagonally forward and to the right
//
//                    }
//                    else {
//                        robot.stopRobot();
//                        state = 1;
//                    }

                    if (robot.getDiskOds().getRawLightDetected() > 2) {
                        // run the launcher motor at a slower speed to find the black stripe
                        robot.getLauncherMotor().setPower(0.3);
                    } else {
                        // stop the motor once the black stripe is detected
                        robot.getLauncherMotor().setPower(0);

                        // wait 100 milliseconds for the ball to be launched
                        sleep(100);

                        // drive the motor past the black stripe for 0.25 seconds
                        robot.getLauncherMotor().setPower(1);
                        sleep(250);
                        robot.getLauncherMotor().setPower(0);

                        state = 4;
                    }

                break;

                case 4:
                    // open intake door
                    robot.getDoor3().setPosition(0.25);

                    robot.getIntakeMotor().setPower(-1);
                    sleep(2000);
                    robot.stopRobot();

                    state = 1;
                    break;

                // align with gyro
                case 5:

                    // stop the robot if it is perfectly centered
                    if(robot.getGyroSensor().getIntegratedZValue() == 0) {
                        robot.stopRobot();

                        // we're done, so switch to next case
                        state = 2;
                    // pivot right if robot is rotated left
                    } else if(robot.getGyroSensor().getIntegratedZValue() < 359) {
                        robot.pivotRight(0.2);
                    // pivot left if robot is roatated right
                    } else if(robot.getGyroSensor().getIntegratedZValue() > 1) {
                        robot.pivotLeft(0.2);
                    }

                    break;
                // drive left and print sensor values
                case 6:
                    telemetry.addData("gyro Z value", robot.getGyroSensor().getIntegratedZValue());
                    telemetry.update();

                    robot.driveLeft(0.2);
                    sleep(250);
                    robot.stopRobot();

                    state = 3;

                    break;

                // drive right to beacon lines
                case 7:
                    //if viewing black, drive right
                    if(robot.getFrontLightSensor().getLightDetected() < 0.25) {
                        robot.driveRight(0.15);
                    // stop when the white line is detected
                    } else {
                        robot.stopRobot();
                    }

                    break;

//                // if the robot is rotated right
//                if(robot.getGyroSensor().getIntegratedZValue() > 5) {
//                    robot.getBackRightDrive().setPower(-0.3);
//                // if the robot is rotated left
//                } else if(robot.getGyroSensor().getIntegratedZValue() > 354) {
//                    robot.getFrontLeftDrive().setPower(0.3);
//                } else {
//                    robot.getFrontLeftDrive().setPower(0.2);
//                    robot.getBackRightDrive().setPower(-0.2);
//                }
//                break;
            }
        }

//        while(opModeIsActive() && leftRange < 85) {
//
//            // read sensor values in byte buffer
//            leftRangeCache = leftRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
//            leftRange = leftRangeCache[0] & 0xFF;
//
//            print(frontRangeReader, leftRangeReader);
//
//            robot.driveRight(0.4);
//        }
//
//        robot.stopRobot();
//        sleep(100);
//
//
//        while(opModeIsActive() && frontRange > 20) {
//
//            // read sensor values in byte buffer
//            frontRangeCache = frontRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
//            frontRange = frontRangeCache[0] & 0xFF;
//
//            robot.driveForward(0.2);
//
//            print(frontRangeReader, leftRangeReader);
//
//            telemetry.addData("front light", robot.getFrontLightSensor());
//            telemetry.addData("back light", robot.getBackLightSensor());
//
//            telemetry.update();
//        }
//
//        robot.stopRobot();
//
//        // both sensor detecting the mat
//        while(robot.getFrontLightSensor().getLightDetected() < 0.28 &&
//                robot.getBackLightSensor().getLightDetected() < 0.28) {
//            robot.driveRight(0.2);
//        }
//
//        // if both detect white
//        if(robot.getFrontLightSensor().getLightDetected() > 0.28 &&
//                robot.getFrontLightSensor().getLightDetected() > 0.28) {
//            robot.stopRobot();
//        } else if(robot.getFrontLightSensor().getLightDetected() > 0.28) {
//            robot.pivotLeft(0.1);
//        } else if(robot.getBackLightSensor().getLightDetected() > 0.28) {
//            robot.pivotRight(0.1);
//        }
//            robot.pivotLeft(0.1);
//        } else if(robot.getFrontLightSensor().getLightDetected() > 0.28)  {
//            robot.pivotRight(0.1);
//        }
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
