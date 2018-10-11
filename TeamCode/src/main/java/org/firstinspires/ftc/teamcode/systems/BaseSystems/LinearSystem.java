package org.firstinspires.ftc.teamcode.systems.BaseSystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by MichaelSimpson on 3/15/2018.
 */

public abstract class LinearSystem extends System {

    protected static final double ABSOLUTE_MAX = 1.0;
    protected static final double ABSOLUTE_MIN = 0.0;

    protected int max;
    protected double[] positions;

    public LinearSystem(OpMode opMode, String systemName) {
        super(opMode, systemName);

        //this.positions = new double[config.getInt("arraySize")];
        this.max = positions.length - 1;

        for (int i = 0; i < positions.length; i++) {
            //positions[i] = config.getDouble("position" + i);
        }
    }

    public abstract void goToPosition(double targetPosition, double power);

    public void goToPosition(int targetPosition, double power) {
        goToPosition(positions[targetPosition], power);
    }
}
