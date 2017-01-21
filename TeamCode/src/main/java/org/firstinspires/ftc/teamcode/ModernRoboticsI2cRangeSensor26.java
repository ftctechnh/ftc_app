/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

/**
 * {@link ModernRoboticsRangeSensor26} implements support for the MR ultrasonic/optical combo
 * range sensor.
 *
 * @see <a href="http://www.modernroboticsinc.com/range-sensor">MR Range Sensor</a>
 */
@I2cSensor(name = "MR Range Sensor 26", description = "a MR range sensor with i2c:0x26", xmlTag = "ModernRoboticsI2cRangeSensor26")
public class ModernRoboticsI2cRangeSensor26 extends I2cDeviceSynchDevice<I2cDeviceSynch> implements DistanceSensor, OpticalDistanceSensor, I2cAddrConfig
{
    //----------------------------------------------------------------------------------------------
    // Constants
    //----------------------------------------------------------------------------------------------

    public final static I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(0x26);

    public enum Register
    {
        FIRST(0),
        FIRMWARE_REV(0x00),
        MANUFACTURE_CODE(0x01),
        SENSOR_ID(0x02),
        ULTRASONIC(0x04),
        OPTICAL(0x05),
        LAST(OPTICAL.bVal),
        UNKNOWN(-1);

        public byte bVal;
        Register(int bVal) { this.bVal = (byte)bVal; }
    }

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    protected static final double apiLevelMin = 0.0;
    protected static final double apiLevelMax = 1.0;

    // Experimentally determined constants for converting optical measurements
    // to distance. See cmFromOptical() below.
    public double pParam = -1.02001;
    public double qParam = 0.00311326;
    public double rParam = -8.39366;
    public int    sParam = 10;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ModernRoboticsI2cRangeSensor26(I2cDeviceSynch deviceClient)
    {
        super(deviceClient, true);

        this.setOptimalReadWindow();
        this.deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);
        this.deviceClient.engage();

        super.registerArmingStateCallback();
    }

    public void setOptimalReadWindow()
    {
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(
                Register.FIRST.bVal,
                Register.LAST.bVal - Register.FIRST.bVal + 1,
                I2cDeviceSynch.ReadMode.REPEAT);
        this.deviceClient.setReadWindow(readWindow);
    }

    @Override
    protected synchronized boolean doInitialize()
    {
        return true;    // nothing to do
    }

    @Override
    public Manufacturer getManufacturer()
    {
        return Manufacturer.ModernRobotics;
    }

    @Override public String getDeviceName()
    {
        return String.format(Locale.getDefault(), "Modern Robotics Range Sensor %s",
                new RobotUsbDevice.FirmwareVersion(this.read8(Register.FIRMWARE_REV)));
    }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    public byte read8(Register reg)
    {
        return this.deviceClient.read8(reg.bVal);
    }

    public void write8(Register reg, byte value)
    {
        this.write8(reg, value, false);
    }
    public void write8(Register reg, byte value, boolean waitForCompletion)
    {
        this.deviceClient.write8(reg.bVal, value, waitForCompletion);
    }

    protected int readUnsignedByte(Register reg)
    {
        return TypeConversion.unsignedByteToInt(this.read8(reg));
    }

    //----------------------------------------------------------------------------------------------
    // I2cAddressConfig
    //----------------------------------------------------------------------------------------------

    @Override public void setI2cAddress(I2cAddr newAddress)
    {
        this.deviceClient.setI2cAddress(newAddress);
    }

    @Override public I2cAddr getI2cAddress()
    {
        return this.deviceClient.getI2cAddress();
    }

    //----------------------------------------------------------------------------------------------
    // DistanceSensor
    //----------------------------------------------------------------------------------------------

    public int rawUltrasonic()
    {
        return readUnsignedByte(Register.ULTRASONIC);
    }

    public int rawOptical()
    {
        return readUnsignedByte(Register.OPTICAL);
    }

    /*
        The manufacturer has this to say about the two sensors:

            "The ultrasonic element works by one of the transducers emitting a sound wave
            and the other receiving the sound wave. This reading is accurate between 5cm
            and approximately 255cm. Since the value returned is in units of centimeters,
            the return is linear.

            The optical element works by emitting infrared light from on LED and receiving
            infrared light to the other LED. The optical value can detect objects within 15cm.
            As an object approaches the optical element the returned value will increase at an
            exponential rate."

        We seek here to combine those into a single reading. Accordingly, some experiments are
        required. Several readings were taken at various altitudes from a wooden desk. The altitudes
        we measured from the flat back of the sensor as that was easiest. However, the sensor is
        17mm thick, so that could be compensated for. The raw readings were as follows:

            altitude
            back (cm)   reading
            2           250
            2.5         154
            3           90
            3.5         63
            4           49
            4.5         32
            5           26
            6           16
            7           10
            8           7
            9           5
            10          4

        An exponential curve was fitted to that data, using the following Mathematica command

            NonlinearModelFit[data, {a + b Exp[c x], a < 10 }, {{a, 1}, b, c}, x]

        That yielded the following expression for the optical reading (y) as a function of distance (x):

            y = 8.39366 + 321.207 exp(-0.980378 x)

        Inverting that relation, we have

            x == -1.02001 ln(0.00311326 (-8.39366+y))

        Experimentation indicates that this can be usefully used with a raw optical reading of 10
        or more, which corresponds to 5.40 cm.

        This parameterization is likely to be of utility to most. Those requiring finer precision
        or operating in environments in which the above experiment is invalid (for whatever reason)
        might consider running their own experiments and performing an analogous fitting process.
     */

    /**
     * Converts a reading of the optical sensor into centimeters. This computation
     * could be adjusted by altering the numeric parameters, or by providing an alternate
     * calculation in a subclass.
     */
    protected double cmFromOptical(int opticalReading)
    {
        if (opticalReading < sParam)
            return 0;
        else
            return pParam * Math.log(qParam * (rParam + opticalReading));
    }

    public double cmUltrasonic()
    {
        return rawUltrasonic();
    }

    public double cmOptical()
    {
        return cmFromOptical(rawOptical());
    }

    @Override public double getDistance(DistanceUnit unit)
    {
        double cmOptical = cmOptical();
        double cm        = cmOptical > 0 ? cmOptical : cmUltrasonic();
        return unit.fromUnit(DistanceUnit.CM, cm);
    }

    //----------------------------------------------------------------------------------------------
    // OpticalDistanceSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getLightDetected()
    {
        return Range.clip(
                Range.scale(getRawLightDetected(), 0, getRawLightDetectedMax(), apiLevelMin, apiLevelMax),
                apiLevelMin, apiLevelMax);
    }

    @Override public double getRawLightDetected()
    {
        return rawOptical();
    }

    @Override public double getRawLightDetectedMax()
    {
        return 255;
    }

    @Override public void enableLed(boolean enable)
    {
        // enabling or disabling the LED does nothing on this piece of hardware
    }

    @Override public String status()
    {
        return String.format(Locale.getDefault(), "%s on %s", getDeviceName(), getConnectionInfo());
    }
}
