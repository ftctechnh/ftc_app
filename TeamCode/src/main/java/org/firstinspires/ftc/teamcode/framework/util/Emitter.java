package org.firstinspires.ftc.teamcode.framework.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// Emitter is event handling middleware.
//
// Create a new event with 'emit'. Register event handlers with 'on'.
public class Emitter {
    private ConcurrentHashMap<String, Callable<Boolean>> EventRegistry = new ConcurrentHashMap<>();
    private ArrayList<String> PausedEvents = new ArrayList<>();
    private ConcurrentHashMap<String, Future<Boolean>> cache = new ConcurrentHashMap<>();

    private ExecutorService service;

    public Emitter() {
        service = Executors.newCachedThreadPool();
    }

    // Register a new event handler.
    // An event handler is a Runnable that is run on an Executor.
    public void on(String eventName, Callable eventHandler) {
        EventRegistry.put(eventName, eventHandler);
    }

    public void pauseEvent(String name) {
        PausedEvents.add(name);
    }

    public void resumeEvent(String name) {
        PausedEvents.remove(name);
    }

    public void removeEvent(String name) {
        EventRegistry.remove(name);
    }

    // Send a named event.
    //
    // This will run all event handlers registered to this event. Each event handler will be
    // executed inside of the executor service, which means events may be handled in parallel.
    public Future<Boolean> emit(String name) throws RuntimeException {

        /*// Clears the cache of finished futures
        ArrayList<String> finishedFutures = new ArrayList<>();
        for(Map.Entry<String, Future<Boolean>> entry:cache.entrySet()){
            if(entry.getValue().isDone())finishedFutures.add(entry.getKey());
        }
        for(String finishedFuture:finishedFutures){
            cache.remove(finishedFuture);
        }*/

        for (String pausedName : PausedEvents) {
            if (pausedName.equals(name)) return new EmptyResult();
        }

        Future<Boolean> f = fire(name);
        this.cache.put(name, f);
        return f;
    }

    public Future<Boolean> futureFor(String eventName) {
        if (this.cache.contains(eventName)) {
            return this.cache.get(eventName);
        }
        return new EmptyResult();
    }

    // This does the actual firing of the event. The emit() method calls this, and caches
    // the resulting future for cancelation.
    protected Future<Boolean> fire(String eventName) throws RuntimeException {
        Callable eventHandler = EventRegistry.get(eventName);
        if (eventHandler != null) {
            return service.submit(eventHandler);
        }
        // By default, we return a future that returns false.
        return new EmptyResult();
    }

    public void shutdown() {
        for (Map.Entry<String, Future<Boolean>> future : cache.entrySet()) {
            if (!futureFor(future.getKey()).isDone()) {
                futureFor(future.getKey()).cancel(true);
            }
        }
        service.shutdownNow();
    }

    class EmptyResult implements Future<Boolean> {
        @Override
        public boolean cancel(boolean b) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public Boolean get() throws InterruptedException, ExecutionException {
            return false;
        }

        @Override
        public Boolean get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return false;
        }
    }
}
