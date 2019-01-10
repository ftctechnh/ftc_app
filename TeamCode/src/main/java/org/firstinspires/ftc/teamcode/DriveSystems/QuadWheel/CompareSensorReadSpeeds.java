package org.firstinspires.ftc.teamcode.DriveSystems.QuadWheel;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.teamcode.Hardware.QuadWheelHardware;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Quad - Compare sensor read speeds", group="Diagnostics")
public class CompareSensorReadSpeeds extends LinearOpMode {

    public BNO055IMU imu;
    public AnalogInput encoder;

    public static final int TEST_MS = 10000;

    @Override
    public void runOpMode() {
        imu = hardwareMap.get(BNO055IMU.class, "primaryIMU");
        imu.initialize(new BNO055IMU.Parameters());
        encoder = hardwareMap.analogInput.get("armPot");

        telemetry.log().add("Read speed comparison");
        telemetry.log().add("--------------------------");
        telemetry.log().add("No movement will take place");
        telemetry.update();

        waitForStart();

        ElapsedTime timer = new ElapsedTime();
        int imuHz = 0;

        while (opModeIsActive() && timer.milliseconds() < TEST_MS) {
            Quaternion q = imu.getQuaternionOrientation();
            // The atan2 returns the half-angle, so double it and convert to degrees.
            double angle = -Math.atan2(q.z,q.w) * 2; // The atan gives us the half-angle.
            imuHz++;
        }
        telemetry.log().add("Read IMU at " + imuHz / 1000.0 + " hz");

        timer.reset();
        int potHz = 0;

        while (opModeIsActive() && timer.milliseconds() < TEST_MS) {
            double vol = encoder.getVoltage();
            potHz++;
        }
        telemetry.log().add("Read analog at " + potHz / 1000.0 + " hz");

        telemetry.setMsTransmissionInterval(20);

        while (opModeIsActive()) {
            Quaternion q = imu.getQuaternionOrientation();
            // The atan2 returns the half-angle, so double it and convert to degrees.
            double angle = -Math.atan2(q.z,q.w) * 2; // The atan gives us the half-angle.
            telemetry.addData("IMU", angle);
            telemetry.addData("Pot", encoder.getVoltage());
            telemetry.update();
        }
    }
}
