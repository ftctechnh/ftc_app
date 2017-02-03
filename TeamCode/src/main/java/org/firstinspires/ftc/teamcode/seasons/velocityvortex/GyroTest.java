package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by ftc6347 on 1/30/17.
 */
@Autonomous(name = "Gyro test", group = "tests")
public class GyroTest extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        telemetry.addData("gyro value", getGyroSensor().getIntegratedZValue());
        telemetry.update();

        waitForStart();

        // gyro pivot 90 degrees
        gyroPivot(0.5, 10);
    }
}
