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

    private byte[] frontRangeCache;
    private byte[] leftRangeCache;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ZoidbergHardware(hardwareMap);

        I2cDeviceSynchImpl frontRangeReader = new I2cDeviceSynchImpl(robot.getFrontRangeSensor(),
                ZoidbergHardware.FRONT_RANGE_SENSOR_I2C_ADDR, false);

        I2cDeviceSynchImpl leftRangeReader = new I2cDeviceSynchImpl(robot.getLeftRangeSensor(),
                ZoidbergHardware.LEFT_RANGE_SENSOR_I2C_ADDR, false);

        frontRangeReader.engage();
        leftRangeReader.engage();

        int leftRange = 0;
        int frontRange = 0;



        waitForStart();

        while(leftRange < 85) {

            // read sensor values in byte buffer
            leftRangeCache = leftRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
            leftRange = leftRangeCache[0] & 0xFF;

            print(frontRangeReader,leftRangeReader);

            robot.driveRight(0.4);
        }

        //robot.stopRobot();
        //sleep(100);

        while(true) {

            frontRangeCache = frontRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
            frontRange = frontRangeCache[0] & 0xFF;

            if  (frontRange > 10){// read sensor values in byte buffer
                frontRangeCache = frontRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
                frontRange = frontRangeCache[0] & 0xFF;

                robot.driveForward(0.25);
            }
            else {
                robot.stopRobot();
            }
            print(frontRangeReader,leftRangeReader);
        }

    }

    void print(I2cDeviceSynchImpl frontRangeReader, I2cDeviceSynchImpl leftRangeReader){
        frontRangeCache = frontRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
        leftRangeCache = leftRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);

        telemetry.addLine("front ")
                .addData("ultrasonic: ", frontRangeCache[0] & 0xFF)
                .addData("ODS: ", frontRangeCache[1] & 0xFF);

        telemetry.addLine("left ")
                .addData("ultrasonic: ", leftRangeCache[0] & 0xFF)
                .addData("ODS: ", leftRangeCache[1] & 0xFF);

        telemetry.update();

    }
}
