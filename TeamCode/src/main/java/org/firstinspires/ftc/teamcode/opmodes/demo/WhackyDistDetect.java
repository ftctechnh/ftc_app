package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.WhackyDistLib;
import org.firstinspires.ftc.teamcode.libraries.interfaces.LocationSensor;

/**
 * Created by Noah on 12/28/2017.
 */

@Autonomous(name="Stick Distance Measuring", group="test")
public class WhackyDistDetect extends OpMode {
    private static final boolean red = true;
    BotHardware bot = new BotHardware(this);
    AutoLib.Sequence mSeq = new AutoLib.LinearSequence();
    APDS9960 dist;
    APDS9960.Config config = new APDS9960.Config();

    VectorF stupid = new VectorF(0,0);

    public void init() {
        bot.init();

        config.setPulse(APDS9960.Config.PulseLength.PULSE_16US, (byte)8, APDS9960.Config.LEDStrength.STREN_100MA, APDS9960.Config.LEDBoost.BOOST_1X, APDS9960.Config.DistGain.GAIN_2X);
        dist = new APDS9960(config, hardwareMap.get(I2cDeviceSynch.class, "dist"), true, APDS9960.Config.DistGain.GAIN_2X);
        dist.initDevice();

        final WhackyDist step = new WhackyDist(this, bot.getStick(), dist, -1, 90, red);
        mSeq.add(step);
        mSeq.add(new AutoLib.RunCodeStep(new Runnable() {
            @Override
            public void run() {
                stupid = step.getLocation();
            }
        }));
    }

    public void start() {
        dist.startDevice();
    }

    public void loop() {
        if(mSeq.loop()) telemetry.addData("Location", stupid.toString());
    }

    public static class WhackyDist extends AutoLib.Step implements LocationSensor {
        //constants
        private static final double DIST_START = 140;
        private static final double SLOPE_MAX = 100;
        //members
        private final OpMode mode;
        private final APDS9960 sensor;
        private final Servo stick;
        private final double inc;
        private final double start;
        private final boolean red;

        //storage
        private double lastTime = 0;
        private double lastDist;
        private double currentAngle = 90;

        //to be referenced by the step that gets the location from this step
        private VectorF location;

        private final FilterLib.MovingWindowFilter filter = new FilterLib.MovingWindowFilter(5, 600);

        WhackyDist(OpMode mode, Servo stick, APDS9960 measure, double increment, double start, boolean red) {
            this.mode = mode;
            this.stick = stick;
            this.sensor = measure;
            this.inc = increment;
            this.currentAngle = start;
            this.start = start;
            this.red = red;
        }

        public boolean loop() {
            filter.appendValue(sensor.getLinearizedDistance(red));
            final double dist = filter.currentValue();
            if(firstLoopCall()) {
                lastDist = dist;
            }

            final double time = mode.getRuntime();
            final double slope = Math.abs(dist - lastDist) / (time - lastTime);
            lastDist = dist;
            lastTime = time;

            //check if done

            if(dist <= DIST_START && slope <= SLOPE_MAX) {
                //store location from the whacky sticks position
                location = new VectorF((float)dist, (float)WhackyDistLib.getWhackyDistance(currentAngle));
                //reset servo
                stick.setPosition(WhackyDistLib.getWhackyPosFromDegrees(start));
                return true;
            }

            //increment servo
            currentAngle += inc;
            stick.setPosition(WhackyDistLib.getWhackyPosFromDegrees(currentAngle));

            mode.telemetry.addData("angle", currentAngle);
            mode.telemetry.addData("position", stick.getPosition());

            return false;
        }

        public VectorF getLocation() {
            return location;
        }
    }
 }
