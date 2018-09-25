package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.util.NanoClock

/**
 * This class provides basic functionality of a tank/differential drive using on [TankKinematics].
 *
 * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
 */
abstract class TankDrive @JvmOverloads constructor(
        val trackWidth: Double,
        clock: NanoClock = NanoClock.system()
) : Drive() {

    /**
     * Default localizer for tank drivetrains based on the drive encoders.
     *
     * @param drive drive
     * @param clock clock
     */
    class TankLocalizer @JvmOverloads constructor(
            private val drive: TankDrive,
            private val clock: NanoClock = NanoClock.system()
    ) : Localizer {
        override var poseEstimate: Pose2d = Pose2d()
            set(value) {
                lastWheelPositions = emptyList()
                lastUpdateTimestamp = Double.NaN
                field = value
            }
        private var lastWheelPositions = emptyList<Double>()
        private var lastUpdateTimestamp = Double.NaN

        override fun update() {
            val wheelPositions = drive.getWheelPositions()
            val timestamp = clock.seconds()
            if (lastWheelPositions.isNotEmpty()) {
                val dt = timestamp - lastUpdateTimestamp
                val wheelVelocities = wheelPositions
                        .zip(lastWheelPositions)
                        .map { (it.first - it.second) / dt }
                val robotPoseDelta = TankKinematics.wheelToRobotVelocities(wheelVelocities, drive.trackWidth) * dt
                poseEstimate = Kinematics.relativeOdometryUpdate(poseEstimate, robotPoseDelta)
            }
            lastWheelPositions = wheelPositions
            lastUpdateTimestamp = timestamp
        }
    }

    override var localizer: Localizer = TankLocalizer(this, clock)

    override fun setVelocity(poseVelocity: Pose2d) {
        val powers = TankKinematics.robotToWheelVelocities(poseVelocity, trackWidth)
        setMotorPowers(powers[0], powers[1])
    }

    /**
     * Sets the following motor powers (normalized voltages). All arguments are on the interval `[-1.0, 1.0]`.
     */
    abstract fun setMotorPowers(left: Double, right: Double)

    /**
     * Returns the positions of the wheels in linear distance units.
     */
    abstract fun getWheelPositions(): List<Double>
}