package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.drive.MecanumKinematics
import com.acmerobotics.roadrunner.drive.SwerveKinematics
import com.acmerobotics.roadrunner.drive.TankKinematics
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KinematicsTest {
    @Test
    fun testTankKinematics() {
        val actualVelocity = Pose2d(2.0, 0.0, -Math.PI / 4)
        val wheelVelocities = TankKinematics.robotToWheelVelocities(actualVelocity, 10.0)
        val predictedVelocity = TankKinematics.wheelToRobotVelocities(wheelVelocities, 10.0)
        assertEquals(predictedVelocity, actualVelocity)
    }

    @Test
    fun testMecanumKinematics() {
        val actualVelocity = Pose2d(2.0, 1.0, -Math.PI / 4)
        val wheelVelocities = MecanumKinematics.robotToWheelVelocities(actualVelocity, 10.0, 5.0)
        val predictedVelocity = MecanumKinematics.wheelToRobotVelocities(wheelVelocities, 10.0, 5.0)
        assertEquals(predictedVelocity, actualVelocity)
    }

    @Test
    fun testSwerveKinematics() {
        val actualVelocity = Pose2d(2.0, -1.25, -Math.PI / 4)
        val wheelVelocities = SwerveKinematics.robotToWheelVelocities(actualVelocity, 10.0, 5.0)
        val moduleOrientations = SwerveKinematics.robotToModuleOrientations(actualVelocity, 10.0, 5.0)
        val predictedVelocity = SwerveKinematics.wheelToRobotVelocities(wheelVelocities, moduleOrientations, 10.0, 5.0)
        assertEquals(predictedVelocity, actualVelocity)
    }
}