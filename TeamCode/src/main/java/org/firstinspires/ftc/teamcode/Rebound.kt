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
    private val ENCODER_COUNTS_PER_ROTATION : Double = 1120.0
    private val WHEEL_CIRCUMFERENCE_INCHES : Double = 4.0 * PI
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
        with(parameters) {
            mode = BNO055IMU.SensorMode.IMU
            angleUnit = BNO055IMU.AngleUnit.DEGREES
            accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
            calibrationDataFile = "BNO055IMUCalibration.json" // see the calibration sample opmode
            loggingEnabled = true
            loggingTag = "IMU"
            accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        }
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

    fun setTargetRotations(lfTarget: Double, rfTarget: Double, lbTarget: Double, rbTarget: Double) =
            setTargetPositions((lfTarget * ENCODER_COUNTS_PER_ROTATION).toInt(),
                (rfTarget * ENCODER_COUNTS_PER_ROTATION).toInt(),
                (lbTarget * ENCODER_COUNTS_PER_ROTATION).toInt(),
                (rbTarget * ENCODER_COUNTS_PER_ROTATION).toInt())
    fun setTargetRotations(lTarget: Double, rTarget: Double) = setTargetRotations(lTarget, rTarget, lTarget, rTarget)
    fun setTargetRotations(target: Double) = setTargetRotations(target, target)
    /**
     * Set a strafe target in rotations.
     * @param lCfgTarget controls Left Configuration wheels (LF and RB).
     * @param rCfgTarget controls Right Configuration wheels (Rf and LB).
     */
    fun setTargetRotationStrafe(lCfgTarget: Double, rCfgTarget: Double) =
            setTargetRotations(lCfgTarget, rCfgTarget, rCfgTarget, lCfgTarget)

    fun setTargetTravelInches(lfTarget: Double, rfTarget: Double, lbTarget: Double, rbTarget: Double) =
            setTargetRotations(lfTarget * WHEEL_CIRCUMFERENCE_INCHES,
                    rfTarget * WHEEL_CIRCUMFERENCE_INCHES,
                    lbTarget * WHEEL_CIRCUMFERENCE_INCHES,
                    rbTarget * WHEEL_CIRCUMFERENCE_INCHES)
    fun setTargetTravelInches(lTarget: Double, rTarget: Double) =
            setTargetTravelInches(lTarget, rTarget, lTarget, rTarget)
    fun setTargetTravelInches(target: Double) = setTargetTravelInches(target, target)
    // about 2866 encoder counts per rotation for full rotation, or 716.5 in a 90 degree turn

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
     * Sets motor powers based on an Inverse Kinematics algorithm.
     * @param vtX translational power along the X axis (-1 to 1)
     * @param vtY translational power along the Y axis (-1 to 1)
     * @param vR rotational power around the Z axis (-1 to 1)
     */
    fun arcadeMecanum(vtX: Double, vtY: Double, vR: Double) {
        //telemetry.addData("Inputs: ", listOf(vtX, vtY, vR))
        // calculate raw motor powers
        val lfPow : Double = vtY + vtX - vR
        val rfPow : Double = vtY - vtX + vR
        val lbPow : Double = vtY - vtX - vR
        val rbPow : Double = -(vtY + vtX + vR)
        // get the max wheel power
        val wMax : Double = max(lfPow, max(rfPow, max(lbPow, rbPow)))
        //telemetry.addData("Max Speed: ", wMax)
        // scale the motor powers
        val powers: List<Double> = listOf(lfPow, rfPow, lbPow, rbPow).map {x -> x/max(1.0, wMax)}
        telemetry.addData("LF: ", powers[0])
                 .addData("RF: ", powers[1])
                 .addData("LB: ", powers[2])
                 .addData("RB: ", powers[3])
        // set the motor powers
        lf.power = powers[0]
        rf.power = powers[1]
        lb.power = powers[2]
        rb.power = powers[3]
    }
    /**
    * Sets motor powers based on an Inverse Kinematics algorithm using input from a Gamepad.
    * Two joysticks are used - the X and Y axes of the left joystick control the linear motion of the robot,
    * and the X axis of the right joystick controls rotational motion.
    * @param gamepad the gamepad used to drive
    */
    fun arcadeMecanum(gamepad: Gamepad) =
            arcadeMecanum(Math.pow(gamepad.left_stick_x.toDouble(), 3.0),
                    Math.pow(gamepad.left_stick_y.toDouble(), 3.0),
                    Math.pow(gamepad.right_stick_x.toDouble(), 3.0))

    /**
     * Sets motor powers based on an Inverse Kinematics algorithm designed to act similarly to a tank drive system
     * when used with a Gamepad.
     * @param left_y the left joystick Y axis
     * @param right_y the right joystick y axis
     * @param left_trigger the left trigger
     * @param right_trigger the right trigger
     * @param kT a turning multiplier, used to tune gamepad sensitivity
     */
    fun tankMecanum(left_y: Double, right_y: Double,
                    left_trigger: Double, right_trigger: Double, kT: Double = 0.5) {
        // set up movement variables
        val vtY : Double = (left_y + right_y) / 2
        val vR : Double = (left_y - right_y) / 2
        val vtX : Double = right_trigger - left_trigger
        // then plug those into arcadeMecanum() - we're done!
        arcadeMecanum(vtX = vtX, vtY = vtY, vR = (kT*vR))
    }
    /**
     * Sets motor powers based on an Inverse Kinematics algorithm using a Gamepad, designed to work like a tank drive.
     * The joysticks behave identically to a tank drive, and the triggers allow for strafing.
     * @param gamepad the gamepad used to drive
     */
    fun tankMecanum(gamepad: Gamepad) = tankMecanum(Math.pow(gamepad.left_stick_y.toDouble(), 3.0),
            Math.pow(gamepad.right_stick_y.toDouble(), 3.0),
            Math.pow(gamepad.left_trigger.toDouble(), 3.0),
            Math.pow(gamepad.right_trigger.toDouble(), 3.0))

    /**
     * Sets motor powers based on the Simplistic Mecanum algorithm from WPI Think Tank.
     * @param vtX translational power along the X axis (-1 to 1)
     * @param vtY translational power along the Y axis (-1 to 1)
     * @param vR rotational power around the Z axis (-1 to 1)
     */
    fun arcadeMecanum_WPI(vtX: Double, vtY: Double, vR: Double) {
        val vD : Double = sqrt(Math.pow(vtX, 2.0)
                + Math.pow(vtY, 2.0))
        val thetaD : Double = atan2(-vtX, vtY)
        val vTheta : Double = Math.pow(vR, 3.0)/(1.5)
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
     * Two joysticks are used - the X and Y axes of the left joystick control the linear motion of the robot,
     * and the X axis of the right joystick controls rotational motion.
     * @param gamepad the gamepad used to drive
     */
    fun arcadeMecanum_WPI(gamepad: Gamepad) =
            arcadeMecanum_WPI(Math.pow(gamepad.left_stick_x.toDouble(), 3.0),
                    Math.pow(gamepad.left_stick_y.toDouble(), 3.0),
                    Math.pow(gamepad.right_stick_x.toDouble(), 3.0))

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