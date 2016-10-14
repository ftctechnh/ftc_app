package edu.usrobotics.opmode;

import java.util.ArrayList;

import edu.usrobotics.opmode.task.Task;

/**
 * Created by mborsch19 on 10/28/15.
 */
public class Route {
    private ArrayList<Task> tasks;
    public int taskIndex = 0;

    public Route () {
        this.tasks = new ArrayList<>();
    }

    public Route (ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTaskIndex () {
        return taskIndex;
    }

    public Task getCurrentTask () {
        return tasks.get(taskIndex);
    }

    public Task getTask (int taskIndex) {
        return tasks.get(taskIndex);
    }

    public void addTask (Task task) {
        tasks.add(task);
    }

    public boolean taskCompleted () {
        taskIndex++;

        return taskIndex >= tasks.size();
    }

    // Event fired when last task is completed
    public void onCompleted () {

    }
}
