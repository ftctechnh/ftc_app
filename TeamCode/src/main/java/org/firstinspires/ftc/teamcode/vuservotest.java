package org.firstinspires.ftc.teamcode;

/**
 * Created by Aus on 11/7/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Autonomous(name = "vuservotest", group = "Concept")
public class vuservotest extends LinearOpMode {
    Phone phone;
    public void runOpMode() throws InterruptedException {
        phone = new Phone(hardwareMap, telemetry);
        waitForStart();
        phone.getMark();
    }
}
