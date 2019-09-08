package org.firstinspires.ftc.teamcode.robot.mecanum;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;

public class MecanumPowers {
    public double frontLeft;
    public double frontRight;
    public double backLeft;
    public double backRight;

    public MecanumPowers(double frontLeft, double frontRight, double backLeft, double backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MecanumPowers mecanumPowers = (MecanumPowers) o;
        return MathUtil.approxEquals(mecanumPowers.frontLeft, this.frontLeft) &&
                MathUtil.approxEquals(mecanumPowers.frontRight, this.frontRight) &&
                MathUtil.approxEquals(mecanumPowers.backLeft, this.backLeft) &&
                MathUtil.approxEquals(mecanumPowers.backRight, this.backRight);
    }
}
