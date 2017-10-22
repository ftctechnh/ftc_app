package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by pston on 9/18/2017
 */

public class REVTest extends OpMode {
    private DcMotor testMotor;

    @Override
    public void init() {
        testMotor = hardwareMap.dcMotor.get("test");
    }

    @Override
    public void loop() {
        testMotor.setPower(0.5);
    }
}
