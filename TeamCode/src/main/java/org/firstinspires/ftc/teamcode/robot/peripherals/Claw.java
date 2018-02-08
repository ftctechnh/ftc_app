package org.firstinspires.ftc.teamcode.robot.peripherals;

/**
 * Created by Derek on 2/6/2018.
 */

public interface Claw extends Peripheral {
    Claw setPosition(double position);
//    Claw setPosition(ClawPositions.Position position);
    double getPosition();
    ClawPositions getClampPositions();
}
