package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.robotcore.util.Range;

public class MecanumUtil {

    public static final MecanumPowers FORWARD =
            MecanumUtil.powersFromAngle(0, 1, 0);
    public static final MecanumPowers LEFT =
            MecanumUtil.powersFromAngle(-Math.PI / 2, 1, 0);
    public static final MecanumPowers BACKWARD =
            MecanumUtil.powersFromAngle(-Math.PI, 1, 0);
    public static final MecanumPowers RIGHT =
            MecanumUtil.powersFromAngle(-3 * Math.PI / 2, 1, 0);

    public static final MecanumPowers COUNTERCLOCKWISE =
            MecanumUtil.powersFromAngle(0, 0, 1);
    public static final MecanumPowers CLOCKWISE =
            MecanumUtil.powersFromAngle(0, 0, -1);

    private static double clipScale(double d, double scale) {
        return Range.clip(d * scale, -1, 1);
    }

    public static double deadZone(double control, double deadZone) {
        return Math.abs(control) > deadZone ? control : 0;
    }


    // Theta is defined same way as robot theta - 0 radians = forward, and we go counterclockwise

    // Front left  = cos(theta+pi/4)
    // Front right = sin(theta+pi/4)

    // Positive turn turns counterclockwise
    public static MecanumPowers powersFromAngle(double theta, double thetaScale, double turn) {

        theta += Math.PI/4;

        double maxTrig = Math.max(Math.abs(Math.sin(theta)), Math.abs(Math.cos(theta)));
        double scaleFactor = thetaScale / maxTrig;

        return new MecanumPowers(
                clipScale(Math.cos(theta), scaleFactor) - turn,
                clipScale(Math.sin(theta), scaleFactor) + turn,
                clipScale(Math.sin(theta), scaleFactor) - turn,
                clipScale(Math.cos(theta), scaleFactor) + turn
        );
    }
}
