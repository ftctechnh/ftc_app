package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.vuforia;

import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision.SamplePosition;

public class SampleVuforia {
    Vuforia vuforia;

    public SampleVuforia(){
        vuforia = new Vuforia();
        vuforia.startTracking("Calc_OT");
    }

    public SamplePosition getSamplePosition(){
        return SamplePosition.UNKNOWN;
    }

    public double[] getPose(){
        return vuforia.getPose();
    }
}
