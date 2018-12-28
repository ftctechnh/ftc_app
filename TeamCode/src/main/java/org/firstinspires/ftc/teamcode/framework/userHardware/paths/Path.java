package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractAutonNew;

import java.util.concurrent.ConcurrentHashMap;

public class Path {

    private ConcurrentHashMap<Integer, Segment> segments = new ConcurrentHashMap<>();
    private Segment currentSegment = null;

    private int numSegments = 0;

    private boolean isDone = false;

    private final String name;

    public Path(String name) {
        this.name = name;
    }

    public void reset() {
        currentSegment = null;
        isDone = false;
    }

    public void addSegment(Segment segment) {
        segment.setNumber(numSegments);
        segments.put(numSegments, segment);
        numSegments++;
    }

    public Segment getNextSegment() {
        if (currentSegment == null) {
            currentSegment = segments.get(0);
            currentSegment.start();
            return currentSegment;
        } else {
            currentSegment.stop();
        }

        AbstractAutonNew.addFinishedState(currentSegment.getName());

        if (currentSegment.getNumber() >= segments.size() - 1) {
            isDone = true;
            return null;
        }

        currentSegment = segments.get(currentSegment.getNumber() + 1);

        currentSegment.start();
        return currentSegment;
    }

    public Segment getCurrentSegment() {
        if (currentSegment == null) return segments.get(0);
        return currentSegment;
    }

    public void nextSegment() {
        currentSegment.stop();
    }

    public void pause() {
        currentSegment.pause();
    }

    public void resume() {
        currentSegment.resume();
    }

    public boolean isDone() {
        return isDone;
    }

    public String getName() {
        return name;
    }
}
