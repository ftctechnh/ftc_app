package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BeehiveVuforia;

/**
 * Created by root on 12/26/17.
 */
public class BeehiveVuforiaImpl extends OpMode {
    BeehiveVuforia v;
    public void init() {
        v = new BeehiveVuforia(hardwareMap, telemetry);
    }
    public void loop() {
        v.getMark();
    }
}
