package org.firstinspires.ftc.teamcode.modules;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public abstract class State {
    final String id;
    StateMachine stateMachine;

    private Map<String, Integer> parameterInt;
    private Map<String, Boolean> parameterBool;

    public State(@NonNull String id) {
        this.id = id;
        parameterInt = new HashMap<>();
        parameterBool = new HashMap<>();
    }

    public abstract void run();

    public final void changeState(@NonNull String stateId) {
        if(this == stateMachine.activeState) {
            Log.d("State", this.id + " is already the active state!");
            return;
        }

        stateMachine.changeState(stateId);
    }

    public void sendData(String key, int anInt) {
        parameterInt.put(key, anInt);
    }

    public void sendData(String key, boolean aBool) {
        parameterBool.put(key, aBool);
    }

    protected int getInt(String key) {
        return parameterInt.get(key);
    }

    protected boolean getBool(String key) {
        return parameterBool.get(key);
    }
}