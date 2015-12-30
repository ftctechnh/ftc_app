package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Christopher on 2015-12-29.
 *
 * Change log:
 * 1.0.0 - First version.
 */

public class ServoTest extends OpMode {

    VersionNumber version = new VersionNumber(1,0,0);
    DumpClimberController dumperCtrl;
    boolean first = true;

    @Override
    public void init() {
        telemetry.addData("OpMode","PacmanBotManual");
        telemetry.addData("Version", version.string());
//        telemetry.addData("Message", "1.5");
 //       telemetry.addData("HWB Version", hwbVersion.string());

        dumperCtrl = new DumpClimberController(hardwareMap.servo.get("thrower"));
    }

    @Override
    public void loop() {

        dumperCtrl.check();

        if (first)
        {
            first = false;
            dumperCtrl.dumperInit();
            dumperCtrl.startDump();
        }

    }
}
