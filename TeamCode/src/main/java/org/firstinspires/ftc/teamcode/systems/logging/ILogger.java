package org.firstinspires.ftc.teamcode.systems.logging;

public interface ILogger
{
    void log(String name, Object data, Object... args);
    void write();
}
