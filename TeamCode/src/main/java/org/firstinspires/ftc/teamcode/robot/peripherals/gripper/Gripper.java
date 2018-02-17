package org.firstinspires.ftc.teamcode.robot.peripherals.gripper;

import org.firstinspires.ftc.teamcode.robot.peripherals.Peripheral;
import org.firstinspires.ftc.teamcode.robot.peripherals.PeripheralPosition;

/**
 * Created by Derek on 2/6/2018.
 */

public interface Gripper extends Peripheral {
    Gripper setPosition(double position);
    Gripper setPosition(PeripheralPosition position);
    double getPosition();
    GripperClampPositions getClampPositions();
}
