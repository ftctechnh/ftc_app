package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Inspiration Team on 3/18/2018.
 */

@TeleOp(name = "OO Color Test REV")
public class ColorSensorOOPTest extends Autonomous_General_George {
    @Override
    public void runOpMode() {
        initiate(false);
        waitForStart();
        while (opModeIsActive()) {
            colorState state = jewelSensor.getValue();
            switch (state) {
                case RED:
                    telemetry.addData("color is red", "");
                    telemetry.update();
                    break;
                case BLUE:
                    telemetry.addData("color is blue", "");
                    telemetry.update();
                    break;
                case OTHER:
                    telemetry.addData("idk", "");
                    telemetry.update();
                    break;
                default:
                    telemetry.addData("default state executed", "");
                    telemetry.update();
                    break;
            }
        }

    }
}
