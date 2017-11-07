package org.firstinspires.ftc.teamcode.modules;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class StateMachine {
    private Map<String, State> states;
    protected State activeState;
    protected ActiveStateThread activeStateThread;

    private Map<String, Integer> parameterInt;
    private Map<String, Boolean> parameterBool;
    private Map<String, Double> parameterDouble;

    public StateMachine(State initialState, State... otherStates) {
        this.states = new HashMap<>();
        this.parameterInt = new HashMap<>();
        this.parameterBool = new HashMap<>();
        this.parameterDouble = new HashMap<>();

        this.activeState = initialState;
        this.activeStateThread = new ActiveStateThread();
        this.activeState.stateMachine = this;

        this.states.put(activeState.id, activeState);
        for (State state : otherStates) {
            state.stateMachine = this;
            this.states.put(state.id, state);
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

    public String getActiveState() {
        return activeState.id;
    }

    private class ActiveStateThread extends Thread {
        private boolean terminate;

        public ActiveStateThread() {
            terminate = false;
        }

        @Override
        public void run() {
            while(!terminate) {
                synchronized (this) {
                    StateMachine.this.activeState.run();
                }
            }
        }
    }

    public StateMachine start() {
        activeStateThread.start();
        return this;
    }

    public StateMachine stop() {
        this.activeStateThread.terminate = true;
        this.activeStateThread = null;
        return this;
    }

    public boolean isOn() {
        return !this.activeStateThread.terminate;
    }



    public void sendData(String key, int anInt) {
        parameterInt.put(key, anInt);
    }

    public void sendData(String key, boolean aBool) {
        parameterBool.put(key, aBool);
    }

    public void sendData(String key, Double aDouble) {
        parameterDouble.put(key, aDouble);
    }

    public int getInt(String key) {
        Integer integer = parameterInt.get(key);

        if (integer == null) {
            return 0;
        }

        return integer;
    }

    public boolean getBool(String key) {
        Boolean bool = parameterBool.get(key);

        if (bool == null) {
            return false;
        }

        return bool;
    }

    public double getDouble(String key) {
        Double dbl = parameterDouble.get(key);

        if (dbl == null) {
            return 0.0;
        }

        return dbl;
    }
}
