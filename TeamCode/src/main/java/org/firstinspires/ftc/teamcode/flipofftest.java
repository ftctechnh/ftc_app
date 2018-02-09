package org.firstinspires.ftc.teamcode;

/**
 * Created by Aus on 11/7/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

//@Autonomous(name = "flipofftest", group = "Concept")
public class flipofftest extends LinearOpMode {
    JewelArm JewelArm;
    public void runOpMode() throws InterruptedException {
        JewelArm = new JewelArm(hardwareMap, telemetry);
        waitForStart();
        JewelArm.findJewel(Color.RED);
        sleep(1000);

    }
}
