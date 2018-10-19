package org.firstinspires.ftc.teamcode.systems.logging;

public interface ILogger
{
    void log(String name, String data, Object... args);
    void close();
}
