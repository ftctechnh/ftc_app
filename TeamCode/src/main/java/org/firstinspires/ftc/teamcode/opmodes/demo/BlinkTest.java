package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.libraries.hardware.StupidColor;

/**
 * Created by Noah on 3/6/2018.
 */

@TeleOp(name="Lights On")
@Disabled
public class BlinkTest extends OpMode {
    private DcMotor glow;
    private ColorSensor frontColor;
    private ColorSensor backColor;


    public void init() {
        glow = hardwareMap.get(DcMotor.class, "g");
        frontColor = new StupidColor(hardwareMap.get(AdafruitI2cColorSensor.class, "fc"));
        backColor = new StupidColor(hardwareMap.get(AdafruitI2cColorSensor.class, "bc"));

        frontColor.enableLed(false);
        backColor.enableLed(false);
        glow.setPower(1);
    }

    public void start() {

    }

    public void loop() {

    }

    public void stop() {
        glow.setPower(0);
    }
}
