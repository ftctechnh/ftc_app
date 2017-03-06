package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.seasons.velocityvortex.LinearOpModeBase;

/**
 * Created by ftc6347 on 2/3/17.
 */
@Autonomous(name = "Calibrate gyro", group = "utilities")
public class CalibrateGyro extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        waitForStart();

        telemetry.addData(">", "Calibrating Gyro");
        telemetry.update();

        getGyroSensor().calibrate();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && getGyroSensor().isCalibrating()) {
            idle();
        }

        telemetry.addData(">", "Gyro caibrated");
        telemetry.update();
    }
}
