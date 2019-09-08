package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.robotcore.util.Range;

public class MecanumUtil {
    private static double clipScale(double d, double scale) {
        return Range.clip(d * scale, -1, 1);
    }

    public static double deadZone(double control, double deadZone) {
        return Math.abs(control) > deadZone ? control : 0;
    }

    public static MecanumPowers powersFromAngle(double theta, double thetaScale, double turn) {
        return new MecanumPowers(
                clipScale(Math.sin(theta), thetaScale) - turn,
                clipScale(Math.cos(theta), thetaScale) + turn,
                clipScale(Math.cos(theta), thetaScale) - turn,
                clipScale(Math.sin(theta), thetaScale) + turn
        );
    }
}
