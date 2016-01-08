package org.overlake.ftc.team_7330.Testing;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.Overlake.ftc.Team_7330.Testing.HueData;
import org.Overlake.ftc.Team_7330.Testing.SensorData;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

@TeleOp(name="ColorCalibration")
public class ColorCalibration extends SynchronousOpMode
{
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;
    static final int LED_CHANNEL = 5;

    SensorData[] data = new SensorData[1];
    int dataIndex; // used in composeDashboard to overcome scope problems

    @Override
    public void main() throws InterruptedException
    {
        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        sensorRGB = hardwareMap.colorSensor.get("color");
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);
        cdim.setDigitalChannelState(LED_CHANNEL, true);

        composeDashboard();
        waitForStart();

        for (int i = 0; i < data.length; i++)
        {
            getData(data[i].grayTile);
            getData(data[i].redTape);
            getData(data[i].blueTape);
            getData(data[i].whiteTape);
            getData(data[i].redBeacon);
            getData(data[i].blueBeacon);
        }

        SensorData.toFile("SensorData.json", data);
    }

    public void getData(HueData hue)
    {
        boolean wasA = gamepad1.a;
        while(!(gamepad1.a && !wasA))
        {
            wasA = gamepad1.a;
            waitMs(100);
        }

        for(int i = 0; i < 10; i++)
        {
            hue.addSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()));
            waitMs(200);
        }

        telemetry.update();
    }

    public static double convertColor(int r, int g, int b)
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

        for (dataIndex = 0; dataIndex < data.length; dataIndex++)
        {
            telemetry.addLine(
                    telemetry.item(dataIndex + ": ", new IFunc<Object>()
                    {
                        public Object value()
                        {
                            return data[dataIndex].toString();
                        }
                    })
            );
        }
    }
}

