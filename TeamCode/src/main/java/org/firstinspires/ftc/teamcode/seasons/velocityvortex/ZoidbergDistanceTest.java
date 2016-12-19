package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

/*
Modern Robotics Range Sensors Example
Created 10/31/2016 by Colton Mehlhoff of Modern Robotics using FTC SDK 2.35
Reuse permitted with credit where credit is due

Configuration:
I2CDevice "range28" (MRI Range Sensor with default I2C address 0x28
I2CDevice "range2a" (MRI Color Sensor with I2C address 0x2a

ModernRoboticsI2cGyro is not being used because it does not support .setI2CAddr().

To change range sensor I2C Addresses, go to http://modernroboticsedu.com/mod/lesson/view.php?id=96
Support is available by emailing support@modernroboticsinc.com.
*/


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

@Disabled
@TeleOp(name = "Range test", group = "tests")
//@Disabled
public class ZoidbergDistanceTest extends LinearOpMode {

    private ZoidbergHardware robot;
    private I2cDeviceSynch frontRangeReader;
    private I2cDeviceSynch leftRangeReader;

    @Override
    public void runOpMode() throws InterruptedException {

        robot = new ZoidbergHardware(hardwareMap);

        //I2cDeviceSynchImpl frontRangeReader = new I2cDeviceSynchImpl(robot.getFrontRangeSensor(),
         //       ZoidbergHardware.FRONT_RANGE_SENSOR_I2C_ADDR, false);

       // I2cDeviceSynchImpl leftRangeReader = new I2cDeviceSynchImpl(robot.getLeftRangeSensor(),
//                ZoidbergHardware.LEFT_RANGE_SENSOR_I2C_ADDR, false);



        frontRangeReader.engage();
        leftRangeReader.engage();

        byte[] frontCache;
        byte[] leftCache;

        waitForStart();

        while(opModeIsActive()) {

            frontCache = frontRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);
            leftCache = leftRangeReader.read(ZoidbergHardware.RANGE_SENSOR_REG_START, 2);

            telemetry.addLine("front ")
                    .addData("ultrasonic: ", frontCache[0] & 0xFF)
                    .addData("ODS: ", frontCache[1] & 0xFF);

            telemetry.addLine("left ")
                    .addData("ultrasonic: ", leftCache[0] & 0xFF)
                    .addData("ODS: ", leftCache[1] & 0xFF);

            telemetry.addData("color: ", robot.getColorSensor().argb());
            telemetry.addData("back light: ", robot.getBackLightSensor().getLightDetected());
            telemetry.addData("front light: ", robot.getFrontLightSensor().getLightDetected());

            telemetry.addData("ods1 : ", robot.getLauncherOds().getLightDetected());
            telemetry.addData("ods2 : ", robot.getDiskOds().getRawLightDetected());

            telemetry.update();

            idle();
        }
    }
}