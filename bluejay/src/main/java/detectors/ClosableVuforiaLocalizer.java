package detectors;

import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

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