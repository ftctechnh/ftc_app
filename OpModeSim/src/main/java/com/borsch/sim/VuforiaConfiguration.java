package com.borsch.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VuforiaConfiguration {
    private final Map<String, List<SimulatedVuforiaTrackable>> assets = new HashMap<>();

    public void addAssets (String name, SimulatedVuforiaTrackable ... trackables) {
        if (!assets.containsKey(name)) {
            assets.put(name, new ArrayList<>());
        }

        for (SimulatedVuforiaTrackable trackable : trackables) {
            assets.get(name).add(trackable);
        }
    }

    public void addAsset (String name, SimulatedVuforiaTrackable trackable) {
        addAssets(name, trackable);
    }

    public List<SimulatedVuforiaTrackable> getAsset(String s) {
        return assets.get(s);
    }
}
