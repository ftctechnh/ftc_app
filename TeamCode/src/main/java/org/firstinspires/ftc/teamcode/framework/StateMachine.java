package org.firstinspires.ftc.teamcode.framework;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StateMachine {

    private ArrayList<State> states = new ArrayList<>(), activeStates = new ArrayList<>(), finishedStates, startingStates;

    private ExecutorService service;

    protected StateMachine() {
        service = Executors.newCachedThreadPool();
        activeStates.add(new State("start", "", ()-> true));
    }

    protected void prepare() throws StateConfigurationException {
        for(State state:states) {
            boolean noPrevious = true;

            for (State checkState:states) {
                if(state.getPreviousState().equals(checkState.getName()))noPrevious = false;
            }

            if(state.getPreviousState().equals("start"))noPrevious = false;

            if(noPrevious) throw new StateConfigurationException(state.getName(), state.getPreviousState());
        }
    }

    protected void update() {
        finishedStates = new ArrayList<>();
        startingStates = new ArrayList<>();

        for (State activeState : activeStates) {
            if (activeState.isDone()) {
                finishedStates.add(activeState);
                for (State state : states) {
                    if (state.getPreviousState().equals(activeState.getName())) fire(state);
                }
            }
        }

        activeStates.removeAll(finishedStates);
        activeStates.addAll(startingStates);
    }

    private void fire(State state) throws RuntimeException {
        if (state.getRun() != null) {
            startingStates.add(state);
            state.setFuture(service.submit(state.getRun()));
        }
    }

    protected void addState(State state) {
        states.add(state);
    }

    protected void shutdown() {
        for (State activeState : activeStates) {
            activeState.cancel();
        }
    }
}