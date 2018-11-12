package org.firstinspires.ftc.teamcode.framework;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Emitter is event handling middleware.
//
// Create a new event with 'emit'. Register event handlers with 'on'.
public class Emitter {
    private ConcurrentHashMap<String, Runnable> EventRegistry = new ConcurrentHashMap<>();
    private ExecutorService service;

    public Emitter(){
        service = Executors.newCachedThreadPool();
    }

    // Register a new event handler.
    // An event handler is a Runnable that is run on an Executor.
    public void on(String eventName, Runnable eventHandler) {
        EventRegistry.put(eventName,eventHandler);
    }

    // Send a named event.
    //
    // This will run all event handlers registered to this event. Each event handler will be
    // executed inside of the executor service, which means events may be handled in parallel.
    public void emit(String eventName) throws RuntimeException {
        Runnable eventHandler = EventRegistry.get(eventName);
        if(eventHandler != null){
            service.execute(eventHandler);
        }
    }

    public void shutdown() {
        service.shutdownNow();
    }
}
