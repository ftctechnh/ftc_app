package edu.usrobotics.opmode.task;

/**
 * Created by Max on 9/18/2016.
 */
public class ConcurrentTaskSet implements Task {
    private Task[] tasks;
    private boolean completed = false;

    public ConcurrentTaskSet (Task... tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean execute() {
        boolean allComplete = true;

        boolean taskCompleted;
        for (Task task : tasks) {
            if (task.isCompleted())
                continue;

            taskCompleted = task.execute(); // Execute & Mark if completed during execution
            taskCompleted =  task.onExecuted() || taskCompleted; // Call Event & Mark if completed during event

            if (taskCompleted)
                task.onCompleted(); // Call completed event if task completed.

            allComplete = allComplete && taskCompleted; // If task is not completed, task set is not completed either.
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
        for (Task task : tasks) {
            if (!task.isCompleted())
                task.onCompleted();
        }
        completed = true;
    }

    public boolean isTaskCompleted (int i) {
        return tasks[i].isCompleted();
    }
}
