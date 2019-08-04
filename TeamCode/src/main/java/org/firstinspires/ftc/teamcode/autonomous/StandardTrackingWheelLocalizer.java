package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.firstinspires.ftc.teamcode.common.MathUtil;
import org.firstinspires.ftc.teamcode.common.Point;
import org.firstinspires.ftc.teamcode.common.Pose;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.openftc.revextensions2.RevBulkData;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizer {
    static final double TICKS_PER_REV = 4 * 600;
    static final double WHEEL_RADIUS = 1.14426;
    static final double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    static final double LEFT_Y_POS = 2.85;
    static final double RIGHT_Y_POS = -3.25;
    static final double LAT_X_POS = -7.25;

    static final EncoderWheel[] WHEELS = {
            new EncoderWheel(0, LEFT_Y_POS, 0.0, 0), // left
            new EncoderWheel(0, RIGHT_Y_POS, 0.0, 1), // right
            new EncoderWheel(LAT_X_POS, 0, Math.toRadians(90), 2) // lateral
    };

    public Pose currentPosition;
    int[] prevWheelPositions;
    DecompositionSolver forwardSolver;
    int[] wheelPorts;



    // Argument must be array with >=3 DcMotors,
    public StandardTrackingWheelLocalizer(int leftParallelEncoder, int rightParallelEncoder, int lateralEncoder) {

        Array2DRowRealMatrix inverseMatrix = new Array2DRowRealMatrix(3, 3);
        for (EncoderWheel wheelPosition : WHEELS) {
            Vector2d orientationVector =
                    new Vector2d(Math.cos(wheelPosition.heading), Math.sin(wheelPosition.heading));
            inverseMatrix.setEntry(wheelPosition.row, 0, orientationVector.getX());
            inverseMatrix.setEntry(wheelPosition.row, 1, orientationVector.getY());
            inverseMatrix.setEntry(wheelPosition.row, 2,
                    wheelPosition.x * orientationVector.getY() - wheelPosition.y * orientationVector.getX());
        }

        forwardSolver = new LUDecomposition(inverseMatrix).getSolver();

        if (!forwardSolver.isNonSingular()) {
            throw new IllegalArgumentException("The specified configuration cannot support full localization");
        }

        prevWheelPositions = new int[3]; // Initializes with zeros

        wheelPorts = new int[] {leftParallelEncoder, rightParallelEncoder, lateralEncoder};
        currentPosition = new Pose(0, 0, 0);
    }

    private static double encoderTicksToInches(int ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public void update(RevBulkData data) {
        double[] wheelDeltas = new double[3];
        for (int i = 0; i < 3; i++) {
            wheelDeltas[i] = encoderTicksToInches(
                    data.getMotorCurrentPosition(i) - prevWheelPositions[i]);
            prevWheelPositions[i] = data.getMotorCurrentPosition(i);
        }

        // Below line throws exception
        RealMatrix m = MatrixUtils.createRealMatrix(new double[][] {wheelDeltas});

        RealMatrix rawPoseDelta = forwardSolver.solve(m.transpose());
        Pose robotPoseDelta = new Pose(
                rawPoseDelta.getEntry(0, 0),
                rawPoseDelta.getEntry(1, 0),
                rawPoseDelta.getEntry(2, 0)
        );

        currentPosition = MathUtil.relativeOdometryUpdate(currentPosition, robotPoseDelta);
    }

    public double x() { return currentPosition.x; }
    public double y() { return currentPosition.y; }
    public double h() { return currentPosition.heading; }
    public Point pos() {
        return new Point(currentPosition.x, currentPosition.y);
    }

}

