package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.ConfigParser;

public abstract class System {

    String systemName;

    protected HardwareMap map;
    //public ConfigParser config;
    public Telemetry telemetry;

    public System(OpMode opMode, String systemName) {
        this.systemName = systemName;
        this.map = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;
        //this.config = new ConfigParser(systemName + ".omc");
    }
}
