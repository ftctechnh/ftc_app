package org.firstinspires.ftc.teamcode.autonomous.odometry;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.apache.commons.math3.linear.RealMatrix;
import org.firstinspires.ftc.teamcode.autonomous.odometry.EncoderWheel;
import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.openftc.revextensions2.RevBulkData;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;

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
    public static double LEFT_WHEEL_RADIUS = 1.14426;
    public static double RIGHT_WHEEL_RADIUS = 1.14426;
    public static double LAT_WHEEL_RADIUS = 1.14426;

    static final double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LEFT_Y_POS = 2.85;
    public static double RIGHT_Y_POS = -3.25;
    public static double LAT_X_POS = -7.25;

    static final EncoderWheel[] WHEELS = {
            new EncoderWheel(0, LEFT_Y_POS, 0.0, 0), // left
            new EncoderWheel(0, RIGHT_Y_POS, 0.0, 1), // right
            new EncoderWheel(LAT_X_POS, 0, Math.toRadians(90), 2) // lateral
    };

    public Pose currentPosition;
    public Pose relativeRobotMovement;
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
        relativeRobotMovement = new Pose(0, 0, 0);
    }

    public static double encoderTicksToInches(int ticks, double wheel_radius) {
        return wheel_radius * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public static int inchesToEncoderTicks(double inches) {
        return (int) Math.round(inches * TICKS_PER_REV / (LEFT_WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO));
    }

    public void update(RevBulkData data) {
        double[] wheelDeltas = new double[] {
                encoderTicksToInches(data.getMotorCurrentPosition(0) - prevWheelPositions[0],
                LEFT_WHEEL_RADIUS),
                encoderTicksToInches(data.getMotorCurrentPosition(1) - prevWheelPositions[1],
                        RIGHT_WHEEL_RADIUS),
                encoderTicksToInches(data.getMotorCurrentPosition(2) - prevWheelPositions[2],
                        LAT_WHEEL_RADIUS),
        };

        for (int i = 0; i < 3; i++) {
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

        relativeRobotMovement = relativeRobotMovement.add(robotPoseDelta);

        currentPosition = MathUtil.relativeOdometryUpdate(currentPosition, robotPoseDelta);
    }

    public double x() { return currentPosition.x; }
    public double y() { return -currentPosition.y; }
    public double h() { return currentPosition.heading; }
    public Point pos() {
        return new Point(currentPosition.x, currentPosition.y);
    }
    public Pose pose() {
        return new Pose(currentPosition.x, currentPosition.y, currentPosition.heading);
    }
}

