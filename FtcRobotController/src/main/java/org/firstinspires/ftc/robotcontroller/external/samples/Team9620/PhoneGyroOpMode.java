package org.firstinspires.ftc.robotcontroller.external.samples.Team9620;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Samuel "Red" Donovan on 12/14/2016.
 */
@TeleOp(name="Phone Sensors Ex", group="Linear OpMode")
@Disabled
public class PhoneGyroOpMode extends LinearOpMode {

    org.firstinspires.ftc.robotcontroller.external.samples.Team9620.PhoneSensor accelerometer;

    public void init(HardwareMap hwMap){
        accelerometer = new PhoneSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_FASTEST, hwMap);
    }

    @Override
    public void runOpMode() {
        //When init is hit
        this.init(hardwareMap);

        //When play is hit
        waitForStart();
        while(opModeIsActive()){
            //Retrieve and display acc. data
            telemetry.addData("X", accelerometer.values[0]);
            telemetry.addData("Y", accelerometer.values[1]);
            telemetry.addData("Z", accelerometer.values[2]);
            telemetry.update();
        }
        //When stop is hit
        this.stopOpMode();
    }

    public void stopOpMode(){
        accelerometer.unregister();
    }
}