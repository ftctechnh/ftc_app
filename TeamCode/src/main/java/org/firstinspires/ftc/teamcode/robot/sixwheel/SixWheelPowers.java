package org.firstinspires.ftc.teamcode.robot.sixwheel;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;

public class SixWheelPowers {
    public double left;
    public double right;

    public SixWheelPowers(double dX, double dTheta) {
        this.left = MathUtil.clamp(dX + dTheta);
        this.right = MathUtil.clamp(dX + dTheta);
    }
}
