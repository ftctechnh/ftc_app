package org.firstinspires.ftc.teamcode.systems.logging;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.Telemetry.Item;

import java.util.HashMap;

public class PhoneLogger implements ILogger
{
    private Telemetry telemetry;
    private HashMap<String, Item> items;

    public PhoneLogger(Telemetry telemetry) {
        this.telemetry = telemetry;
        this.items = new HashMap<String, Item>();
    }

    public void log(String name, String data, Object... args) {
        getItem(name).setValue(name, StringFormatter.format(data, args));
    }

    public void write()
    {
        telemetry.update();
    }

    private Item getItem(String name) {
        if (items.containsValue(name))
            return items.get(name);
        else
        {
            Item item = telemetry.addData(name, "");
            items.put(name, item);
            return item;
        }
    }
}
