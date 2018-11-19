package org.firstinspires.ftc.teamcode.framework;

import android.support.annotation.NonNull;

import java.util.Enumeration;
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
    private ExecutorService service;

    private ConcurrentHashMap<String, Future<Boolean>> cache;

    public Emitter(){
        service = Executors.newCachedThreadPool();
        cache = new ConcurrentHashMap<String, Future<Boolean>>();
    }

    // Register a new event handler.
    // An event handler is a Runnable that is run on an Executor.
    public void on(String eventName, Callable eventHandler) {
        EventRegistry.put(eventName,eventHandler);
    }

    // Send a named event.
    //
    // This will run all event handlers registered to this event. Each event handler will be
    // executed inside of the executor service, which means events may be handled in parallel.
    public Future<Boolean> emit(String eventName) throws RuntimeException {
        Future<Boolean> f = fire(eventName);
        this.cache.put(eventName, f);
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
        if(eventHandler != null){
            return service.submit(eventHandler);
        }
        // Bye default, we return a future that returns false.
        return new EmptyResult();
    }

    public void refresh(){
        Enumeration<String> set = cache.keys();
        while(set.hasMoreElements()){
            String next = set.nextElement();
            if(cache.get(next).isDone()){
                cache.get(next).cancel(true);
                cache.remove(next);
            }
        }
    }

    public void shutdown() {
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
