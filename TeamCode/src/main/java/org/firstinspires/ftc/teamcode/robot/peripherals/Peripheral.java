package org.firstinspires.ftc.teamcode.robot.peripherals;

import org.firstinspires.ftc.teamcode.general.Updatable;

/**
 * Created by Derek on 2/7/2018.
 */

public interface Peripheral extends Updatable {
    String getName();

    boolean test();
}
