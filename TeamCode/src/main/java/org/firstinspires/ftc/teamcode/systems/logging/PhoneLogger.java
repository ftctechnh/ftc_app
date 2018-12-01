package org.firstinspires.ftc.teamcode.systems.logging;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry.Item;

import java.util.HashMap;

/**
 * A logger that build on top of the telemetry provided by the ftc core library
 */
public class PhoneLogger implements ILogger
{
    private Telemetry telemetry;

    /**
     * Creates a logger to be used on the phone
     * @param telemetry telemetry provided by the opmode
     */
    public PhoneLogger(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    /**
     * Logs to telemetry
     * @param name name of the key that is being logged to in telemetry
     * @param data raw string to be formatted
     * @param args arguments to be put in the formatted string
     */
    public void log(String name, Object data, Object... args) {
        telemetry.addData(name, StringFormatter.format(data.toString(), args));
    }

    /**
     * Updates the telemetry
     */
    public void write() {
        telemetry.update();
    }
}
