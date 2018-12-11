/*
 * Copyright (c) 2014, 2015 Qualcomm Technologies Inc
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Qualcomm Technologies Inc nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.concurrent.locks.Lock;

public class ModernRoboticsI2cColorSensor2 extends I2cControllerPortDeviceImpl implements ColorSensor, I2cController.I2cPortReadyCallback {

    //------------------------------------------------------------------------------------------------
    // Constants
    //------------------------------------------------------------------------------------------------

    public final static I2cAddr DEFAULT_I2C_ADDRESS = I2cAddr.create8bit(0x3C);
    public final static int ADDRESS_COMMAND = 0x03;
    public final static int ADDRESS_COLOR_NUMBER = 0x04;
    public final static int OFFSET_COMMAND = 0x0 + ModernRoboticsUsbDeviceInterfaceModule.OFFSET_I2C_PORT_MEMORY_BUFFER;
    public final static int OFFSET_COLOR_NUMBER = 0x01 + ModernRoboticsUsbDeviceInterfaceModule.OFFSET_I2C_PORT_MEMORY_BUFFER;
    public final static int OFFSET_RED_READING = 0x02 + ModernRoboticsUsbDeviceInterfaceModule.OFFSET_I2C_PORT_MEMORY_BUFFER;
    public final static int OFFSET_GREEN_READING = 0x03 + ModernRoboticsUsbDeviceInterfaceModule.OFFSET_I2C_PORT_MEMORY_BUFFER;
    public final static int OFFSET_BLUE_READING = 0x04 + ModernRoboticsUsbDeviceInterfaceModule.OFFSET_I2C_PORT_MEMORY_BUFFER;
    public final static int OFFSET_ALPHA_VALUE = 0x05 + ModernRoboticsUsbDeviceInterfaceModule.OFFSET_I2C_PORT_MEMORY_BUFFER;
    public final static int BUFFER_LENGTH = 0x06;
    public final static int COMMAND_PASSIVE_LED = 0x01;
    public final static int COMMAND_ACTIVE_LED = 0x00;

    //------------------------------------------------------------------------------------------------
    // State
    //------------------------------------------------------------------------------------------------

    private volatile I2cAddr i2cAddr = DEFAULT_I2C_ADDRESS; // this can be changed by the user
    private          byte[]  readBuffer;
    private          Lock    readLock;
    private          byte[]  writeBuffer;
    private          Lock    writeLock;

    private enum State {READING_ONLY, PERFORMING_WRITE, SWITCHING_TO_READ};
    private State state = State.READING_ONLY;
    private int lastCommand = COMMAND_ACTIVE_LED; // NXT Sensor starts up with the LED on.

    //------------------------------------------------------------------------------------------------
    // Construction
    //------------------------------------------------------------------------------------------------

    public ModernRoboticsI2cColorSensor2(I2cController module, int physicalPort) {
        super(module, physicalPort);
        finishConstruction();
    }

    @Override
    protected void controllerNowArmedOrPretending() {
        this.readBuffer  = controller.getI2cReadCache(physicalPort);
        this.readLock    = controller.getI2cReadCacheLock(physicalPort);
        this.writeBuffer = controller.getI2cWriteCache(physicalPort);
        this.writeLock   = controller.getI2cWriteCacheLock(physicalPort);

        controller.enableI2cReadMode(physicalPort, i2cAddr, ADDRESS_COMMAND, BUFFER_LENGTH);
        controller.setI2cPortActionFlag(physicalPort);
        controller.writeI2cCacheToController(physicalPort);

        controller.registerForI2cPortReadyCallback(this, physicalPort);
    }

    //------------------------------------------------------------------------------------------------
    // Operations
    //------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.format("argb: %d", argb());
    }

    @Override
    public int red() {
        return getColor(OFFSET_RED_READING);
    }

    @Override
    public int green() {
        return getColor(OFFSET_GREEN_READING);
    }

    @Override
    public int blue() {
        return getColor(OFFSET_BLUE_READING);
    }

    @Override
    public int alpha() {
        return getColor(OFFSET_ALPHA_VALUE);
    }

    public int colorNumber() {
        return getColor(OFFSET_COLOR_NUMBER);
    }

    @Override
    public int argb() {
        return Color.argb(alpha(), red(), green(), blue());
    }



    @Override
    public synchronized void enableLed(boolean enable) {

        byte command = COMMAND_PASSIVE_LED;
        if (enable) {
            command = COMMAND_ACTIVE_LED;
        }

        if (lastCommand == command) { // switching is expensive
            return;
        }

        lastCommand = command;
        state = State.PERFORMING_WRITE;

        try {
            writeLock.lock();
            writeBuffer[OFFSET_COMMAND] = command;
        } finally {
            writeLock.unlock();
        }
    }

    private int getColor(int OFFSET) {
        byte color;
        try {
            readLock.lock();
            color = readBuffer[OFFSET];
        } finally {
            readLock.unlock();
        }
        return TypeConversion.unsignedByteToInt(color);
    }

    @Override public Manufacturer getManufacturer() {
        return Manufacturer.ModernRobotics;
    }

    @Override
    public String getDeviceName() {
        return "Modern Robotics I2C Color Sensor";
    }

    @Override
    public String getConnectionInfo() {
        return controller.getConnectionInfo() + "; I2C port: " + physicalPort;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
    }

    @Override
    public void close() {
        // take no action
    }

    @Override
    public synchronized void portIsReady(int port) {
        controller.setI2cPortActionFlag(physicalPort);
        controller.readI2cCacheFromController(physicalPort);

        if (state == State.PERFORMING_WRITE) {
            controller.enableI2cWriteMode(physicalPort, i2cAddr, ADDRESS_COMMAND, BUFFER_LENGTH);
            controller.writeI2cCacheToController(physicalPort);
            state = State.SWITCHING_TO_READ;
        } else if (state == State.SWITCHING_TO_READ) {
            controller.enableI2cReadMode(physicalPort, i2cAddr, ADDRESS_COMMAND, BUFFER_LENGTH);
            controller.writeI2cCacheToController(physicalPort);
            state = State.READING_ONLY;
        } else {
            controller.writeI2cPortFlagOnlyToController(physicalPort);
        }
    }

    @Override
    public void setI2cAddress(I2cAddr newAddress) {
        ModernRoboticsUsbDeviceInterfaceModule.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
        RobotLog.i(getDeviceName() + ", just changed I2C address. Original address: " + i2cAddr.get8Bit() + ", new address: " + newAddress.get8Bit());

        i2cAddr = newAddress;

        controller.enableI2cReadMode(physicalPort, i2cAddr, ADDRESS_COMMAND, BUFFER_LENGTH);
        controller.setI2cPortActionFlag(physicalPort);
        controller.writeI2cCacheToController(physicalPort);

        controller.registerForI2cPortReadyCallback(this, physicalPort);
    }

    @Override
    public I2cAddr getI2cAddress() {
        return i2cAddr;
    }
}
