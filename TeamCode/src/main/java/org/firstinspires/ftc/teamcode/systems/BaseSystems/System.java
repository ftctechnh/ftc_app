package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config.ConfigParser;
import org.firstinspires.ftc.teamcode.systems.logging.PhoneLogger;

public abstract class System {

    String systemName;

    protected HardwareMap map;
    public ConfigParser config;
    public PhoneLogger telemetry;

    public System(OpMode opMode, String systemName) {
        this.systemName = systemName;
        this.map = opMode.hardwareMap;
        //this.config = new ConfigParser(systemName + ".omc");
        this.telemetry = new PhoneLogger(opMode.telemetry);
        try
        {
            this.config = new ConfigParser(systemName + ".omc");
        } catch (Exception e) {

        }
    }

    public void wait(int seconds) {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException("Failed to sleep thread", e);
        }
    }
}
