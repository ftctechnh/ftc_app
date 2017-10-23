package org.firstinspires.ftc.robotcore.external;

import com.borsch.sim.SimulatedVuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

/**
 * Stub
 */
public class ClassFactory
{
    /**
     * Stub
     */
    public static VuforiaLocalizer createVuforiaLocalizer(VuforiaLocalizer.Parameters parameters)
    {
        return new SimulatedVuforiaLocalizer(parameters);
    }
}
