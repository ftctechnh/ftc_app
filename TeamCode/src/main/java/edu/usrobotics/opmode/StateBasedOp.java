package edu.usrobotics.opmode;

import java.util.LinkedList;
import java.util.Queue;

import edu.usrobotics.opmode.task.Task;

/**
 * Created by Max on 9/14/2016.
 */
public class StateBasedOp extends BaseOp {

    State state; // Current state machine state
    Route route; // Currently Executed Route

    Queue<Route> routeQueue = new LinkedList<>(); // Contains routes that will be executed once 'route' is completed. (NO INCLUDE route)



    public Route addRoute (Route r) {
        routeQueue.add(r);

        return r;
    }

    public boolean executeTask (Task task) {
        boolean completed;

        completed = task.execute();

        completed = task.onExecuted() || completed;
        return completed;
    }

    public void logicUpdate () {
        route = (route != null ? route : routeQueue.remove()); // Grab current route, or new one from queue

        if (route == null) { // If there is no route
            // then we cri
        }
    }

    public void routeUpdate () {
        if (route != null) {
            Task task = route.getCurrentTask();

            telemetry.addData("Task", task.getType().name());

            boolean completed = executeTask (task);
            if (completed) { // If task completed
                onTaskCompleted (route.getCurrentTask(), route);
            }

        } else {
            state = State.EXIT;
        }
    }

    public void onRouteCompleted (Route r) {
        r.onCompleted ();

        route = null;
        state = State.LOGIC;
    }

    public void onTaskCompleted (Task task, Route route) {
        task.onCompleted ();

        if (route.taskCompleted()) { // If Route completed
            onRouteCompleted(route);

        } else { // Next task in route
            route.getTask(route.taskIndex).onReached ();
        }
    }

    @Override public void start ()
    {
        super.start();

        state = State.INIT;

    }

    @Override public void loop ()
    {
        super.loop();

        switch (state)
        {
            case INIT:
                state = State.LOGIC;

                break;

            case LOGIC:

                logicUpdate ();

                break;

            case ROUTE:

                routeUpdate ();

                break;

            case EXIT:
                break;

            default:

                break;
        }
    }

}
