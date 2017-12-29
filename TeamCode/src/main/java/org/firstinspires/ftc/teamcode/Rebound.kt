package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import kotlin.math.*

/**
 * Provides a codebase for use of the Rebound drivetrain.
 * @param telemetry from the opmode
 * @param hardwareMap from the opmode
 * @param lf left front motor
 * @param lb left back motor
 * @param rf right front motor
 * @param rb right back motor
 * @param imu a BNO055 IMU
 */
class Rebound(private val telemetry: Telemetry, private val hardwareMap: HardwareMap,
              private var lf: DcMotor, private var lb: DcMotor, private var rf: DcMotor, private var rb: DcMotor,
              private var imu: BNO055IMU)
{
    /**
     * Sets all the hardware devices up. Assumes motors are named "lf", "lb", "rf", and "rb".
     */
    fun initialize(motorMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER,
                   zeroPowerAction: DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE) {
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
        setZeroPowerBehaviors(zeroPowerAction)
        // run the motors using encoders
        setMotorModes(motorMode)

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

    /**
     * Get the powers of the motors as a list in the order [lf, rf, lb, rb].
     * @return a List of Doubles containing motor powers in the order described above.
     */
    fun getMotorPowers() : List<Double> = listOf(lf.power, rf.power, lb.power, rb.power)

    /**
     * Sets the motor powers based on the parameters.
     * @param lfPow left front power
     * @param lbPow left back power
     * @param rfPow right front power
     * @param rbPow right back power
     */
    fun setMotorPowers(lfPow: Double, rfPow: Double, lbPow: Double, rbPow: Double) {
        lf.power = lfPow
        rf.power = rfPow
        lb.power = lbPow
        rb.power = rbPow
    }
    fun setMotorPowers(lPow: Double, rPow: Double) = setMotorPowers(lPow, lPow, rPow, rPow)
    fun setMotorPowers(power: Double) = setMotorPowers(power, power)

    fun getMotorModes() : List<DcMotor.RunMode> = listOf<DcMotor.RunMode>(lf.mode, rf.mode, lb.mode, rb.mode)

    /**
     * Get the z-axis orientation from the IMU.
     */
    fun getOrientation() =
            imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle

    /**
     * Gets the encoder positions of the motors as a list in the order [lf, rf, lb, rb].
     * @returns a List of Doubles containing the encoder positions of the motors.
     */
    fun getEncoderPositions() = listOf(lf.currentPosition, rf.currentPosition, lb.currentPosition, rb.currentPosition)

    /**
     * Sets the target positions of eacch motor.
     * @param lfTarget left front motor target position
     * @param rfTarget right front motor target position
     * @param lbTarget left back motor target position
     * @param rbTarget right back motor target position
     */
    fun setTargetPositions(lfTarget: Int, lbTarget: Int, rfTarget: Int, rbTarget: Int) {
        if (getMotorModes().first() != DcMotor.RunMode.RUN_TO_POSITION) {setMotorModes(DcMotor.RunMode.RUN_TO_POSITION)}
        lf.targetPosition = lfTarget
        lb.targetPosition = lbTarget
        rf.targetPosition = rfTarget
        rb.targetPosition = rbTarget
    }
    /**
     * Sets the target positions for each side of the robot - useful for performing encoder turns.
     * @param lTarget left side motor target positions
     * @param rTarget right side motor target positions
     */
    fun setTargetPositions(lTarget: Int, rTarget: Int) = setTargetPositions(lTarget, lTarget, rTarget, rTarget)
    /**
     * Sets the target positions for all motors at once.
     * @param target the target position for all motors
     */
    fun setTargetPositions(target: Int) = setTargetPositions(target, target)

    /**
     * Resets the encoders, then sets the motors to the given mode.
     * @param mode the mode to set for the motors after the encoders have been reset
     */
    fun resetEncoders(mode: DcMotor.RunMode = DcMotor.RunMode.RUN_USING_ENCODER) {
        setMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
        setMotorModes(mode)
    }

    /**
     * Sets the motors to the given modes.
     * @param lfMode left front motor mode
     * @param rfMode right front motor mode
     * @param lbMode left back motor mode
     * @param rbMode right back motor mode
     */
    fun setMotorModes(lfMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER,
                      rfMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER,
                      lbMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER,
                      rbMode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
        lf.mode = lfMode
        rf.mode = rfMode
        lb.mode = lbMode
        rb.mode = rbMode
    }
    /**
     * Sets all motors to the given mode.
     * @see setMotorModes
     * @param mode the desired mode
     */
    fun setMotorModes(mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER) =
            setMotorModes(mode, mode, mode, mode)

    /**
     * Sets the behavior of the motors at power 0.
     * @param behavior either BRAKE or FLOAT
     */
    fun setZeroPowerBehaviors(behavior: DcMotor.ZeroPowerBehavior) {
        lf.zeroPowerBehavior = behavior
        lb.zeroPowerBehavior = behavior
        rf.zeroPowerBehavior = behavior
        rb.zeroPowerBehavior = behavior
    }

    /**
     * Runs the motors based on an Inverse Kinematics equation that takes input from 2 joysticks.
     * @
     *
     */
    @Deprecated(message = "No longer in use")
    fun inverseKinematicsMecanum(vtX: Double, vtY: Double, vR: Double) {
        telemetry.addData("Inputs: ", listOf(vtX, vtY, vR))
        val rfPow : Double = vtY - vtX + vR
        val lfPow : Double = vtY + vtX - vR
        val lbPow : Double = vtY - vtX - vR
        val rbPow : Double = -(vtY + vtX + vR)
        // find scale factor
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

    /**
     * Sets motor powers based on the Simplistic Mecanum algorithm, designed for input from two Joysticks.
     * @param left_x the x-axis of the left joystick
     * @param left_y the y axis of the left joystick
     * @param right_x the x axis of the right joystick
     */
    fun arcadeMecanum(left_x: Double, left_y: Double, right_x: Double) {
        val vD : Double = sqrt(Math.pow(left_x, 2.0)
                + Math.pow(left_y, 2.0))
        val thetaD : Double = atan2(-left_x, left_y)
        val vTheta : Double = Math.pow(right_x, 3.0)/(1.5)
        telemetry.addData("Inputs: ", listOf(vD, thetaD, vTheta))

        val lfPow : Double = vD * sin(-thetaD + Math.PI / 4) - vTheta
        val rfPow : Double = vD * cos(-thetaD + Math.PI / 4) + vTheta
        val lbPow : Double = vD * cos(-thetaD + Math.PI / 4) - vTheta
        val rbPow : Double = -(vD * sin(-thetaD + Math.PI / 4) + vTheta)
        telemetry.addData("Pre-Scale: ", listOf(lfPow, rfPow, lbPow, rbPow))
        Range.scale(lfPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rfPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(lbPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rbPow, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        telemetry.addData("Scaled: ", listOf(lfPow, rfPow, lbPow, rbPow))
        lf.power = lfPow
        rf.power = rfPow
        lb.power = lbPow
        rb.power = rbPow
    }
    /**
     * Sets motor powers based on the Simplistic Mecanum algorithm, using input from a Gamepad.
     * @param gamepad the gamepad used to drive
     */
    fun arcadeMecanum(gamepad: Gamepad) =
            arcadeMecanum(Math.pow(gamepad.left_stick_x.toDouble(), 3.0),
                    Math.pow(gamepad.left_stick_y.toDouble(), 3.0),
                    Math.pow(gamepad.right_stick_x.toDouble(), 3.0))

    fun tankMecanum(left_y: Double, right_y: Double,
                    left_trigger: Double, right_trigger: Double) {
        val yF : Double = (left_y + right_y) / 2
        val yT : Double = (left_y - right_y) / 2
        val vX : Double = right_trigger - left_trigger

        val kT = 0.5 // turn multiplier

        val lfPow : Double = yF + yT + (kT*vX)
        val rfPow : Double = yF - yT - (kT*vX)
        val lbPow : Double = yF + yT - (kT*vX)
        val rbPow : Double = -(yF - yT + (kT*vX))

        val wMax : Double = max(lfPow, max(rfPow, max(lbPow, rbPow)))
        val powers = listOf(lfPow, rfPow, lbPow, rbPow).map { x -> x/max(1.0, abs(wMax))}

        lf.power = powers[0]
        rf.power = powers[1]
        lb.power = powers[2]
        rb.power = powers[3]
    }
    fun tankMecanum(gamepad: Gamepad) = tankMecanum(Math.pow(gamepad.left_stick_y.toDouble(), 3.0),
            Math.pow(gamepad.right_stick_y.toDouble(), 3.0),
            Math.pow(gamepad.left_trigger.toDouble(), 3.0),
            Math.pow(gamepad.right_trigger.toDouble(), 3.0))

    /**
     * Checks if motors are busy.
     * @return TRUE if ALL motors is busy, FALSE if any
     */
    fun areMotorsBusy() : Boolean = lf.isBusy || rf.isBusy || lb.isBusy || rb.isBusy

    /**
     * Stops the motors immediately. No questions asked.
     */
    fun stopMoving() {
        lf.power = 0.0
        lb.power = 0.0
        rf.power = 0.0
        rb.power = 0.0
    }
    /*
    private fun Double.roundTo3DecimalPlaces() =
            BigDecimal(this).setScale(3, BigDecimal.ROUND_HALF_UP).toDouble()*/

}