package org.firstinspires.ftc.teamcode.robot.locomotion;

/**
 * Created by Derek on 12/7/2017.
 */

public interface DriveMode {


    /**
     *
     */


    public void update(DriveInfo driveInfo);

    public type getType();
}

enum type {
    MECCANUM(4),TANK(2),ARCDRIVE(2);

    private final int wheelCount;

    type(int wheelCount) {
        this.wheelCount = wheelCount;
    }

    public int getWheelCount() {
        return this.getWheelCount();
    }
}
