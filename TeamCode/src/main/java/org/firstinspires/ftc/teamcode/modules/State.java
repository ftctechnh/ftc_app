package org.firstinspires.ftc.teamcode.modules;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public abstract class State {
    final String id;
    StateMachine stateMachine;

    public State(@NonNull String id) {
        this.id = id;
    }

    public abstract void run();

    public final void changeState(@NonNull String stateId) {
//        if(this == stateMachine.activeState) {
//            Log.d("State", this.id + " is already the active state!");
//            return;
//        }

        stateMachine.changeState(stateId);
    }

    public void sendData(String key, int anInt) {
        stateMachine.sendData(key, anInt);
    }

    public void sendData(String key, boolean aBool) {
        stateMachine.sendData(key, aBool);
    }

    public void sendData(String key, double aDouble) {
        stateMachine.sendData(key, aDouble);
    }

    protected int getInt(String key) {
        return stateMachine.getInt(key);
    }

    protected boolean getBool(String key) {
        return stateMachine.getBool(key);
    }

    protected double getDouble(String key) {
        return stateMachine.getDouble(key);
    }
}