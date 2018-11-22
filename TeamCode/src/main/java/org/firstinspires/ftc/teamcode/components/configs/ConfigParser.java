package org.firstinspires.ftc.teamcode.components.configs;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by evancoulson on 9/23/17.
 */

public class ConfigParser implements IConfig
{
    private File file;
    private File externalRoot;
    private File root;
    private String fileName;

    private Map<String, String[]> configData;

    public ConfigParser(String filename)
    {
        this.fileName = filename;
        configData = new HashMap<String, String[]>();
        externalRoot = Environment.getExternalStorageDirectory();
        root = new File(externalRoot.getAbsolutePath() + "/Android/data/com.overlake.ftc.configapp/files", "configurations");

        file = new File(root.getPath() + "/" + filename.split(".omc")[0] + ".omc");
        FileInputStream fis;
        try
        {
            fis = new FileInputStream(file);
            Scanner input = new Scanner(fis);
            while (input.hasNextLine())
            {
                String[] args = input.nextLine().split(" ");
                args[0] = args[0].substring(1, args[0].length() - 1).trim();
                args[1] = args[1].replace(':', ' ').trim();
                args[2] = args[2].trim();
                configData.put(args[1], args);
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Missing config file " + filename);
        }
    }

    public int getInt(String key)
    {
        return Integer.parseInt(getValue(key, "int"));
    }

    public float getFloat(String key)
    {
        return Float.parseFloat(getValue(key, "float"));
    }

    public double getDouble(String key)
    {
        return Double.parseDouble(getValue(key, "double"));
    }

    public double getChar(String key)
    {
        return getValue(key, "char").charAt(0);
    }

    public byte getByte(String key)
    {
        return Byte.parseByte(getValue(key, "byte"));
    }

    public long getLong(String key)
    {
        return Long.parseLong(getValue(key, "long"));
    }

    public short getShort(String key)
    {
        return Short.parseShort(getValue(key, "short"));
    }

    public boolean getBoolean(String key)
    {
        String value = getValue(key, "boolean");
        if (value.equals("0"))
        {
            return false;
        }
        if (value.equals("1"))
        {
            return true;
        }
        if (value.toLowerCase().equals("true"))
        {
            return true;
        }
        if (value.toLowerCase().equals("false"))
        {
            return false;
        }
        throw new IllegalStateException();
    }

    public String getString(String key)
    {
        return getValue(key, "String");
    }

    public String getValue(String key, String type)
    {
        if (configData.containsKey(key))
        {
            if (configData.get(key)[0].equals(type))
            {
                return configData.get(key)[2];
            }
            else
            {
                throw new IllegalStateException("Trying to get key with wrong type of: " + type + ", should be: " + configData.get(key)[0]);
            }
        }
        else
        {
            throw new IllegalArgumentException("Can not find key: " + key + " in config: " + this.fileName);
        }
    }

    public void updateKey(String key, String newVal)
    {
        FileOutputStream fos;
        try
        {
            StringBuilder sb = new StringBuilder();
            fos = new FileOutputStream(file);
            for (String dataKey : configData.keySet())
            {
                String[] args = configData.get(dataKey);
                if (args[1].equals(key))
                {
                    args[2] = newVal;
                }
                sb.append("[" + args[0] + "] " + args[1] + ": " + args[2] + "\n");
            }
            String str = sb.toString();
            fos.write(str.getBytes());
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Error opening config");
        }
    }
}