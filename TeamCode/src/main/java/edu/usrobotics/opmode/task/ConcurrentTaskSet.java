package edu.usrobotics.opmode.task;

/**
 * Created by Max on 9/18/2016.
 */
public class ConcurrentTaskSet implements Task {
    private Task[] tasks;
    private boolean[] completed;

    public ConcurrentTaskSet (Task... tasks) {
        this.tasks = tasks;
        this.completed = new boolean[tasks.length];
    }

    @Override
    public boolean execute() {
        boolean allComplete = true;

        for (int i=0; i<tasks.length; i++) {
            completed[i] = completed[i] || tasks[i].execute();
            allComplete = allComplete && completed[i];
        }

        return allComplete;
    }

    @Override
    public TaskType getType() {
        return TaskType.NONE;
    }

    @Override
    public void onReached() {
        for (Task t : tasks) t.onReached();
    }

    @Override
    public boolean onExecuted() {
        boolean allComplete = true;

        for (int i=0; i<tasks.length; i++) {
            completed[i] = completed[i] || tasks[i].onExecuted();
            allComplete = allComplete && completed[i];
        }

        return allComplete;
    }

    @Override
    public void onCompleted() {
        for (Task t : tasks) t.onCompleted();
    }
}
