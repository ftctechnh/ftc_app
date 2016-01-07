package org.overlake.ftc.team_7330.Testing;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.Overlake.ftc.Team_7330.Testing.HueCalibration;
import org.Overlake.ftc.Team_7330.Testing.HueCalibrationData;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

@TeleOp(name="ColorCalibration")
public class ColorCalibration extends SynchronousOpMode
{
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;
    static final int LED_CHANNEL = 5;

    boolean redRecorded = false;
    boolean blueRecorded = false;
    boolean whiteRecorded = false;
    boolean matRecorded = false;

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

        HueCalibrationData[] data = new HueCalibrationData[1];

        for (int i = 0; i < data.length; i++)
        {
            getData(data[i].grayTile);
            
            for (int j = 0; j < 10; j++)
            {
                data[i].grayTile.AddSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()););
            }

            for (int j = 0; j < 10; j++)
            {
                data[i].redTape.AddSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()););
            }

            for (int j = 0; j < 10; j++)
            {
                data[i].blueTape.AddSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()););
            }

            for (int j = 0; j < 10; j++)
            {
                data[i].whiteTape.AddSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()));
            }

            for (int j = 0; j < 10; j++)
            {
                data[i].redBeacon.AddSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()));
            }

            for (int j = 0; j < 10; j++)
            {
                data[i].blueBeacon.AddSample(convertColor(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue()));
            }
        }

        HueCalibrationData.toFile("HueCalibrationData.json", data);
    }

    public void waitForButton()
    {
        boolean wasA = gamepad1.a;
        while(!(gamepad1.a && !wasA))
        {
            wasA = gamepad1.a;
            try
            {
                wait(100);
            }
            catch (Exception e)
            {
            }
        }
    }

    public double convertColor(int r, int g, int b)
    {
        double y = Math.sqrt(3) * (g - b);
        double x = 2 * r - g - b;
        return Math.atan2(y, x) * (360.0 / (2 * Math.PI));
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
                telemetry.item("Red: ", new IFunc<Object>()
                {
                    public Object value()
                    {
                        return "R: " + redRecorded +
                                ", B: " + blueRecorded +
                                ", W: " + whiteRecorded +
                                ", M: " + matRecorded;
                    }
                })
        );
    }
}
