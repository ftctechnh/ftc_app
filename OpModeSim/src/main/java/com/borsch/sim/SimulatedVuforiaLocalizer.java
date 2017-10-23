package com.borsch.sim;

import com.borsch.OpModeSimulation;
import com.vuforia.CameraCalibration;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.vuforia.SimulatedVuforiaTrackables;

import java.util.concurrent.BlockingQueue;

public class SimulatedVuforiaLocalizer implements VuforiaLocalizer {

    private final VuforiaConfiguration configuration;
    private final Parameters parameters;

    public SimulatedVuforiaLocalizer(Parameters parameters) {
        this.parameters = parameters;
        this.configuration = OpModeSimulation.vuforiaConfiguration;
    }

    @Override
    public VuforiaTrackables loadTrackablesFromAsset(String s) {
        return new SimulatedVuforiaTrackables(this, configuration.getAsset(s));
    }

    @Override
    public VuforiaTrackables loadTrackablesFromFile(String s) {
        return null;
    }

    @Override
    public CameraCalibration getCameraCalibration() {
        return null;
    }

    @Override
    public BlockingQueue<CloseableFrame> getFrameQueue() {
        return null;
    }

    @Override
    public void setFrameQueueCapacity(int i) {

    }

    @Override
    public int getFrameQueueCapacity() {
        return 0;
    }
}
