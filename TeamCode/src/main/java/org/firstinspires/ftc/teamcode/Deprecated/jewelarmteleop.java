package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by Aus on 10/24/2017.
 */
public class jewelarmteleop extends OpMode {
    private Servo claw;
    private double servolowend = 0.0;
    private double servohighend = 1.0;


    @Override

    public void init() {
        claw = hardwareMap.servo.get("s1");
        claw.setPosition(servohighend);

    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            claw.setPosition(Range.clip(claw.getPosition() + 0.003, servolowend, servohighend));
            //claw.setPosition(servolowend);
        }
        if (gamepad1.b) {
            claw.setPosition(Range.clip(claw.getPosition() - 0.003, servolowend, servohighend));
            //claw.setPosition(servohighend);
        }
        telemetry.addData("clawposition: ", claw.getPosition());
    }

}
