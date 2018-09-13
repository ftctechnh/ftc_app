package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

/**
 * Created by Computer on 8/9/2018.
 */

public final class Toggle {

    public enum Status{
        NOT_BEGUN,
        PRESSING,
        COMPLETE
    }

    private Status stat = Status.NOT_BEGUN;

    public Status getStatus(boolean buttonStatus){
        if (buttonStatus && stat == Status.NOT_BEGUN){
            stat = Status.PRESSING;
        }
        else if (! buttonStatus && stat == Status.PRESSING){
            stat = Status.COMPLETE;
        }
        else if (stat == Status.COMPLETE){
            stat = Status.NOT_BEGUN;
        }
        return stat;
    }

    public final boolean isPressed(boolean buttonStatus){
        boolean pressed = false;

        if (getStatus(buttonStatus) == Status.COMPLETE){
            pressed = true;
        }
        return pressed;
    }
}
