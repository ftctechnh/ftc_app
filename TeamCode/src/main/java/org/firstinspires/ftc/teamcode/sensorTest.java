package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by anshnanda on 04/02/18.
 */

@TeleOp (name = "sensor value test", group = "test")

public class sensorTest extends LinearOpMode{


    private static ColorSensor jColor;

    @Override
    public void runOpMode() throws InterruptedException{

        jColor = hardwareMap.colorSensor.get("colF");
        jColor.enableLed(true);
        telemetry.clear();

        waitForStart();

        while (opModeIsActive()){

            if (gamepad1.a){
                telemetry.addData("red: ", jColor.red());
                telemetry.update();
            }

            if (gamepad1.b){
                telemetry.addData("green: ", jColor.green());
                telemetry.update();
            }

            if (gamepad1.x){
                telemetry.addData("blue: ", jColor.blue());
                telemetry.update();
            }

            if (gamepad1.dpad_up){
                jColor.enableLed(true);
            }

            if (gamepad1.dpad_down){
                jColor.enableLed(false);
            }

            idle();

        }

    }

}
