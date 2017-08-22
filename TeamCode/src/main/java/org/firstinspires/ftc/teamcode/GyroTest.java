package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;


/**
 * Created by Nora on 8/21/2017.
 */

@Autonomous(name="GoForward", group="GryoThingForTheSummer")

public class GyroTest {
    Gyroscope
            gyroscope = hardwareMap.get(Gyroscope.class, "gyrofreesummer");
    @Override
    public void runOpMode() throws InterruptedException {
        double gyrorotationfraction = gyroscope.getAngularVelocityAxes();
        int currentCount = 0;
        int elapsedSeconds = 0;
        telemetry.addData("count", currentCount);
        telemetry.addData("elapsedTime", "%.3f", elapsedSeconds);
        telemetry.update(gyrorotationfraction);

    }
}
