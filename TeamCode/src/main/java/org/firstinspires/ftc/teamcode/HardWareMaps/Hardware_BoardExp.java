package org.firstinspires.ftc.teamcode.HardWareMaps;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hardware_BoardExp {
    public DeviceInterfaceModule interfaceModule = null;

    public void init(HardwareMap hardwareMap){
        interfaceModule = hardwareMap.deviceInterfaceModule.get("Board");

        interfaceModule.setDigitalChannelMode(0, DigitalChannel.Mode.OUTPUT);
        interfaceModule.setDigitalChannelMode(1, DigitalChannel.Mode.OUTPUT);
        interfaceModule.setDigitalChannelMode(2, DigitalChannel.Mode.OUTPUT);
        interfaceModule.setDigitalChannelMode(3, DigitalChannel.Mode.INPUT);
        interfaceModule.setDigitalChannelMode(4, DigitalChannel.Mode.INPUT);
        //  reset All Digital Pins
        int index = 0;
        do {
            interfaceModule.setDigitalChannelState(index, false);
        }while (index ++ < 7);
        //  reset All LEDs
        interfaceModule.setLED(0,false);
        interfaceModule.setLED(1,false);
    }

}
