package edu.usrobotics.opmode.mecanumbot;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Max on 10/20/2017.
 */

public class ImageIdentifierWorker implements Runnable {

    private volatile boolean isAlive = true;
    public boolean stopped = false;
    public Map<VuforiaTrackable, Integer> visibilityCount;
    List<VuforiaTrackable> allTrackables;

    public ImageIdentifierWorker (List<VuforiaTrackable> allTrackables) {
        this.allTrackables = allTrackables;

        visibilityCount = new HashMap<>();
        for (VuforiaTrackable trackable : allTrackables) {
            visibilityCount.put(trackable, 0);
        }
    }

    public void stop () {
        isAlive = false;
    }

    @Override
    public void run() {
        while (isAlive) {
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    visibilityCount.put(trackable, visibilityCount.get(trackable) + 1);
                }
            }
        }

        stopped = true;
    }
}
