package org.firstinspires.ftc.teamcode.Jep_code;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by jeppe on 03-10-2017.
 */

public class spinner extends OpMode
{
    // -----Hardware-----
    // ---Motors---

    // ---Servos---
    // normal

    // continuous rotation
    CRServo spinner;

    // -----Variables-----
    double spin;

    @Overide
    public void init() {

        spinner = hardwareMap.crservo.get("sp");

    }

    @Override
    public void loop() {
        spin = gamepad2.right_trigger - gamepad2.left_trigger;
        spin = gamepad2.left_trigger - gamepad2.right_trigger;
        spinner.setPower(spin);
        telemetry.addData("spinner", spin);
    }
}