package org.firstinspires.ftc.teamcode;

/**
 * Created by Aus on 11/7/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name = "vuservotest", group = "Concept")
public class vuservotest extends LinearOpMode {
    BeehiveVuforia BeehiveVuforia;
    public void runOpMode() throws InterruptedException {
        BeehiveVuforia = new BeehiveVuforia(hardwareMap, telemetry);
        waitForStart();
        telemetry.addData("the thing is ", BeehiveVuforia.getMark());
        Systems.sleep(50000);
    }
}
