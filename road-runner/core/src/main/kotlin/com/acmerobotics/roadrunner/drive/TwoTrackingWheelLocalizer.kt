package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.util.NanoClock
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.DecompositionSolver
import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.MatrixUtils
import kotlin.math.cos
import kotlin.math.sin

/**
 * Localizer based on two unpowered tracking omni wheels and an orientation sensor.
 *
 * @param wheelPositions wheel positions relative to the center of the robot (positive X points forward on the robot)
 * @param wheelOrientations angular orientations of the wheels measured counterclockwise from positive X in radians
 * @param clock clock
 */
abstract class TwoTrackingWheelLocalizer @JvmOverloads constructor(
        wheelPositions: List<Vector2d>,
        wheelOrientations: List<Double>,
        private val clock: NanoClock = NanoClock.system()
) : Localizer {
    override var poseEstimate: Pose2d = Pose2d()
        set(value) {
            lastWheelPositions = emptyList()
            lastHeading = Double.NaN
            lastUpdateTimestamp = Double.NaN
            field = value
        }
    private var lastWheelPositions = emptyList<Double>()
    private var lastHeading = Double.NaN
    private var lastUpdateTimestamp = Double.NaN

    private val forwardSolver: DecompositionSolver

    init {
        if (wheelPositions.size != 2) {
            throw IllegalArgumentException("2 wheel positions must be provided")
        }

        if (wheelOrientations.size != 2) {
            throw IllegalArgumentException("2 wheel orientations must be provided")
        }

        val inverseMatrix = Array2DRowRealMatrix(3, 3)
        for (i in 0..1) {
            val orientationVector = Vector2d(cos(wheelOrientations[i]), sin(wheelOrientations[i]))
            val positionVector = wheelPositions[i]
            inverseMatrix.setEntry(i, 0, orientationVector.x)
            inverseMatrix.setEntry(i, 1, orientationVector.y)
            inverseMatrix.setEntry(i, 2,
                    positionVector.x * orientationVector.y - positionVector.y * orientationVector.x)
        }
        inverseMatrix.setEntry(2, 2, 1.0)

        forwardSolver = LUDecomposition(inverseMatrix).solver
    }

    override fun update() {
        val wheelPositions = getWheelPositions()
        val heading = getHeading()
        val timestamp = clock.seconds()
        if (lastWheelPositions.isNotEmpty()) {
            val dt = timestamp - lastUpdateTimestamp
            val wheelVelocities = wheelPositions
                    .zip(lastWheelPositions)
                    .map { (it.first - it.second) / dt }
            val omega = (heading - lastHeading) / dt
            val poseDeltaVector = forwardSolver.solve(MatrixUtils.createRealMatrix(
                    arrayOf((wheelVelocities + omega).toDoubleArray())
            ).transpose())
            val robotPoseDelta = Pose2d(
                    poseDeltaVector.getEntry(0,0),
                    poseDeltaVector.getEntry(1,0),
                    poseDeltaVector.getEntry(2,0)
            )
            poseEstimate = Kinematics.relativeOdometryUpdate(poseEstimate, robotPoseDelta)
        }
        lastWheelPositions = wheelPositions
        lastHeading = heading
        lastUpdateTimestamp = timestamp
    }

    /**
     * Returns the positions of the tracking wheels in the desired distance units (not encoder counts!)
     */
    abstract fun getWheelPositions(): List<Double>

    /**
     * Returns the heading of the robot (usually from a gyroscope or IMU).
     */
    abstract fun getHeading(): Double
}