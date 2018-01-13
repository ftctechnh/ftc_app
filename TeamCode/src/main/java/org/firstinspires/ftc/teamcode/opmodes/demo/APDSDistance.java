package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9930;
import org.firstinspires.ftc.teamcode.libraries.CrappyGraphLib;

import java.util.LinkedList;

/**
 * Created by Noah on 12/21/2017.
 */

@Autonomous(name = "APDS Linearization", group = "test")
public class APDSDistance extends CrappyGraphLib {
    APDS9930 dist;
    APDS9930 redDist;

    private int regDist;
    private double linearDist;
    private int redRegDist;
    private double redLinearDist;

    private LinkedList<Double> data = new LinkedList<>();

    public void init() {
        initGraph(data, true);

        dist = new APDS9930(hardwareMap.get(I2cDeviceSynch.class, "bluedist"), true, APDS9930.ProxGain.GAIN_1X, APDS9930.ProxGain.GAIN_8X);
        redDist = new APDS9930(hardwareMap.get(I2cDeviceSynch.class, "reddist"), true, APDS9930.ProxGain.GAIN_2X, APDS9930.ProxGain.GAIN_8X);
        dist.initDevice();
        redDist.initDevice();
        dist.startDevice();
        redDist.startDevice();

        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                APDS9930.ProxGain mahGain = dist.getPGAIN();
                regDist = dist.getDist();
                linearDist = dist.getLinearizedDistance(regDist, mahGain, false);
                mahGain = redDist.getPGAIN();
                redRegDist = redDist.getDist();
                redLinearDist = redDist.getLinearizedDistance(redRegDist, mahGain, false);
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

        telemetry.addLine().addData("Red Unfixed Distance", new Func<Integer>() {
            @Override
            public Integer value() {
                return redRegDist;
            }
        });
        telemetry.addLine().addData("Red Linear Distance", new Func<Double>() {
            @Override
            public Double value() {
                return redLinearDist;
            }
        });
    }

    public void start() {
        telemetry.clearAll();
    }

    public void loop() {
        APDS9930.ProxGain gain = dist.getPGAIN();
        int heh = dist.getDist();
        linearDist = dist.getLinearizedDistance(heh, gain, false);

        APDS9930.ProxGain gain2 = redDist.getPGAIN();
        int heh2 = redDist.getDist();
        double linearDist2 = redDist.getLinearizedDistance(heh2, gain2, false);

        //add graph stuff
        this.data.add(linearDist);
        if(this.data.size() > 100) this.data.remove();

        drawGraph();
        telemetry.addData("BLUE", "");
        telemetry.addData("Gain", dist.getPGAIN().toString());
        telemetry.addData("Regular", heh);
        telemetry.addData("Linear", linearDist);
        telemetry.addData("RED", "");
        telemetry.addData("Red Gain", redDist.getPGAIN().toString());
        telemetry.addData("Red Regular", heh2);
        telemetry.addData("Red Linear", linearDist2);
    }

    public void stop() {
        dist.stopDevice();
        redDist.stopDevice();
    }
}
