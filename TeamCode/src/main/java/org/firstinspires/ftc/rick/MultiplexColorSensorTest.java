package org.firstinspires.ftc.rick;

        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Chris D on 10/5/2016
 *
 * In this example, you need to create a device configuration that lists two
 * "I2C Device"s, one named "mux" and the other named "ada". There are two
 * Adafruit color sensors plugged into the I2C multiplexer on ports 0 and 3.
 */
@TeleOp(name="MultiplexColorSensorTest", group="Iterative Opmode")
@Disabled
public class MultiplexColorSensorTest extends OpMode
{
    MultiplexColorSensor muxColor;
    int[] ports = {0,1,2,3};

    @Override
    public void init() {
        int milliSeconds = 48;
        muxColor = new MultiplexColorSensor(hardwareMap, "mux", "ada",
                ports, milliSeconds,
                MultiplexColorSensor.GAIN_16X);
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        muxColor.startPolling();
    }

    @Override
    public void loop() {
        for (int i=0; i<ports.length; i++) {
            int[] crgb = muxColor.getCRGB(ports[i]);

            telemetry.addLine("Sensor " + ports[i]);
            telemetry.addData("CRGB", "%5d %5d %5d %5d",
                    crgb[0], crgb[1], crgb[2], crgb[3]);
        }

        telemetry.update();
    }

    @Override
    public void stop() {

    }
}