package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.libraries.APDS9960;
import org.firstinspires.ftc.teamcode.libraries.CrappyGraphLib;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Noah on 12/21/2017.
 */

@Autonomous(name = "APDS Linearization", group = "test")
public class APDSDistance extends CrappyGraphLib {
    APDS9960 dist;
    APDS9960.Config config = new APDS9960.Config();

    private static final int RAISE_GAIN_THRESH = 100;
    private static final int LOWER_GAIN_THRESH = 200;

    private int regDist;
    private double linearDist;

    private LinkedList<Double> data = new LinkedList<>();

    public void init() {
        initGraph(data, true);

        config.setPulse(APDS9960.Config.PulseLength.PULSE_16US, (byte)8, APDS9960.Config.LEDStrength.STREN_100MA, APDS9960.Config.LEDBoost.BOOST_1X, APDS9960.Config.DistGain.GAIN_8X);
        dist = new APDS9960(config, hardwareMap.get(I2cDeviceSynch.class, "dist"), true);
        dist.initDevice();
        dist.startDevice();

        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                APDS9960.Config.DistGain mahGain = config.gain;
                regDist = dist.getDist();
                linearDist = dist.getLinearizedDistance(regDist, mahGain);
            }
        });

        telemetry.addLine().addData("Unfixed Distance", new Func<Integer>() {
            @Override
            public Integer value() {
                return regDist;
            }
        });
        telemetry.addLine().addData("Linear Distance", new Func<Double>() {
            @Override
            public Double value() {
                return linearDist;
            }
        });
    }

    public void start() {
        telemetry.clearAll();
    }

    public void loop() {
        linearDist = dist.getLinearizedDistance();

        //add graph stuff
        this.data.add(linearDist);
        if(this.data.size() > 100) this.data.remove();

        drawGraph();

        telemetry.addData("Gain", config.gain.toString());
        telemetry.addData("Regular", regDist);
        telemetry.addData("Linear", linearDist);
    }
}
