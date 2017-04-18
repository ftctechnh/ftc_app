package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;


/**
 * Created by aburger on 3/5/2017.
 * <p>
 * Reference: https://ftc-tricks.com/color-sensor-calibration/
 * <p>
 * Another Reference:
 * http://ftcforum.usfirst.org/archive/index.php/t-6757.html
 * https://github.com/trc492/Ftc2016FirstResQ/blob/master/FtcRobotController/src/main/java/ftclib/FtcMRI2cColorSensor.java
 * https://github.com/trc492/Ftc2017VelocityVortex/blob/master/Ftc3543Lib/src/main/java/ftclib/FtcMRI2cColorSensor.java
 */
@Disabled
@TeleOp(name = "Calibrate Color Sensor", group = "utilities")
public class CalibrateColorSensor extends OpMode {


    // Color Sensor hardware
    ModernRoboticsI2cColorSensor color_sensor1;
    ModernRoboticsI2cColorSensor color_sensor2;


    // Variables to prevent repeat calibration
    boolean isWorking = false;
    double timeStartedWorking = 0.0;
    COLORSENSOR SENSOR = COLORSENSOR.SENSOR1;
    private ColorController colorController1;
    private ColorController colorController2;

    /* In init(), we get a handle on the color sensor itself and its controller.
     * We need access to the cycle of events on the sensor (when the port is
     * ready for read/write activity). The portIsReady() method is registered
     * as a callback.
     */
    public void init() {

        // Remember to change the name of the color sensor in get().

        //old color_sensor = (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get(color_sensor_name);
        color_sensor1 = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "clr");
        color_sensor1.setI2cAddress(I2cAddr.create8bit(0x3C));
        colorController1 = new ColorController(color_sensor1);
        colorController1.register();

        color_sensor2 = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "clr2");
        color_sensor2.setI2cAddress(I2cAddr.create8bit(0x3E));
        colorController2 = new ColorController(color_sensor2);
        colorController2.register();


    }

    // Respond to gamepad input.
    public void loop() {

        // If enough time has elapsed since the last time we started a
        // calibration, allow another one to be started.
        if (isWorking) {

            // The if-statements are written in this way so that no
            // telemetry is written while an operation is taking place.

            // Reset the flag.
            if (getRuntime() - timeStartedWorking > 2.0)
                isWorking = false;
        }

        // Button A should begin a black calibration.
        else if (gamepad1.a && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            if (SENSOR == COLORSENSOR.SENSOR1) {
                colorController1.blackCal();
            } else {
                colorController2.blackCal();
            }

            telemetry.addData("Black Calibration", "In Progress...");
        }

        // Button B should begin a white calibration.
        else if (gamepad1.b && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            if (SENSOR == COLORSENSOR.SENSOR1) {
                colorController1.whiteCal();
            } else {
                colorController2.whiteCal();
            }

            telemetry.addData("White Calibration", "In Progress...");
        }

        // Separately, allow the LED to be turned on and off.
        else if (gamepad1.x && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            if (SENSOR == COLORSENSOR.SENSOR1) {
                colorController1.ledOn();
            } else {
                colorController2.ledOn();
            }

            telemetry.addData("LED Status", "Turning On");
        } else if (gamepad1.y && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            if (SENSOR == COLORSENSOR.SENSOR1) {
                colorController1.ledOff();
            } else {
                colorController2.ledOff();
            }

            telemetry.addData("LED Status", "Turning Off");
        } else if (gamepad1.right_bumper && !isWorking) {
            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            if (SENSOR == COLORSENSOR.SENSOR1) {
                SENSOR = COLORSENSOR.SENSOR2;
            } else {
                SENSOR = COLORSENSOR.SENSOR1;
            }
            telemetry.addData(">", "Switch color sensors" + SENSOR.value);

        }

        // Give instructions to the user.
        else {
            telemetry.addData("Black Calibration", "Press A");
            telemetry.addData("White Calibration", "Press B");
            telemetry.addData("LED Status", "Press X/Y for On/Off");
            telemetry.addData(color_sensor1.getConnectionInfo(), reading(color_sensor1));
            telemetry.addData(color_sensor2.getConnectionInfo(), reading(color_sensor2));

        }
    }

    private String reading(ColorSensor colorSensor) {
        StringBuilder string = new StringBuilder();
        string.append("R(");
        string.append(colorSensor.red());
        string.append(") G(");
        string.append(colorSensor.green());
        string.append(") B(");
        string.append(colorSensor.blue());
        string.append(") A(");
        string.append(colorSensor.alpha());
        string.append(")");
        return string.toString();
    }

    private enum COLORSENSOR {
        SENSOR1("sensor1"), SENSOR2("sensor2");
        private String value;

        COLORSENSOR(String value) {
            this.value = value;
        }

    }


}
