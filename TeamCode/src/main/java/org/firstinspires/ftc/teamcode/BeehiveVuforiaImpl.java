package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by root on 12/26/17.
 */
@TeleOp(name = "vuforiatest", group = "test")
public class BeehiveVuforiaImpl extends OpMode {
    BeehiveVuforia v = new BeehiveVuforia();
    public void init() {

    }
    public void loop() {
        v.getMark(hardwareMap, telemetry);
    }
}
