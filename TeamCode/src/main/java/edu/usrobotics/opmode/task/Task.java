package edu.usrobotics.opmode.task;


public interface Task {

    // Called to update the task, return true if completed.
    boolean execute ();

    // Returns the TaskType of this task.
    // Mostly used for debugging as Task Type can be set to anything regardless of the actual task purpose.
    TaskType getType ();

    // Event fired when the State Machine reaches this task.
    void onReached ();

    // Event fired when the State Machine updates this task. Return true if task was completed while executing.
    boolean onExecuted ();

    // Event fired when task completed
    void onCompleted ();
}
