package edu.usrobotics.opmode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.usrobotics.opmode.tracker.Tracker;

/**
 * Created by Max on 9/14/2016.
 */
public class TrackedOp extends StateBasedOp {

    ArrayList<Tracker> trackers = new ArrayList<>();

    OpenGLMatrix robotTransform;
    OpenGLMatrix lastRobotTransform;

    float maxPositionError_mm = 40f; // Maximum position deviation from reliable tracker acceptable in millimeters.
    float maxRotationError_deg = 5f; // Maximum rotation deviation from reliable tracker acceptable in degrees.


    public Tracker addTracker (Tracker t) {
        trackers.add(t);

        // Sort trackers by reliability, most reliable to least reliable
        Collections.sort(trackers, new Comparator<Tracker>() {
            @Override
            public int compare(Tracker t1, Tracker t2) {
                return Float.compare(t1.getReliability(), t2.getReliability());
            }
        });

        return t;
    }

    @Override public void loop ()
    {
        super.loop();

        OpenGLMatrix position = null;
        OpenGLMatrix rotation = null;

        for (Tracker tracker : trackers) {
            if (tracker.track()) { // If tracked successfully (updated transforms)

                OpenGLMatrix p = tracker.getRobotPosition();
                if (p != null) {
                    if (position != null) {
                        // Get difference / "distance" between reliable position and more precise position
                        OpenGLMatrix difference = position.inverted().multiplied(p);

                        // If precise position is near reliable position, we use precise position.
                        if (difference.getTranslation().magnitude() < maxPositionError_mm) {
                            position = p;
                        }

                    } else {
                        position = p; // If this is the most reliable position, use it.
                    }
                }

                OpenGLMatrix r = tracker.getRobotOrientation();
                if (r != null) {
                    if (rotation != null) {
                        // Get difference / "distance" between reliable rotation and more precise rotation
                        OpenGLMatrix difference = r.inverted().multiplied(rotation);
                        // Get negative translation of matrix
                        VectorF negativeTranslation = difference.getTranslation().multiplied(-1f);
                        // Add negative translation to matrix to zero the translation (and only have rotation data)
                        difference = difference.translated(negativeTranslation.get(0), negativeTranslation.get(1), negativeTranslation.get(2));

                        // If precise rotation is near reliable rotation, we use precise rotation.
                        if (difference.getTranslation().magnitude() < maxRotationError_deg) {//TODO
                            rotation = r;
                        }

                    } else {
                        rotation = r; // If this is the most reliable rotation, use it.
                    }
                }

            }
        }
    }
}
