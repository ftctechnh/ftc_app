package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.util.NanoClock

/**
 * This class provides basic functionality of a mecanum drive using on [MecanumKinematics].
 *
 * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
 * @param wheelBase distance between pairs of wheels on the same side of the robot
 */
abstract class MecanumDrive @JvmOverloads constructor(
        val trackWidth: Double,
        val wheelBase: Double = trackWidth,
        clock: NanoClock = NanoClock.system()
) : Drive() {

    /**
     * Default localizer for mecanum drivetrains based on the drive encoders.
     *
     * @param drive drive
     * @param clock clock
     */
    // TODO: add heading support to the localizer
    class MecanumLocalizer @JvmOverloads constructor(
            private val drive: MecanumDrive,
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
                val robotPoseDelta = MecanumKinematics.wheelToRobotVelocities(wheelVelocities, drive.wheelBase, drive.trackWidth) * dt
                poseEstimate = Kinematics.relativeOdometryUpdate(poseEstimate, robotPoseDelta)
            }
            lastWheelPositions = wheelPositions
            lastUpdateTimestamp = timestamp
        }
    }

    override var localizer: Localizer = MecanumLocalizer(this, clock)

    override fun setVelocity(poseVelocity: Pose2d) {
        val powers = MecanumKinematics.robotToWheelVelocities(poseVelocity, trackWidth, wheelBase)
        setMotorPowers(powers[0], powers[1], powers[2], powers[3])
    }

    /**
     * Sets the following motor powers (normalized voltages). All arguments are on the interval `[-1.0, 1.0]`.
     */
    abstract fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double)

    /**
     * Returns the positions of the wheels in linear distance units.
     */
    abstract fun getWheelPositions(): List<Double>
}