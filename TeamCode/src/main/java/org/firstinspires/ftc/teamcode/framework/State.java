package org.firstinspires.ftc.teamcode.framework;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class State{

    private final String name, previousState;
    private final Callable<Boolean> run;
    private Future<Boolean> future = null;

    public State(String name, String previousState, Callable<Boolean> run){
        this.name = name;
        this.previousState = previousState;
        this.run = run;
    }

    public String getName() {
        return name;
    }

    public Callable<Boolean> getRun(){
        return run;
    }

    public String getPreviousState() {
        return previousState;
    }

    public void setFuture(Future<Boolean> future){
        this.future = future;
    }

    public boolean isDone(){
        if(future!=null){
            return future.isDone();
        }
        return true;
    }

    public void cancel(){
        future.cancel(true);
    }
}