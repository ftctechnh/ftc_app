package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.seasons.velocityvortex.LinearOpModeBase;

/**
 * Created by ftc6347 on 4/10/17.
 */
@Disabled
@TeleOp(name = "Gyro pivot test", group = "tests")
public class GyroPivotTest extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        waitForStart();
        
//        rangeGyroStrafe(0, 20, 36, RobotDirection.LEFT);
        encoderDrive(0.8, 36, RobotDirection.LEFT);
    }
}
