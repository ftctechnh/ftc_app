package org.firstinspires.ftc.teamcode.components.pixy;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.*;

@I2cDeviceType()
@DeviceProperties(name = "PixyCam", description = "PixyCam", xmlTag = "PixyCam")
public class PixyCam extends I2cDeviceSynchDevice<I2cDeviceSynch>
{
    private static final int BLOCK_SIZE = 14;
    public int blockCount;

    public PixyCam(I2cDeviceSynch deviceClient){
        super(deviceClient, true);
        this.deviceClient.setI2cAddress(I2cAddr.create7bit(0x54));
        this.setReadWindow();
        this.deviceClient.engage();
    }

    protected void setReadWindow()
    {
        // Sensor registers are read repeatedly and stored in a register. This method specifies the
        // registers and repeat read mode

        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(
                0,
                1,
                I2cDeviceSynch.ReadMode.REPEAT);
        this.deviceClient.setReadWindow(readWindow);
    }


    @Override
    protected boolean doInitialize() {
        return true;
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "PixyCam";
    }

    public void setBlockCount(int count){
        blockCount = count;
    }

    public ArrayList<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<>(blockCount);
        byte[] bytes = new byte[(blockCount+1)*BLOCK_SIZE+2];

        // read data from pixy
        bytes = this.getDeviceClient().read(0, bytes.length);

        // search for sync
        for (int i = 0; i < bytes.length - 2; i++){
            boolean sync1 = checkSync(bytes,i);
            boolean sync2 = checkSync(bytes,i+2);
            boolean startOfFrame = sync1 && sync2;

            if (startOfFrame){
                i += 2;
                for (int j = 0; j < blockCount; j++) {
                    byte[] tempBytes = new byte[BLOCK_SIZE];
                    for (int tempByteOffset = 0; tempByteOffset < BLOCK_SIZE; tempByteOffset++) {
                        if (i < 58) {
                            tempBytes[tempByteOffset] = bytes[i];
                            i++;
                        }
                    }
                    Block block = getBlock(tempBytes);
                    if (block != null) {
                        blocks.add(block);
                    }
                    if (blocks.size() == blockCount) {
                        return blocks;
                    }
                }
                return blocks;
            }
        }
        return blocks;
    }

    private boolean checkSync(byte[] bytes, int byteOffset){
        int b1 = bytes[byteOffset];
        if (b1 < 0){
            b1+=256;
        }
        int b2 = bytes[byteOffset+1];
        if (b2 < 0){
            b2+=256;
        }

        return b1 == 0x55 && b2 == 0xaa;
    }

    public Block getBlock(byte[] bytes){

        Block block = new Block();

        int checksum = convertBytesToInt(bytes[3], bytes[2]);
        // if the checksum is 0 or the checksum is a sync byte, then there
        // are no more blocks.
        if (checksum == 0 || checksum == 0xaa55)
        {
            // return an empty block
            return block;
        }
        block.signature = convertBytesToInt(bytes[5], bytes[4]);
        block.xCenter = convertBytesToInt(bytes[7], bytes[6]);
        block.yCenter = convertBytesToInt(bytes[9], bytes[8]);
        block.width = convertBytesToInt(bytes[11], bytes[10]);
        block.height = convertBytesToInt(bytes[13], bytes[12]);

        return block;
    }


    public class Block{
        public int signature;
        public int xCenter;
        public int yCenter;
        public int width;
        public int height;

        public String toString() {
            return "sig: " + signature + "__ x: " + xCenter + "__ y: " + yCenter +
                    "__ w: " + width + "__ h: " + height;
        }
    }

    public int convertBytesToInt(int msb, int lsb){
        if (msb < 0) {
            msb += 256;
        }

        int value = msb * 256;

        if (lsb < 0) {
            value += 256;
        }
        value += lsb;
        return value;
    }


}