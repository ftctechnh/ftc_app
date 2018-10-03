package org.firstinspires.ftc.teamcode.components.configs;

public interface IConfig
{
    int getInt(String key);

    float getFloat(String key);

    double getDouble(String key);

    double getChar(String key);

    byte getByte(String key);

    long getLong(String key);

    short getShort(String key);

    boolean getBoolean(String key);

    String getString(String key);
}
