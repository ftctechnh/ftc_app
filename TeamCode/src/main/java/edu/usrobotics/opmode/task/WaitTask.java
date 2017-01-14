package edu.usrobotics.opmode.task;

/**
 * Created by dsiegler19 on 1/8/17.
 */
public class WaitTask implements Task{

    private long waitTime;
    private long startTime;

    public WaitTask(long millis){

        this.waitTime = millis;

    }

    public boolean execute(){

        return System.currentTimeMillis() - startTime >= waitTime;

    }

    public boolean isCompleted(){

        return System.currentTimeMillis() - startTime >= waitTime;

    }

    public TaskType getType(){

        return TaskType.WAIT;

    }

    // Event fired when the State Machine reaches this task.
    public void onReached (){

        this.startTime = System.currentTimeMillis();

    }

    // Event fired when the State Machine updates this task. Return true if task was completed while executing.
    public boolean onExecuted (){

        return System.currentTimeMillis() - startTime >= waitTime;

    }

    public void onCompleted (){}

}
