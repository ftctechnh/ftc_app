package org.firstinspires.ftc.teamcode.robot.mecanum;

import org.firstinspires.ftc.teamcode.common.math.MathUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public MecanumPowers(double x, double y, double turnPower) {
        this.frontLeft = y - turnPower + x * 1.5;
        this.backLeft = y - turnPower - x * 1.5;
        this.frontRight = y + turnPower + x * 1.5;
        this.backRight = y + turnPower - x * 1.5;
        this.scale();
    }

    public List<Double> asList() {
        return Arrays.asList(this.frontLeft, this.frontRight, this.backLeft, this.backRight);
    }

    // If we're somehow above one, scale back down
    private void scale() {
        List<Double> vals = asList();
        double max = Math.max(Collections.max(vals), -Collections.min(vals));
        if (max > 1) {
            this.frontLeft /= max;
            this.frontRight /= max;
            this.backLeft /= max;
            this.backRight /= max;
        }
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


    /*
    %.1f----%.1f
    | Front |
    |       |
    |       |
    %.1f----%.1f
     */
    @Override
    public String toString() {
        return String.format(
                "\n" +
                "%.1f---%.1f\n" +
                "| Front |\n" +
                "|       |\n" +
                "|       |\n" +
                "%.1f---%.1f\n"
                , frontLeft, frontRight, backLeft, backRight);
    }
}
