package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Easy way to interface with hardware.
 */
public class HW {
    private final HardwareMap hardwareMap;
    public Harvester harvester;
    public Sorter sorter;

    /**
     * Constructor.
     *
     * @param hardwareMap from the OpMode. Used to access the hardware.
     */
    public HW(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.harvester = new Harvester("harvester");
        this.sorter = new Sorter("sorter");
    }

    /**
     * Abstract superclass to be used for the hardware devices
     *
     * @param <T> the type of device.
     */
    abstract class HWDevice<T extends HardwareDevice> {
        protected final T device;

        public HWDevice(String deviceStr) {
            this.device = (T) HW.this.hardwareMap.get(deviceStr);
        }
    }

    class Harvester extends HWDevice<DcMotor> {
        public Harvester(String deviceStr) {
            super(deviceStr);
        }

        /**
         * Runs the harvester forward at 50% power.
         */
        public void forward() {
            this.device.setPower(.5);
        }

        /**
         * Stops the harvester.
         */
        public void stop() {
            this.device.setPower(0);
        }

        /**
         * Runs the harvester backwards at 50% power.
         */
        public void reverse() {
            this.device.setPower(-.5);
        }
    }

    class Sorter extends HWDevice<Servo> {
        public Sorter(String deviceStr) {
            super(deviceStr);
        }

        public void blue() {
            this.device.setPosition(212.0 / 255.0);
        }

        public void red() {
            this.device.setPosition(32.0 / 255.0);
        }
    }

    class MidColor extends HWDevice<ColorSensor> {
        public MidColor(String deviceStr) {
            super(deviceStr);
            this.device.setI2cAddress(I2cAddr.create7bit(0x0c));
        }

        /**
         * Sets the color sensor in active mode.
         */
        public void enableLed() {
            this.device.enableLed(true);
        }

        /**
         * Sets the color sensor in passive mode.
         */
        public void disableLed() {
            this.device.enableLed(false);
        }

        /**
         * Whether the sensor detects green.
         *
         * @return true if the sensor detects green. False otherwise.
         */
        public boolean isGreen() {
            return true; // TODO: implement me!
        }
    }
}
