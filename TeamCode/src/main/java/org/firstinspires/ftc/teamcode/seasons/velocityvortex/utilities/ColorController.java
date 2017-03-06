package org.firstinspires.ftc.teamcode.seasons.velocityvortex.utilities;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;

import java.util.concurrent.locks.Lock;

/**
 * Created by aburger on 3/5/2017.
 */

class ColorController  implements I2cController.I2cPortReadyCallback {

    ModernRoboticsI2cColorSensor _colorSensor;
    I2cController controller;

    // Variable to track the read/write mode of the sensor
    I2CMode controller_mode = I2CMode.READ;

    // I2C address, registers, and commands
    private I2cAddr colorSensorAddr = I2cAddr.create7bit(0x3E);
    private static final byte COMMAND_CODE_BLACK = 0x42;
    private static final byte COMMAND_CODE_WHITE = 0x43;
    private static final byte COMMAND_CODE_LED_ON = 0x00;
    private static final byte COMMAND_CODE_LED_OFF = 0x01;
    private static final byte WRITE_CACHE_OFFSET = 4;

    // Enum for the controller_mode variable.
    private enum I2CMode {
        READ, WRITE, RESET
    }




    public ColorController(ModernRoboticsI2cColorSensor colorSensor) {
        _colorSensor = colorSensor;
        controller = colorSensor.getI2cController();
        colorSensorAddr = colorSensor.getI2cAddress();

    }

    public void register(){
        // Register the callback for portIsReady().
        controller.registerForI2cPortReadyCallback(this, _colorSensor.getPort());
    }

    @Override
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
                controller.enableI2cReadMode(port, colorSensorAddr, 0x03, 6);
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
        _colorSensor.portIsReady(port);
    }

    /* In sendCommand(), we do the write process. Before reading or writing
    * anything, we need to lock the relevant cache. We enable write mode,
    * write our command code to the write cache, and then signal the
    * controller to re-enable read mode when the port is next ready.
    */
    private synchronized void sendCommand(byte command) {

        // Get a handle on the write cache and lock.
        int port = _colorSensor.getPort();
        Lock wLock = controller.getI2cWriteCacheLock(port);
        byte[] wCache = controller.getI2cWriteCache(port);

        // Do the locking in a try/catch, in case a lock can't be made.
        try {

            // Lock the cache before anything.
            wLock.lock();

            // Enable write mode on the controller.
            controller.enableI2cWriteMode(port, colorSensorAddr, 0x03, 1);

            // Write the supplied command to the relevant register.
            wCache[WRITE_CACHE_OFFSET] = command;

        } finally {

            // Ensure the cache is unlocked.
            wLock.unlock();

            // Signal portIsReady() to enable read mode when we're done.
            controller_mode = I2CMode.WRITE;
        }
    }

    public void ledOn(){
        sendCommand(COMMAND_CODE_LED_ON);
    }
    public void ledOff(){
        sendCommand(COMMAND_CODE_LED_OFF);
    }
    public void blackCal(){
        sendCommand(COMMAND_CODE_BLACK);
    }
    public void whiteCal(){
        sendCommand(COMMAND_CODE_WHITE);
    }


}
