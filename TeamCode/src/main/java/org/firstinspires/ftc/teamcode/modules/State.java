package org.firstinspires.ftc.teamcode.modules;

import android.support.annotation.NonNull;
import android.util.Log;


public abstract class State {
    final String id;
    StateMachine stateMachine;

    public State(@NonNull String id) {
        this.id = id;
    }

    public abstract void run();

    public final void changeState(@NonNull String stateId) {
        if(this == stateMachine.activeState) {
            Log.d("State", this.id + " is already the active state!");
            return;
        }

        stateMachine.changeState(stateId);
    }
}