package edu.usrobotics.opmode.task;

/**
 * Created by Max on 9/18/2016.
 */
public class ConcurrentTaskSet implements Task {
    private Task[] tasks;
    private boolean[] completedTasks;

    private boolean completed = false;

    public ConcurrentTaskSet (Task... tasks) {
        this.tasks = tasks;
        this.completedTasks = new boolean[tasks.length];
    }

    @Override
    public boolean execute() {
        boolean allComplete = true;

        for (int i=0; i<tasks.length; i++) {
            completedTasks[i] = completedTasks[i] || tasks[i].execute();
            completedTasks[i] = completedTasks[i] || tasks[i].onExecuted();

            allComplete = allComplete && completedTasks[i]; // If a task is not completed, then the task set is not completed either.
        }

        return allComplete;
    }

    @Override
    public boolean isCompleted() {
        return completed;
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
        return false;
    }

    @Override
    public void onCompleted() {
    }

    public boolean isTaskCompleted (int i) {
        return tasks[i].isCompleted();
    }
}
