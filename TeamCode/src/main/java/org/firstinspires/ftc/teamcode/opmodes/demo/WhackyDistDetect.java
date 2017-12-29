package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.libraries.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.WhackyDistLib;

/**
 * Created by Noah on 12/28/2017.
 */

@Autonomous(name="Stick Distance Measuring", group="test")
public class WhackyDistDetect extends OpMode {
    BotHardware bot = new BotHardware(this);

    public void init() {
        bot.init();
    }

    public void start() {

    }

    public void loop() {

    }

    public static class WhackyDist extends AutoLib.Step {
        //constants
        private static final double DIST_START = 140;
        private static final double SLOPE_MAX = 100;
        private static final double FOUND_COUNT = 10;
        //members
        private final OpMode mode;
        private final APDS9960 sensor;
        private final Servo stick;
        private final double inc;
        private final double start;

        //storage
        private double lastTime = 0;
        private double lastDist;
        private double currentAngle = 90;
        private int foundCount = 0;

        private final FilterLib.MovingWindowFilter filter = new FilterLib.MovingWindowFilter(5);


        WhackyDist(OpMode mode, Servo stick, APDS9960 measure, double increment, double start) {
            this.mode = mode;
            this.stick = stick;
            this.sensor = measure;
            this.inc = increment;
            this.currentAngle = start;
            this.start = start;
        }

        public boolean loop() {
            filter.appendValue(sensor.getLinearizedDistance());
            final double dist = filter.currentValue();
            if(firstLoopCall()) {
                lastDist = dist;
            }

            final double time = mode.getRuntime();
            final double slope = Math.abs(dist - lastDist) / (time - lastTime);
            lastDist = dist;
            lastTime = time;

            //check if done

            if(dist >= DIST_START && slope <= SLOPE_MAX && ++foundCount >= FOUND_COUNT) {
                //reset servo
                stick.setPosition(WhackyDistLib.getWhackyPosFromDegrees(start));
                return true;
            }
            else foundCount = 0;

            //increment servo
            currentAngle += inc;
            stick.setPosition(WhackyDistLib.getWhackyPosFromDegrees(currentAngle));

            return false;
        }
    }

 }
