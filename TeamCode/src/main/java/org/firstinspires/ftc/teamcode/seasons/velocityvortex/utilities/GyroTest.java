package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.seasons.velocityvortex.LinearOpModeBase;

/**
 * Created by ftc6347 on 1/30/17.
 */
@Disabled
@Autonomous(name = "Gyro test", group = "utilities")
public class GyroTest extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        telemetry.addData("gyro value", getGyroSensor().getIntegratedZValue());
        telemetry.update();

        waitForStart();

        // gyro pivot 90 degrees
        gyroPivot(0.5, 10, false);
    }
}
