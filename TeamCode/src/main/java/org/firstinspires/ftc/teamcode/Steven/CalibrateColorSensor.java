
package org.firstinspires.ftc.teamcode.Steven;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import java.util.concurrent.locks.Lock;

/**
 * Color Sensor Calibration
 * AJ Foster | July 2016
 *
 * This code is meant to be an educational tool. Please deploy this code at
 * your own risk. You should never run code you don't understand.
 *
 * The following OpMode allows you to calibrate the Modern Robotics color
 * sensor using the A/B buttons on a gamepad. Button A (on gamepad 1) will
 * perform the black calibration, and Button B will perform the white
 * calibration. You can also use the X/Y buttons to turn the sensor's LED
 * on and off, respectively.
 */
public class CalibrateColorSensor extends OpMode implements I2cPortReadyCallback {

    // IMPORTANT!
    // What did you name your color sensor in the Robot Controller config?
    String color_sensor_name = "color";

    // Color Sensor hardware
    ModernRoboticsI2cColorSensor color_sensor;
    I2cController controller;

    // Variable to track the read/write mode of the sensor
    I2CMode controller_mode = I2CMode.READ;

    // Variables to prevent repeat calibration
    boolean isWorking = false;
    double timeStartedWorking = 0.0;

    // I2C address, registers, and commands
    public byte COLOR_SENSOR_ADDR = 0x3C;
    public byte COMMAND_CODE_BLACK = 0x42;
    public byte COMMAND_CODE_WHITE = 0x43;
    public byte COMMAND_CODE_LED_ON = 0x00;
    public byte COMMAND_CODE_LED_OFF = 0x01;
    public byte WRITE_CACHE_OFFSET = 4;


    /* In init(), we get a handle on the color sensor itself and its controller.
     * We need access to the cycle of events on the sensor (when the port is
     * ready for read/write activity). The portIsReady() method is registered
     * as a callback.
     */
    public void init() {

        // Remember to change the name of the color sensor in get().
        color_sensor = (ModernRoboticsI2cColorSensor) hardwareMap.colorSensor.get(color_sensor_name);

        // Get a handle on the color sensor's controller.
        controller = color_sensor.getI2cController();

        // Register the callback for portIsReady().
        controller.registerForI2cPortReadyCallback(this, color_sensor.getPort());

        // Unimportant.
       // telemetry.setSorted(false);
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

            sendCommand(COMMAND_CODE_BLACK);

            telemetry.addData("Black Calibration", "In Progress...");
        }

        // Button B should begin a white calibration.
        else if (gamepad1.b && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            sendCommand(COMMAND_CODE_WHITE);

            telemetry.addData("White Calibration", "In Progress...");
        }

        // Separately, allow the LED to be turned on and off.
        else if (gamepad1.x && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            sendCommand(COMMAND_CODE_LED_ON);

            telemetry.addData("LED Status", "Turning On");
        }

        else if (gamepad1.y && !isWorking) {

            // Prevent another command from running soon.
            isWorking = true;
            timeStartedWorking = getRuntime();

            sendCommand(COMMAND_CODE_LED_OFF);

            telemetry.addData("LED Status", "Turning Off");
        }

        // Give instructions to the user.
        else {
            telemetry.addData("Black Calibration", "Press A");
            telemetry.addData("White Calibration", "Press B");
            telemetry.addData("LED Status", "Press X/Y for On/Off");

            String reading = "R(" + color_sensor.red() +
                    ") G(" + color_sensor.green() +
                    ") B(" + color_sensor.blue() +
                    ") A(" + color_sensor.alpha() + ")";

            telemetry.addData("Reading", reading);
        }
    }


    /* In sendCommand(), we do the write process. Before reading or writing
     * anything, we need to lock the relevant cache. We enable write mode,
     * write our command code to the write cache, and then signal the
     * controller to re-enable read mode when the port is next ready.
     */
    public synchronized void sendCommand(byte command) {

        // Get a handle on the write cache and lock.
        int port = color_sensor.getPort();
        Lock wLock = controller.getI2cWriteCacheLock(port);
        byte[] wCache = controller.getI2cWriteCache(port);

        // Do the locking in a try/catch, in case a lock can't be made.
        try {

            // Lock the cache before anything.
            wLock.lock();

            // Enable write mode on the controller.
          //  controller.enableI2cWriteMode(port, COLOR_SENSOR_ADDR, 0x03, 1);


            // Write the supplied command to the relevant register.
            wCache[WRITE_CACHE_OFFSET] = command;

        } finally {

            // Ensure the cache is unlocked.
            wLock.unlock();

            // Signal portIsReady() to enable read mode when we're done.
            controller_mode = I2CMode.WRITE;
        }
    }


    /* When the I2C port is ready for read/write action, we may need to take
     * different actions depending on what we have queued. We use the
     * controller_mode variable to track the current state.
     */
    public synchronized void portIsReady(int port) {

        // I'm not sure why this needs to take place.
        controller.setI2cPortActionFlag(port);
        controller.readI2cCacheFromController(port);

        switch (controller_mode) {

            // Flush the write cache and set up a reset on next cycle.
            case WRITE:
                controller.writeI2cCacheToController(port);
                controller_mode = I2CMode.RESET;
                break;

            // During reset, we move back to read mode.
            case RESET:
                //controller.enableI2cReadMode(port, COLOR_SENSOR_ADDR, 0x03, 6);
                controller.writeI2cCacheToController(port);
                controller_mode = I2CMode.READ;
                break;

            // Let ModernRoboticsI2cColorSensor handle reading.
            case READ:
            default:
                break;
        }

        // Allow the ModernRoboticsI2cColorSensor class to handle the rest of
        // the portIsReady read/write cycle.
        color_sensor.portIsReady(port);
    }


    // Enum for the controller_mode variable.
    private enum I2CMode {
        READ, WRITE, RESET
    }
}