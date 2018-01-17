package org.firstinspires.ftc.teamcode.robot.locomotion;

import org.firstinspires.ftc.teamcode.math.vector.Vec3;

import static org.firstinspires.ftc.teamcode.Constants.MOTOR_CLAMP;

/**
 * Created by Derek on 12/7/2017.
 *
 * This class should be able to take various forms of input, and produce a single output for all drives types to use
 * how the values are interpreted is up to the end user
 */

public class DriveInfo {
    private Vec3 speed = new Vec3(0,0,0);

    public DriveInfo setFromNorm(Vec3 vec3) {
        this.speed = vec3;
        return this;
    }

    public Vec3 getSpeed() {
        return speed.clamp(MOTOR_CLAMP);
    }
}
