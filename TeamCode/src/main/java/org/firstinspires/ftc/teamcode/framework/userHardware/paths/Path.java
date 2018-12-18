package org.firstinspires.ftc.teamcode.framework.userHardware.paths;

import java.util.concurrent.ConcurrentHashMap;

public class Path {

    private ConcurrentHashMap<Integer, Segment> segments = new ConcurrentHashMap<>();
    private Segment currentSegment;

    private int numSegments = 0;

    private boolean isDone = false;

    private final String name;

    public Path(String name){
        this.name = name;
        currentSegment = segments.get(0);
    }

    public void addSegment(Segment segment) {
        segment.setNumber(numSegments);
        segments.put(numSegments, segment);
        numSegments++;
    }

    public Segment getNextSegment(){
        currentSegment.stop();
        if(currentSegment.getNumber()>=numSegments){
            isDone = true;
            return null;
        } else {
            currentSegment = segments.get(currentSegment.getNumber()+1);
            currentSegment.start();
            return currentSegment;
        }
    }

    public Segment getCurrentSegment(){
        return currentSegment;
    }

    public void pause(){
        currentSegment.pause();
    }

    public void resume(){
        currentSegment.resume();
    }

    public boolean isDone(){
        return isDone;
    }

    public String getName(){
        return name;
    }
}
