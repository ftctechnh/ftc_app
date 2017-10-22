package org.firstinspires.ftc.robotcore.internal.vuforia;

import com.borsch.sim.SimulatedVuforiaLocalizer;
import com.borsch.sim.SimulatedVuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.AbstractList;
import java.util.List;

public class SimulatedVuforiaTrackables extends AbstractList<VuforiaTrackable> implements VuforiaTrackables {
    private final SimulatedVuforiaLocalizer localizer;
    private String name = "Simulated Vuforia Trackables";
    private List<SimulatedVuforiaTrackable> trackables;

    public SimulatedVuforiaTrackables(SimulatedVuforiaLocalizer vuforiaLocalizer, List<SimulatedVuforiaTrackable> trackables) {
        this.localizer = vuforiaLocalizer;
        this.trackables = trackables;
    }

    @Override
    public VuforiaTrackable get(int index) {
        return trackables.get(index);
    }

    @Override
    public int size() {
        return trackables.size();
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public VuforiaLocalizer getLocalizer() {
        return localizer;
    }
}
