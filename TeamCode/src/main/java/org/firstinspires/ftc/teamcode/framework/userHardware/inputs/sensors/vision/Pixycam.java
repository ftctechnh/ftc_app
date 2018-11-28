package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractOpMode;

@I2cDeviceType()
@DeviceProperties(xmlTag = "PIXYCAMERA", name = "PixyCam", description = "Camera with fast on board vision processing")

public class Pixycam extends I2cDeviceSynchDevice<I2cDeviceSynch> implements HardwareDevice {

    private I2cAddr pixyAddress = I2cAddr.create7bit(0x54);

    //Constructor
    public Pixycam(I2cDeviceSynch device) {
        super(device, true);

        this.deviceClient.setI2cAddress(pixyAddress);

        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(0x0e, 22, I2cDeviceSynch.ReadMode.REPEAT);

        this.deviceClient.setReadWindow(readWindow);

        this.deviceClient.engage();

        setLight(false);
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "Pixycam";
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    protected boolean doInitialize() {
        // This is where we write any necessary configuration to the device
        return true;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {
        this.deviceClient.close();
    }

    public byte[] getFullVersion() {
        return getBytes(Register.REQUEST_FIRMWARE_VERSION.val, 26);
    }

    public String getVersionString() {

        byte[] blocks = getFullVersion();

        String version = "";

        version += Character.toString((char) blocks[12]);//1
        version += Character.toString((char) blocks[13]);//2
        version += Character.toString((char) blocks[14]);//3
        version += Character.toString((char) blocks[15]);//4
        version += Character.toString((char) blocks[16]);//5
        version += Character.toString((char) blocks[17]);//6
        version += Character.toString((char) blocks[18]);//7
        version += Character.toString((char) blocks[19]);//8
        version += Character.toString((char) blocks[20]);//9
        version += Character.toString((char) blocks[21]);//10

        return version;
    }

    public byte[] getBytes(int request, int numBytes) {
        byte[] commands = {(byte) 0xae, (byte) 0xc1, (byte) request, (byte) 0x00};
        this.deviceClient.write(0x54, commands);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this.deviceClient.read(0x54, numBytes);
    }

    public void setLight(boolean on) {
        if (on) {
            byte[] commands = {(byte) 0xae, (byte) 0xc1, (byte) 0x16, (byte) 0x02, (byte) 1, (byte) 1};
            this.deviceClient.write(0x54, commands);
        } else {
            byte[] commands = {(byte) 0xae, (byte) 0xc1, (byte) 0x16, (byte) 0x02, (byte) 0, (byte) 0};
            this.deviceClient.write(0x54, commands);
        }
    }

    //Debugged byte transition
    public void test() {
        byte[] commands = {(byte) 0xae, (byte) 0xc1, (byte) Register.REQUEST_BLOCKS.val, (byte) 0x02, (byte) 0xff, (byte) 0xff};
        this.deviceClient.write(0x54, commands);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        byte[] data = this.deviceClient.read(0x54, 19);

        for (int n = 0; n < 19; n++) {
            AbstractOpMode.getTelemetry().addDataPhone("First Data:" + data[n]);
        }

        data = this.deviceClient.read(0x54, 13);

        for (int n = 0; n < 13; n++) {
            AbstractOpMode.getTelemetry().addDataPhone("Second Data:" + data[n]);
        }

        data = this.deviceClient.read(0x54, 13);

        for (int n = 0; n < 13; n++) {
            AbstractOpMode.getTelemetry().addDataPhone("Third Data:" + data[n]);
        }

        data = this.deviceClient.read(0x54, 13);

        for (int n = 0; n < 13; n++) {
            AbstractOpMode.getTelemetry().addDataPhone("Forth Data:" + data[n]);
        }

        data = this.deviceClient.read(0x54, 13);

        for (int n = 0; n < 13; n++) {
            AbstractOpMode.getTelemetry().addDataPhone("Fifth Data:" + data[n]);
        }
    }

    public Block[] getAllBlocks() {
        try {
            byte[] commands = {(byte) 0xae, (byte) 0xc1, (byte) Register.REQUEST_BLOCKS.val, (byte) 0x02, (byte) 0xff, (byte) 0xff};
            this.deviceClient.write(0x54, commands);
            AbstractOpMode.delay(100);
            byte[] start = this.deviceClient.read(0x54, 5), data;

            while (start[2] != (byte) Register.RESPONSE_BLOCKS.val) {
                this.deviceClient.write(0x54, commands);
                AbstractOpMode.delay(100);
                start = this.deviceClient.read(0x54, 5);
            }

            int numBlocks = start[3] / 14;

            Block[] blocks = new Block[numBlocks];

            for (int n = 0; n < numBlocks; n++) {
                blocks[n] = new Block(this.deviceClient.read(0x54, 13));
            }

            return blocks;
        } catch (NegativeArraySizeException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return new Block[0];
    }

    public Block[] getSignatureBlocks(int signature) {
        if (signature > 7 || signature < 1)
            throw new NullPointerException("No such pixycam block signature, acceptable values 1-7");
        Block[] allBlocks = getAllBlocks();
        int numBlocks = 0;
        for (Block block : allBlocks) {
            if (block.getSignature() == signature) {
                numBlocks++;
            }
        }
        Block[] blocks = new Block[numBlocks];
        numBlocks = 0;
        for (Block block : blocks) {
            if (block.getSignature() == signature) {
                blocks[numBlocks] = block;
                numBlocks++;
            }
        }
        return blocks;
    }

    public Block getLargestBlock() {
        Block[] blocks = getAllBlocks();

        if (blocks.length > 0) {

            int largest = 0;

            for (int n = 1; n < blocks.length; n++) {
                if (blocks[n].getX() * blocks[n].getY() > blocks[largest].getX() * blocks[largest].getY())
                    largest = n;
            }

            return blocks[largest];
        }
        return null;
    }

    public SamplePosition getSamplePosition() {
        Block block = getLargestBlock();

        if (block != null) {
            return SamplePosition.getPositionFromValue(block.getX());
        }

        return SamplePosition.UNKNOWN;
    }

    public enum Register {
        //Need to expand entries
        REQUEST_FIRMWARE_VERSION(0x0e),
        REQUEST_BLOCKS(0x20),

        RESPONSE_BLOCKS(0x21);

        public int val;

        Register(int val) {
            this.val = val;
        }
    }

    public class Block {

        private int x, y, width, height, signature;

        public Block(int signature, int x, int y, int width, int height) {
            this.signature = signature;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Block(byte[] bytes) {
            this.signature = littleEndian(bytes[0], bytes[1]);
            this.x = littleEndian(bytes[2], bytes[3]);
            this.y = littleEndian(bytes[4], bytes[5]);
            this.width = littleEndian(bytes[6], bytes[7]);
            this.height = littleEndian(bytes[8], bytes[9]);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getSignature() {
            return signature;
        }
    }

    public byte[] combineByteArrrays(byte[] array1, byte[] array2) {
        byte[] combined = new byte[array1.length + array2.length];
        for (int i = 0; i < combined.length; i++) {
            combined[i] = i < array1.length ? array1[i] : array2[i - array1.length];
        }
        return combined;
    }

    public int littleEndian(byte first, byte second) {
        int w = second;
        w <<= 8;
        w |= first;
        if (w < 0) {
            w = 128 + (128 - Math.abs(w));
        }
        return w;
    }
}