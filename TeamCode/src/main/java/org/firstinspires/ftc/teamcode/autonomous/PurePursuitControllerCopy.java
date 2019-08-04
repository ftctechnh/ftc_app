package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.MathUtil;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;
import org.openftc.revextensions2.RevBulkData;

import static org.firstinspires.ftc.teamcode.common.MathUtil.clamp;
import static org.firstinspires.ftc.teamcode.common.MathUtil.deadZone;

public class PurePursuitControllerCopy {

    final static double PREFERRED_ANGLE = 0;
    final static double DECELERATION_RATE = Math.toRadians(30);

    public static SixWheelPowers goToPosition(SixWheelHardware robot, double targetX, double targetY, double targetTheta, double latSpeed, double turnSpeed) {
        double deltaX = targetX - robot.localizer.x();
        double deltaY = targetY - robot.localizer.y();

        double distanceToTarget = Math.hypot(deltaX, deltaY);
        double absAngleToTarget = Math.atan2(deltaX, deltaY);

        // A heading of 0 degrees is forward
        double relativeAngleToTarget = MathUtil.angleWrap(absAngleToTarget - robot.localizer.h());

        double relativeXToTarget = Math.cos(relativeAngleToTarget) * distanceToTarget;
        double relativeYToTarget = Math.sin(relativeAngleToTarget) * distanceToTarget;

        double totalPowerDenom = Math.abs(relativeXToTarget) + Math.abs(relativeYToTarget);
        double movementXPower = relativeXToTarget * latSpeed / totalPowerDenom;
        double movementYPower = relativeYToTarget * latSpeed / totalPowerDenom;

        double relativeTurnAngle = relativeAngleToTarget + PREFERRED_ANGLE - Math.PI;
        double movementTurnPower = Range.clip(relativeTurnAngle / DECELERATION_RATE, -1, 1) * turnSpeed;
        return new SixWheelPowers(movementXPower, movementTurnPower);
    }
}
