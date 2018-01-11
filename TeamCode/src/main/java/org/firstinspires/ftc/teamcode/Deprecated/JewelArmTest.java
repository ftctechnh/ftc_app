package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by Kaden on 11/30/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.JewelArm;

public class JewelArmTest extends OpMode {
    JewelArm JewelArm;
    public void init() {
        JewelArm = new JewelArm(hardwareMap, telemetry);
        JewelArm.up();
    }
    public void loop() {
        if(gamepad1.a) {
            JewelArm.setPostion(JewelArm.servo.getPosition() + 0.001);
        }
        if(gamepad1.b) {
            JewelArm.setPostion(JewelArm.servo.getPosition() - 0.001);
        }
        telemetry.addData("current pos: ", JewelArm.servo.getPosition());
        telemetry.update();
    }
}
