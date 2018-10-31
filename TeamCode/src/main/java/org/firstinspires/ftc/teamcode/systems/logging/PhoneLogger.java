package org.firstinspires.ftc.teamcode.systems.logging;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry.Item;

import java.util.HashMap;

public class PhoneLogger implements ILogger
{
    private Telemetry telemetry;

    public PhoneLogger(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void log(String name, Object data, Object... args) {
        telemetry.addData(name, StringFormatter.format(data.toString(), args));
    }

    public void write() {
        telemetry.update();
    }
}
