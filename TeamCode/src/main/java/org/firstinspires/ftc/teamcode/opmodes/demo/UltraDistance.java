package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.libraries.CrappyGraphLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;

import java.util.LinkedList;

/**
 * Created by Noah on 3/19/2018.
 */

@TeleOp(name="Graph Ultra")
public class UltraDistance extends CrappyGraphLib {
    private LinkedList<Double> data = new LinkedList<>();
    private MatbotixUltra frontUltra;

    public void init() {
        initGraph(data, true);
        frontUltra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultrafront"), 100);
        frontUltra.initDevice();
    }

    public void start() {
        frontUltra.startDevice();
    }

    public void loop() {
        //add graph stuff
        int val = frontUltra.getReading();
        this.data.add((double)val);
        if(this.data.size() > 100) this.data.remove();
        drawGraph();

        telemetry.addData("Ultra", val);
    }
}
