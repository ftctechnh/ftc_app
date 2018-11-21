package org.firstinspires.ftc.teamcode.Components

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.Utils.Logger
import kotlin.math.min

enum class Direction(val intRepr: Int) {
    FORWARD(1),
    BACKWARD(-1),
    SPIN_CW(1),
    SPIN_CCW(-1),
    FORWARD_LEFT(1),
    FORWARD_RIGHT(-1),
    BACKWARD_LEFT(1),
    BACKWARD_RIGHT(-1)
}

class DriveTrain2(val opMode: LinearOpMode){
    val l:Logger = Logger("DRIVETRAIN2")
    val lDrive = Motor(opMode.hardwareMap, "lDrive")
    val rDrive  = Motor(opMode.hardwareMap, "rDrive")
    val driveMotors = MotorGroup()

    init {
        initMotors()
    }

    fun initMotors() {
        lDrive.setDirection(DcMotorSimple.Direction.FORWARD)
        rDrive.setDirection(DcMotorSimple.Direction.REVERSE)
        driveMotors.addMotor(lDrive)
        driveMotors.addMotor(rDrive)
        l.log("Initialized motors")
    }

    fun setPowers(lPower: Double, rPower: Double) {
        lDrive.setPower(lPower)
        rDrive.setPower(rPower)
    }

    fun stopALL(coast: Boolean = false) {
        lDrive.stop(coast)
        rDrive.stop(coast)
    }

    fun move(dir: Direction, power: Double) {
        when (dir) {
            Direction.FORWARD -> { setPowers(power, power) }
            Direction.BACKWARD -> { setPowers(-power, -power) }
            Direction.SPIN_CW -> { setPowers(-power, power) }
            Direction.SPIN_CCW -> { setPowers(power, -power) }
            Direction.FORWARD_LEFT -> { setPowers(0.0, power) }
            Direction.FORWARD_RIGHT -> { setPowers(power, 0.0) }
            Direction.BACKWARD_LEFT -> { setPowers(0.0, -power) }
            Direction.BACKWARD_RIGHT -> { setPowers(-power, 0.0) }
        }
    }

    fun drive(dir: Direction, dist: Double, timeout: Int = 10) {
        driveMotors.prepareEncoderDrive()

        val lTarget = lDrive.motor.currentPosition +
                dir.intRepr * Values.TICKS_PER_INCH_FORWARD * dist
        val rTarget = rDrive.motor.currentPosition +
                dir.intRepr * Values.TICKS_PER_INCH_FORWARD * dist
        val minError = 50

        val lPID = PID(PIDCoefficients(0.0, 0.0, 0.0), lTarget)
        val rPID = PID(PIDCoefficients(0.0, 0.0, 0.0), rTarget)

        lPID.initController(lDrive.motor.currentPosition.toDouble())
        rPID.initController(rDrive.motor.currentPosition.toDouble())

        while (opMode.opModeIsActive() && !opMode.isStopRequested &&
                lPID.prevError!! > minError && rPID.prevError!! > minError) {
            setPowers(
                    lPID.output(lDrive.motor.currentPosition.toDouble()),
                    rPID.output(rDrive.motor.currentPosition.toDouble())
            )
        }

        stopALL()
    }

    fun rotate(dir: Direction, angle: Int, timeout: Int = 10) {
        TODO()
    }

    fun rotateTo(heading: Double, timeout: Int = 10) {
        TODO()
    }

}