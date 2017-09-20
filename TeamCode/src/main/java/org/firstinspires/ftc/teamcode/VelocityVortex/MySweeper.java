package org.firstinspires.ftc.teamcode.VelocityVortex;

/**
 * Created by logan on 11/6/2016.
 */

public class MySweeper {
    double num;
    double r;
    double l;
    public double sweep(double left, double right) {
        double l = left;
        double r = right;
        return motor();
    }
    private double servo() {
        double sweeperPosition = 0.5;
        double sweeperPosition2 = 0.5;

        sweeperPosition = r;
        sweeperPosition = sweeperPosition / 2;
        sweeperPosition = sweeperPosition + 0.5;//This will keep the servo at no power.
        sweeperPosition2 = l;
        sweeperPosition2 = sweeperPosition2 /-2;
        sweeperPosition2 = sweeperPosition2 +0.5;
        if (sweeperPosition > 0.5) {
            num = sweeperPosition;
        } else {
            num = sweeperPosition2;
        }
        return num;
    }
    private double motor() {
        double sweeperPosition;
        double sweeperPosition2;

        sweeperPosition = r;
        sweeperPosition2 = l;

        if (sweeperPosition > 0)
        {
            num = sweeperPosition;
        }

        else
        {
            num = sweeperPosition2;
        }
        return num;
    }
}