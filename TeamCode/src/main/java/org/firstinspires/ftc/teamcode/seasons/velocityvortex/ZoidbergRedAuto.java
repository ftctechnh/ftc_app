package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

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

        // loop similar to waitForStart()
        while (!isStarted()){
            readRangeSensors();

            if(robot.getGyroSensor().isCalibrating()) {
                telemetry.addData("gyro sensor calibrating", "yes");
            } else {
                telemetry.addData("gyro sensor calibrating", "no");
            }
        }

        while(opModeIsActive()) {
            readRangeSensors();

            switch (state) {
            case 0:

                // if the robot is rotated right
                if(robot.getGyroSensor().getIntegratedZValue() > 5) {
                    robot.getBackRightDrive().setPower(-0.3);
                // if the robot is rotated left
                } else if(robot.getGyroSensor().getIntegratedZValue() > 354) {
                    robot.getFrontLeftDrive().setPower(0.3);
                } else {
                    robot.getFrontLeftDrive().setPower(0.2);
                    robot.getBackRightDrive().setPower(-0.2);
                }

                break;
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

        telemetry.update();
    }

}
