package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

public class RelicVuMarkTracker {

    Vuforia vuforia;

    public RelicVuMarkTracker() {
        vuforia = new Vuforia(true);
        //vuforia.setLED(false);
        //vuforia.startTracking("RelicVuMark");
    }

    public RelicRecoveryVuMark getVuMark() {
        return RelicRecoveryVuMark.from(vuforia.getTemplate());
    }

    public double[] getPose() {
        return vuforia.getPose();
    }
}
