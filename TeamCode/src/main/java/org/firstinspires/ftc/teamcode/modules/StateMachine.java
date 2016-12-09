package org.firstinspires.ftc.teamcode.modules;

import android.util.Log;

import java.util.Map;
import java.util.HashMap;

public final class StateMachine {
    private Map<String, State> states;
    protected State activeState;
    private ActiveStateThread activeStateThread;

    public StateMachine(State initialState, State... otherStates) {
        this.states = new HashMap<>();

        this.activeState = initialState;
        this.activeStateThread = new ActiveStateThread();
        this.activeState.stateMachine = this;

        this.states.put(activeState.id, activeState);
        for (State state : otherStates) {
            this.states.put(state.id, state);
            state.stateMachine = this;
        }
    }

    public void changeState(String stateId) {
        State result = this.states.get(stateId);
        if(result == null) {
            Log.d("State", "State ID " + stateId + " is invalid!");
        }
        else {
            this.stop();
            this.activeState = result;
            this.activeStateThread = new StateMachine.ActiveStateThread();
            this.start();
        }
    }

    private class ActiveStateThread extends Thread {
        private boolean terminate = false;

        @Override
        public void run() {
            while(!terminate) {
                StateMachine.this.activeState.run();
            }
        }
    }

    public void start() {
        activeStateThread.start();
    }

    public void stop() {
        this.activeStateThread.terminate = true;
    }

    public boolean isOn() {
        return !this.activeStateThread.terminate;
    }
}
