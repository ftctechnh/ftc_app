package org.firstinspires.ftc.teamcode.robot.sixwheel;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Point;

public class SixWheelPowers {
    public double left;
    public double right;

    public SixWheelPowers(double left, double right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SixWheelPowers sixWheelPowers = (SixWheelPowers) o;
        return MathUtil.approxEquals(sixWheelPowers.left, this.left) &&
                MathUtil.approxEquals(sixWheelPowers.right, this.right);
    }
}
