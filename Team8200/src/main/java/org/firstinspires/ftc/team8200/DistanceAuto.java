package org.firstinspires.ftc.team8200;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 *
 * This is an example LinearOpMode that shows how to use
 * a legacy (NXT-compatible) Light Sensor.
 * It assumes that the light sensor is configured with a name of "sensor_light".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Sensor: Ultrasonic Distance Sensor kev", group = "Sensor")
//@Disabled
public class DistanceAuto extends LinearOpMode {

    DeviceInterfaceModule dim;                  // Device Object
    AnalogInput distanceSensor;
    double voltage, maxVoltage, voltsPerInch, voltageInInches;
    private ElapsedTime runtime = new ElapsedTime();
    String kev = "klk";
//    double finalViI = (double) Math.round(voltageInInches * 100) / 100;
//    double expectedCloseVal = 12.51;
    @Override
    public void runOpMode() {
        dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");   //  Use generic form of device mapping
        distanceSensor = hardwareMap.get(AnalogInput.class, "distance");

        waitForStart();
        while (opModeIsActive()) {
            voltage = distanceSensor.getVoltage();
            maxVoltage = distanceSensor.getMaxVoltage();
            voltsPerInch = 5.0/512.0;
            voltageInInches = voltage/voltsPerInch;
            while (voltageInInches <= 13) {
                runtime.reset();
                if (runtime.seconds() > 3.0) {
                    telemetry.addData("distanceTest. will print klk", kev);
                    telemetry.update();
                }
            }
            telemetry.addData("Voltage", voltageInInches);
            telemetry.addData("Max Voltage", maxVoltage);
            telemetry.update();
        }
    }
}