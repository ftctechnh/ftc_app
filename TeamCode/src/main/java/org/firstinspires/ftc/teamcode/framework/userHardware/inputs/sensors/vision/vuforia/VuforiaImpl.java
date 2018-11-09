package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.vuforia;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class VuforiaImpl {

    public Vuforia vuforia;

    public VuforiaImpl(){
        vuforia = new Vuforia();
    }

    public VuforiaImpl(String camera) {
        vuforia = new Vuforia(camera);
    }

    public VuforiaLocalizer getVuforia(){
        return vuforia.getVuforia();
    }
}
