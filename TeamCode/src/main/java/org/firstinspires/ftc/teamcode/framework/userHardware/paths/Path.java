package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

import java.util.ArrayList;

public class Path {

    private ArrayList<Segment> segments = new ArrayList<>();
    private Segment currentSegment;

    public void addSegment(Segment segment) {
        segments.add(segment);
    }
}
