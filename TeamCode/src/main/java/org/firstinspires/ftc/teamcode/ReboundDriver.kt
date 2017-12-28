package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Provides a codebase for use of the Rebound drivetrain.
 */
class ReboundDriver(private val telemetry: Telemetry, private val hardwareMap: HardwareMap, private var lf: DcMotor,
                    private var lb: DcMotor, private var rf: DcMotor, private var rb: DcMotor,
                    private var imu: BNO055IMU)
{
    /*
    fun clamp(n: Double, min: Double, max: Double) : Double {
        if (n in min..max) {

        } else
    }*/

    fun initialize() {
        // bring the motors in from the configuration
        lf = hardwareMap.get(DcMotor::class.java, "lf")
        lb = hardwareMap.get(DcMotor::class.java, "lb")
        rf = hardwareMap.get(DcMotor::class.java, "rf")
        rb = hardwareMap.get(DcMotor::class.java, "rb")
        // compensate for motor mounting
        lf.direction = DcMotorSimple.Direction.REVERSE
        lb.direction = DcMotorSimple.Direction.REVERSE
        rf.direction = DcMotorSimple.Direction.FORWARD
        rb.direction = DcMotorSimple.Direction.FORWARD
        // make the motors stop hard
        lf.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lb.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rf.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rb.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        // run the motors using encoders
        setMotorModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        // IMU initialization code
        val parameters : BNO055IMU.Parameters = BNO055IMU.Parameters()
        parameters.mode = BNO055IMU.SensorMode.IMU
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile = "BNO055IMUCalibration.json" // see the calibration sample opmode
        parameters.loggingEnabled = true
        parameters.loggingTag = "IMU"
        parameters.accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        // initialize the IMU
        imu = hardwareMap.get(BNO055IMU::class.java, "imu")
        // apply IMU configuration
        imu.initialize(parameters)
        // basic IMU tests
        if (imu.isGyroCalibrated) {
            telemetry.addLine("IMU Gyro is calibrated. AngleSnap should work properly!")
        }
    }

    fun setMotorPowers(lfPow: Double, rfPow: Double, lbPow: Double, rbPow: Double) {
        lf.power = lfPow
        rf.power = rfPow
        lb.power = lbPow
        rb.power = rbPow
    }

    fun getRobotOrientation() =
            imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle

    fun getEncoderPosition() = listOf(lf.currentPosition, lb.currentPosition, rf.currentPosition, rb.currentPosition)

    fun setTargetPositions(target: Int) {
        lf.targetPosition = target
        lb.targetPosition = target
        rf.targetPosition = target
        rb.targetPosition = target
    }

    fun resetEncoders(mode: DcMotor.RunMode = DcMotor.RunMode.RUN_USING_ENCODER) {
        setMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        setMotorModes(mode)
    }

    fun setMotorModes(mode: DcMotor.RunMode) {
        lf.mode = mode
        lb.mode = mode
        rf.mode = mode
        rb.mode = mode
    }

    fun inverseKinematicsMecanum(vtX: Double, vtY: Double, vR: Double) {
        telemetry.addData("Inputs: ", listOf(vtX, vtY, vR))
        val rfPow : Double = vtY - vtX + vR
        val lfPow : Double = vtY + vtX - vR
        val lbPow : Double = vtY - vtX - vR
        val rbPow : Double = -(vtY + vtX + vR)
        // find maximum for scale factor
        //val maxWheelSpeed : Double = max(rfPow, max(lfPow, max(lbPow, rbPow)))
        //telemetry.addData("Max: ", maxWheelSpeed)
        val scale : Double = (abs(vtX) + abs(vtY) + abs(vR))
        telemetry.addData("Scale: ", scale)
        // return a scaled list
        val powers: List<Double> = listOf(lfPow, rfPow, lbPow, rbPow).map {x -> x/scale}
        /*
        for (i in powers) {
            if (powers[i] < - 1) { powers [i] = -1;}
            if (powers[i] > 1) {powers [i] = 1;}
        }*/
        telemetry.addData("Powers: ", powers)
        lf.power = powers[0]
        rf.power = powers[1]
        lb.power = powers[2]
        rb.power = powers[3]
    }

    fun simplisticMecanum(left_x: Double, left_y: Double, right_x: Double) {
        val vD : Double = Math.sqrt(Math.pow(left_x, 2.0)
                + Math.pow(left_y, 2.0))
        val thetaD : Double = Math.atan2(-left_x, left_y)
        val vTheta : Double = Math.pow(right_x, 3.0)/(1+vD)
        telemetry.addData("Inputs: ", listOf(vD, thetaD, vTheta))

        val lfPow : Double = vD * sin(-thetaD + Math.PI / 4) - vTheta
        val rfPow : Double = vD * cos(-thetaD + Math.PI / 4) + vTheta
        val lbPow : Double = vD * cos(-thetaD + Math.PI / 4) - vTheta
        val rbPow : Double = -(vD * sin(-thetaD + Math.PI / 4) + vTheta)
        telemetry.addData("Pre-Scale: ", listOf(lfPow, rfPow, lbPow, rbPow).map{x -> x.roundTo3DecimalPlaces()})
        Range.scale(lfPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rfPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(lbPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rbPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        telemetry.addData("Scaled: ", listOf(lfPow, rfPow, lbPow, rbPow).map{x -> x.roundTo3DecimalPlaces()})
        lf.power = lfPow
        rf.power = rfPow
        lb.power = lbPow
        rb.power = rbPow
    }

    fun stopMoving() {
        lf.power = 0.0
        lb.power = 0.0
        rf.power = 0.0
        rb.power = 0.0
    }

    fun Double.roundTo3DecimalPlaces() =
            BigDecimal(this).setScale(3, BigDecimal.ROUND_HALF_UP).toDouble()

}