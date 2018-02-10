package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;

/**
 * Created by Noah on 1/29/2018.
 */

@Autonomous(name="SPI Bitbang")
@Disabled
public class SPIBitbang extends OpMode {
    DigitalChannel SCK;
    DigitalChannel CS;
    DigitalChannel MISO;
    DigitalChannel MOSI;

    AnalogInput input;

    private enum SPIState {
        CLOCK_LOW,
        DATA_SET,
        CLOCK_HIGH;
    }

    private final byte frame = (byte)(0b00000000);
    byte recv = 0;
    int index = 0;
    SPIState state = SPIState.CLOCK_HIGH;

    public void init() {
        SCK = hardwareMap.digitalChannel.get("sck");
        CS = hardwareMap.digitalChannel.get("cs");
        MISO = hardwareMap.digitalChannel.get("miso");
        MOSI = hardwareMap.digitalChannel.get("mosi");

        SCK.setMode(DigitalChannel.Mode.OUTPUT);
        CS.setMode(DigitalChannel.Mode.OUTPUT);
        MISO.setMode(DigitalChannel.Mode.INPUT);
        MOSI.setMode(DigitalChannel.Mode.OUTPUT);

        SCK.setState(true);
        CS.setState(true);
        //MISO.setState(true);
        MOSI.setState(true);
    }

    public void start() {
        //Select
        CS.setState(false);
        //set write
        for(int i = 0; i < 8; i++) {
            MOSI.setState((frame & (1 << (7-i))) > 0);
            SCK.setState(false);
            SCK.setState(true);
        }
        //recieve read
        for(int i = 0; i < 8; i++) {
            SCK.setState(false);
            recv |= ((MISO.getState() ? 1 : 0) << (7-i));
            SCK.setState(true);
        }
        CS.setState(true);

    }

    public void loop() {
        /*
        if(index < 8) {
            if(state == SPIState.CLOCK_HIGH) SCK.setState(false);
            else if(state == SPIState.CLOCK_LOW) {

            }
        }
        */
        telemetry.addData("recv", recv & 0xff);
    }

    public void stop() {

    }
}
