package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Kaden on 11/30/2017.
 */
@TeleOp(name = "jewel arm thing", group = "linear OpMode")
public class JewelArmTest extends OpMode {
    JewelArm jewelArm;
    public void init() {
        jewelArm = new JewelArm(hardwareMap.servo.get("s4"), hardwareMap.colorSensor.get("cs1"), telemetry);
        jewelArm.up();
    }
    public void loop() {
        if(gamepad1.a) {
            jewelArm.setPostion(jewelArm.servo.getPosition() + 0.001);
        }
        if(gamepad1.b) {
            jewelArm.setPostion(jewelArm.servo.getPosition() - 0.001);
        }
        telemetry.addData("current pos: ", jewelArm.servo.getPosition());
        telemetry.update();
    }
}
