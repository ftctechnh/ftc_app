package org.firstinspires.ftc.teamcode.Components

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.Utils.Logger
import org.firstinspires.ftc.teamcode.Models.Direction
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import org.firstinspires.ftc.teamcode.Utils.PIDController
import org.firstinspires.ftc.teamcode.Utils.WSS


class DriveTrain(val opMode: LinearOpMode,val wss:WSS?=null){
    val l:Logger = Logger("DRIVETRAIN2")
    val lDrive = Motor(opMode.hardwareMap, "lDrive")
    val rDrive  = Motor(opMode.hardwareMap, "rDrive")
    val driveMotors = MotorGroup()
    val imu = IMU(opMode.hardwareMap.get(BNO055IMU::class.java, "imu"))

    init {
        fun initMotors() {
            lDrive.setDirection(DcMotorSimple.Direction.FORWARD)
            rDrive.setDirection(DcMotorSimple.Direction.REVERSE)
            driveMotors.addMotor(lDrive)
            driveMotors.addMotor(rDrive)
            l.log("Initialized motors")
//            driveMotors.logInfo()
        }
        l.log("Websocket initialized: ${wss!=null}")

        initMotors()
    }

    fun setPowers(lPower: Double, rPower: Double) {
        lDrive.setPower(lPower)
        rDrive.setPower(rPower)
    }

    fun stopAll(coast: Boolean = false) {
        lDrive.stop(coast)
        rDrive.stop(coast)
    }

    fun move(dir: Direction, power: Double) {
        when (dir) {
            Direction.FORWARD -> { setPowers(power, power) }
            Direction.BACKWARD -> { setPowers(-power, -power) }
            Direction.SPIN_CW -> { setPowers(-power, power) }
            Direction.SPIN_CCW -> { setPowers(power, -power) }
            Direction.FORWARD_CCW -> { setPowers(0.0, power) }
            Direction.FORWARD_CW -> { setPowers(power, 0.0) }
            Direction.BACKWARD_CCW -> { setPowers(0.0, -power) }
            Direction.BACKWARD_CW -> { setPowers(-power, 0.0) }
        }
    }

    fun drive(dir: Direction, dist: Double, timeout: Int = 10) {
        driveMotors.prepareEncoderDrive()

        val lTarget = lDrive.motor.currentPosition +
                dir.intRepr * Values.TICKS_PER_INCH_FORWARD * dist
        val rTarget = rDrive.motor.currentPosition +
                dir.intRepr * Values.TICKS_PER_INCH_FORWARD * dist
        val minError = 50

        val lPID = PIDController(PIDConstants(0.0, 0.0, 0.0), lTarget)
        val rPID = PIDController(PIDConstants(0.0, 0.0, 0.0), rTarget)

        lPID.initController(lDrive.motor.currentPosition.toDouble())
        rPID.initController(rDrive.motor.currentPosition.toDouble())

        while (opMode.opModeIsActive() && !opMode.isStopRequested &&
                lPID.prevError!! > minError && rPID.prevError!! > minError) {
            setPowers(
                    lPID.output(lDrive.motor.currentPosition.toDouble()),
                    rPID.output(rDrive.motor.currentPosition.toDouble())
            )
        }

        stopAll()
    }

    fun rotate(dir: Direction, angle: Int, timeout: Int = 10,broadcast:Boolean=false,pidConstants: PIDConstants) {
        val targetHeading = fixAngle(imu.angle + dir.intRepr * angle)
        rotateTo(targetHeading, timeout,broadcast=broadcast,pidConstants = pidConstants)
    }

    fun fixAngle(angle: Double): Double {
        var fixedAngle = angle
        while (fixedAngle > 180 || fixedAngle < -180) {
            if (fixedAngle > 180) {
                fixedAngle -= 360
            } else {
                fixedAngle += 360
            }
        }
        return fixedAngle
    }

    fun rotateTo(targetHeading: Double, timeout: Int = 10,broadcast:Boolean=false,pidConstants: PIDConstants) {
        val minError = 2
        val minPower = 2.0

        val pid = PIDController(pidConstants, targetHeading,broadcast=broadcast,wss=wss)

        var currentHeading: Double = imu.angle

        pid.initController(currentHeading)
        do {
            currentHeading = imu.getAngle()

            var proportionalPower = pid.output(currentHeading, this::fixAngle)

//            proportionalPower = if (proportionalPower > 0) {
//                Math.max(proportionalPower + 0.1, minPower)
//            } else {
//                Math.min(proportionalPower - 0.1, -minPower)
//            }

            l.logData("P Power",proportionalPower)
            move(Direction.SPIN_CW, proportionalPower)

            if (Math.abs(pid.prevError!!) < minError) {
                l.log("Within minError! Waiting...")
                stopAll()
                opMode.sleep(300)
                currentHeading = imu.getAngle()
                pid.output(currentHeading, this::fixAngle)
            }
        } while (opMode.opModeIsActive() && !opMode.isStopRequested &&
                Math.abs(pid.prevError!!) > minError)
        stopAll()
    }

}