package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.seasons.velocityvortex.LinearOpModeBase;

/**
 * Created by ftc6347 on 3/6/17.
 */
@Disabled
@Autonomous(name = "Range gyro test", group = "utilities")
public class RangeGyroTest extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        autonomousInitLoop();

//        rangeGyroStrafe(0, 15, -65, -65);
        rangeGyroStrafe(0, 15, 70, 70);
    }
}
