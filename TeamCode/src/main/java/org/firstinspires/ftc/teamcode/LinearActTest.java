package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by AshQuinn on 10/6/18.
 */

public class LinearActTest extends LinearOpMode {
    public Servo LinAct;

    @Override
    public void runOpMode() {
        LinAct = hardwareMap.servo.get("LinAct");
        waitForStart();
        LinAct.setPosition (0.0);
    }
}
