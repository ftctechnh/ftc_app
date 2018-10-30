package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config.ConfigParser;
import org.firstinspires.ftc.teamcode.systems.logging.PhoneLogger;

public abstract class System {

    protected String systemName;
    protected LinearOpMode opMode;
    protected HardwareMap hardwareMap;
    //public ConfigParser config;
    public PhoneLogger telemetry;

    public System(OpMode opMode, String systemName) {
        this.opMode = (LinearOpMode)opMode;
        this.systemName = systemName;
        this.hardwareMap = opMode.hardwareMap;
        //this.config = new ConfigParser(systemName + ".omc");
        this.telemetry = new PhoneLogger(opMode.telemetry);
        try
        {
            //this.config = new ConfigParser(systemName + ".omc");
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
