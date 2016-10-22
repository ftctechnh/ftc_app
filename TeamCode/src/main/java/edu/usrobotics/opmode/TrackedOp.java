package edu.usrobotics.opmode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

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
    Orientation robotOrientation;

    float maxPositionError_mm = 40f; // Maximum acceptable translation deviation from reliable tracker in millimeters.
    float maxRotationError_deg = 5f; // Maximum acceptable rotation deviation from reliable tracke in degrees.

    float inch_mm        = 25.4f;
    float RobotWidth_mm       = 18 * inch_mm;
    float FTCFieldWidth_mm  = (12*12 - 2) * inch_mm;   // the FTC field is ~11'10" center-to-center of the glass panels



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

    public void updateTrackers () {
        OpenGLMatrix position = null;
        Orientation rotation = null;

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

                Orientation r = tracker.getRobotOrientation();
                if (r != null) {
                    if (rotation != null) {
                        VectorF normal = new VectorF(r.firstAngle, r.secondAngle, r.thirdAngle);
                        //Orientation.getOrientation(rotation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                        VectorF normal2 = new VectorF(rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);

                        // If precise rotation is near reliable rotation, we use precise rotation.
                        if ((normal.subtracted(normal2).magnitude()) < maxRotationError_deg) {
                            rotation = r;
                        }

                    } else {
                        rotation = r; // If this is the most reliable rotation, use it.
                    }
                }

            }
        }

        if (position != null)
            lastRobotTransform = robotTransform;

        if (rotation != null) {
            robotTransform = position.multiplied(rotation.getRotationMatrix());
            robotOrientation = rotation;
        }

    }

    public OpenGLMatrix getRobotTransform() {
        return robotTransform;
    }

    public Orientation getRobotOrientation() {
        return robotOrientation;
    }

    @Override public void loop ()
    {
        super.loop();

        updateTrackers();
    }
}
