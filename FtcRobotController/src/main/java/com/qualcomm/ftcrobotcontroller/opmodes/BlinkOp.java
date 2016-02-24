/**
 * Created by mhaeberli on 11/5/15.
 */


/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
        import com.qualcomm.robotcore.hardware.DigitalChannelController;
        import com.qualcomm.robotcore.hardware.I2cDevice;
        import com.qualcomm.robotcore.util.ElapsedTime;

        import java.text.SimpleDateFormat;
        import java.util.Date;

/**
 * BlinkOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class BlinkOp extends OpMode {

    private String startDate;
    private ElapsedTime runtime = new ElapsedTime();

    DeviceInterfaceModule dim1;
    I2cDevice blink;

    @Override
    public void init() {
        hardwareMap.logDevices();

        // get a reference to our DeviceInterfaceModule object.
        dim1 = hardwareMap.deviceInterfaceModule.get("dim1");

        blink = hardwareMap.i2cDevice.get("blink");
        //cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

    }

    /*
       * Code to run when the op mode is first enabled goes here
       * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
       */
    @Override
    public void init_loop() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        runtime.reset();
        telemetry.addData("Blink Op Init Loop", runtime.toString());
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        telemetry.addData("1 Start", "BlinkOp started at " + startDate);
        telemetry.addData("2 Status", "running for " + runtime.toString());

        double t = runtime.time();
        int iT = (int) t;

        int whichColor = (iT % 4);
        switch (whichColor) {
            case 0:
                setBlinkColor(0xF0, 0x20, 0x20);
                break;
            case 1:
                setBlinkColor(0x20, 0xF0, 0x20);
                break;
            case 2:
                setBlinkColor(0x20, 0x20, 0xF0);
                break;
            case 3:
                setBlinkColor(0x20, 0x20, 0x20);
                break;
        }
    }

    void setBlinkColor(int red, int green, int blue) {
        byte[] writeData = {(byte)red, (byte)green, (byte)blue};
        //0x09 is the 7-bit I2C address, you have to multiply by 2 to get the 8-bit one
        blink.enableI2cWriteMode(0x09 * 2, (byte)'c', writeData.length);
        blink.copyBufferIntoWriteBuffer(writeData);
        blink.setI2cPortActionFlag();
        blink.writeI2cCacheToController();
    }
}
