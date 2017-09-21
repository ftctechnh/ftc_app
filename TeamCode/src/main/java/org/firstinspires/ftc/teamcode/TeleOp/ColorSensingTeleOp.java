package org.firstinspires.ftc.teamcode.TeleOp;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.teamcode.Robot.TestBotHardware;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by gallagherb20503 on 9/19/2017.
 */
@TeleOp(name="TestbotColorSensor", group="TeleOp")
public class ColorSensingTeleOp extends OpMode {
    TestBotHardware testBot = new TestBotHardware();
    HardwareMap hwMap;
    @Override
    public void init() {
        testBot.init(hardwareMap);
        hwMap = hardwareMap;
    }

    @Override
    public void loop() {


        telemetry.addData("blue", "%d", testBot.colorSensor.blue());
        telemetry.addData("red", "%d", testBot.colorSensor.red());
        telemetry.addData("green", "%d", testBot.colorSensor.green());
        telemetry.update();

        int alpha = testBot.colorSensor.alpha();
        int red   = testBot.colorSensor.red();
        int green = testBot.colorSensor.green();
        int blue  = testBot.colorSensor.blue();


        red = Range.clip(red, 0, 255);
        green = Range.clip(green, 0, 255);
        blue = Range.clip(blue, 0, 255);
        final int argb = Color.argb(0xff,red,green,blue);

        Activity appContext = (Activity) hwMap.appContext;

        final View view =
                appContext.findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        view.post(  new Runnable(){
            public void run(){
                view.setBackgroundColor(argb);
            }
        });


    }
}
