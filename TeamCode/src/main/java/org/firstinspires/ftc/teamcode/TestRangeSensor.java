package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by FTC ROBOTICS on 2/7/2018.
 */
@TeleOp(name = "testing range sensor", group = "test")
public class TestRangeSensor extends OpMode {
    private AutoDrive drive;
    public void init() {
        drive = new AutoDrive(hardwareMap, telemetry);
    }
    public void loop() {
        telemetry.addData("Distance",drive.getDistance());
    }
}
