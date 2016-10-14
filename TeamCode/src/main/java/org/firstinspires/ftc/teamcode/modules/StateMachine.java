package org.firstinspires.ftc.teamcode.modules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;

public class StateMachine {
    private final Object eventLock = new Object();
    private Map<String, Event> eventMap;
    private Queue<Event> eventQueue;

    public StateMachine() {
        eventMap = new HashMap<String, Event>();
        eventQueue = new ConcurrentLinkedQueue<>();
    }

    public interface Event {
        void call();
    }

    public enum State {
        QUEUED,
        RUNNING,
        STOPPED
    }

    public void register(String name, Event event) {
        synchronized (eventLock) {
            eventMap.put(name, event);
        }
    }

    public Collection<Event> events() {
        return eventMap.values();
    }

    public Event getEvent(String name) {
        return eventMap.get(name);
    }

    public boolean queueEvent(String name) {
        Event event = getEvent(name);

        if (event == null) {
            return false;
        }
        synchronized (eventLock) {
            eventQueue.offer(event);
        }

        return true;
    }
}
