package org.overlake.ftc.team_7330.Testing;

import android.widget.Button;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.overlake.ftc.team_7330.Testing.HueData;
import org.overlake.ftc.team_7330.Testing.ColorSensorData;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.util.EmptyStackException;

@TeleOp(name="ColorCalibration")
public class ColorCalibration extends SynchronousOpMode
{
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;
    static final int LED_CHANNEL = 5;

    // TODO: Implement this program for multiple sensors with an array of sensors
    ColorSensorData[] data = new ColorSensorData[1];
    String message = "Program starting (next: grayTile)";

    @Override
    public void main() throws InterruptedException
    {
        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        sensorRGB = hardwareMap.colorSensor.get("color");
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);
        cdim.setDigitalChannelState(LED_CHANNEL, true);

        composeDashboard();
        telemetry.update();

        for (int i = 0; i < data.length; i++)
        {
            data[i] = new ColorSensorData();
            getData(data[i].grayTile, "Gray tile calibrated (next: redTape)");
            getData(data[i].redTape, "Red tape calibrated (next: blueTape)");
            getData(data[i].blueTape, "Blue tape calibrated (next: whiteTape)");
            getData(data[i].whiteTape, "White tape calibrated (next: redBeacon)");
            getData(data[i].redBeacon, "Red beacon calibrated (next: blueBeacon)");
            getData(data[i].blueBeacon, "Blue beacon calibrated (sending to file)");
        }

        ColorSensorData.toFile("/sdcard/FIRST/colorSensorData.txt", data);
        message = "Sent to file!";
        telemetry.update();
    }

    public void getData(HueData hue, String message)
    {
        boolean a = false;
        while (!a)
        {
            updateGamepads();
            a = gamepad1.a;
        }

        boolean b = false;
        while (!b)
        {
            updateGamepads();
            b = gamepad1.b;
        }

        for(int i = 0; i < 10; i++) {
            hue.addSample(hueFromRGB(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()));
            waitMs(200);
        }

        this.message = message;
        telemetry.update();
    }

    public static double hueFromRGB(int r, int g, int b)
    {
        double y = Math.sqrt(3) * (g - b);
        double x = 2 * r - g - b;
        return Math.atan2(y, x) * (360.0 / (2 * Math.PI));
    }

    public void waitMs(int ms)
    {
        try
        {
            wait(ms);
        }
        catch (Exception e)
        {
        }
    }

    void composeDashboard()
    {
        telemetry.setUpdateIntervalMs(200);

        telemetry.addLine(
                telemetry.item("Red: ", new IFunc<Object>() {
                    public Object value() {
                        return sensorRGB.red();
                    }
                }),
                telemetry.item("Green: ", new IFunc<Object>() {
                    public Object value() {
                        return sensorRGB.green();
                    }
                }),
                telemetry.item("Blue: ", new IFunc<Object>() {
                    public Object value() {
                        return sensorRGB.blue();
                    }
                })
        );

        telemetry.addLine(
                telemetry.item("Message: ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return message;
                    }
                })
        );
    }
}

