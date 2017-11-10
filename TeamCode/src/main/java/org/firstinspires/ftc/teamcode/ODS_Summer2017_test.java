package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/**
 * Created by Eric on 8/7/2017.
 */


@TeleOp (name="Eric: test ODS ", group="Eric ODS")//name and things
@Disabled
public class ODS_Summer2017_test extends LinearOpMode{

    OpticalDistanceSensor ods; //define sensor input name

    @Override
    public void runOpMode() {

        ods = hardwareMap.opticalDistanceSensor.get("sensor_ods");//get sensor input

        waitForStart();//what the code implies

        while (opModeIsActive()){
            //shows stuff about things (specifically the sensor output)

            telemetry.addData("Raw",    ods.getRawLightDetected());//show raw light intensity
            telemetry.addData("Normal", ods.getLightDetected());//show normal light intensity
            telemetry.update();//probably updates what it says

        }
    }


}
