package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

/**
 * Created by Kaden on 2/6/18.
 */

public class ClosableVuforiaLocalizer extends VuforiaLocalizerImpl {
    boolean closed = false;
    public ClosableVuforiaLocalizer(Parameters parameters) {
        super(parameters);
    }
    @Override
    public void close() {
        if (!closed) super.close();
        closed = true;
    }
}