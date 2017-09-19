package org.firstinspires.ftc.teamcode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Robot.TestBotHardware;

/**
 * Created by gallagherb20503 on 9/19/2017.
 */
@TeleOp(name="TestbotColorSensor", group="TeleOp")
public class ColorSensingTeleOp extends OpMode{
    TestBotHardware testBot= new TestBotHardware();
    @Override
    public void init() {
        testBot.init(hardwareMap);
    }

    @Override
    public void loop() {


        telemetry.addData("blue", "%d",testBot.colorSensor.blue());
        telemetry.addData("red", "%d",testBot.colorSensor.red());
        telemetry.addData("green", "%d",testBot.colorSensor.green());
    telemetry.update();
    }
}
