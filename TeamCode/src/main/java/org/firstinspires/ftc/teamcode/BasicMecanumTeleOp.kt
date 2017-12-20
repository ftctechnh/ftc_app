package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@TeleOp(name = "Basic Mecanum Drivetrain", group = "Rebound Development")
//@Disabled
class BasicMecanumTeleOp : OpMode() {

    private lateinit var lf : DcMotorEx
    private lateinit var lb : DcMotorEx
    private lateinit var rf : DcMotorEx
    private lateinit var rb : DcMotorEx

    /**
     * Sets up the motors in a 4-wheel-drive Mecanum drivetrain.
     */
    private fun initializeMotors() {
        // bring the motors in from the configuration
        lf = hardwareMap.get(DcMotorEx::class.java, "lf")
        lb = hardwareMap.get(DcMotorEx::class.java, "lb")
        rf = hardwareMap.get(DcMotorEx::class.java, "rf")
        rb = hardwareMap.get(DcMotorEx::class.java, "rb")
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
        lf.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lb.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rf.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rb.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    override fun init() {
        // motor setup
        initializeMotors()
        if (lf.isMotorEnabled && lb.isMotorEnabled && rf.isMotorEnabled && rb.isMotorEnabled) {
            telemetry.addLine("Motors Initialized! Ready to drive.")
        } else {
            telemetry.addLine("ERROR: Motors not initialized successfully, shutting down.")
            telemetry.update()
            requestOpModeStop()
        }
    }

    override fun loop() {
        /*val gRotPower : Double = Math.pow(gamepad1.right_stick_x.toDouble(), 3.0)
        val gLinearPower : Double = Math.pow(Math.sqrt(Math.pow(gamepad1.left_stick_x.toDouble(), 2.0) +
                Math.pow(gamepad1.left_stick_y.toDouble(), 2.0)), 3.0)
        val gLinearDirection : Double = Math.toDegrees(Math.atan2(-Math.pow(gamepad1.left_stick_y.toDouble(), 3.0),
                Math.pow(gamepad1.right_stick_x.toDouble(), 3.0)));
        val mecanumPowers : List<Double> = mecanumDrive2(gLinearPower, gLinearDirection, gRotPower)*/
        val mecanumPowers : List<Double> = mecanumDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 0.5)
        telemetry.addData("LF: ", mecanumPowers[0])
        lf.power = mecanumPowers[0]
        telemetry.addData("RF: ", mecanumPowers[1])
        rf.power = mecanumPowers[1]
        telemetry.addData("LB: ", mecanumPowers[2])
        lb.power = mecanumPowers[2]
        telemetry.addData("RB: ", mecanumPowers[3])
        rb.power = mecanumPowers[3]
    }

    /**
     * Checks if a double is within a given range, and if it isn't, clamps it to that range.
     * By the wa there MUST be a more Kotlin-like way of doing this, but it's almost midnight.
     * @param n the number to clamp
     * @param low the lowest that nunmber can be
     * @param high the highest thaat number can be
     * @return a double clamped to the given range
     */
    private fun clampDouble (n : Double, low : Double, high : Double) : Double = max(low, min(n, high))

    /**
     * Creates the motor power numbers for a Mecanum drivetrain using vector adding.
     * @param x1 generally the x axis of the left joystick
     * @param y1 generally the y axis of the left joystick
     * @param x2 generally the x axis of the right joystick
     * @param tuner a tuning constant for the rotation of the robot. DO NOT SET HIGHER THAN 1.
     * @return a Double List [lf, rf, lb, rb] containing powers for the four motors.
     */
    private fun mecanumDrive(x1: Float, y1: Float, x2: Float, tuner: Double) : List<Double> {
        // set up the inputs for generating motor powers
        val forward : Double = -(y1.toDouble())
        val right : Double = x1.toDouble()
        val clockwise : Double = tuner * x2.toDouble()

        // generate motor powers - these numbers have been tested in python
        val lf : Double = forward + clockwise + right
        val rf : Double = -(forward - clockwise - right)
        val lb : Double = -(forward + clockwise - right)
        val rb : Double = forward - clockwise + right
        val powers : List<Double> = listOf(lf, rf, lb, rb)
        // clip the motor values, beacause in some cases they do go over 1 or under -1
        // note: this is NOT a problem with the algorithm - it's just how vector adding works

        return powers.map {x -> clampDouble(x, low = -1.0, high = 1.0)} // finally, return the numbers in a nice list
    }

    @SuppressWarnings("unused")
    private fun mecanumDrive2(linearPower : Double, linearDirection : Double, rotPower : Double) : List<Double> {
        // Limit sum of rotational and translational powers to 1
        val processedRotPower: Double =
                if (linearPower + abs(rotPower) > 1.0) (Math.signum(rotPower)*(abs(rotPower)-linearPower)) else rotPower
        // take apart the angle unit vector
        val s : Double = Math.sin(Math.toRadians(linearDirection))
        val c : Double = Math.cos(Math.toRadians(linearDirection))
        // find non-normalized linear powers
        var d1 : Double = s + c
        var d2 : Double = s - c
        // normalize the linear powers
        if (max(abs(d1), abs(d2)) != 0.0) {
            d1 *= 1 / max(abs(d1), abs(d2))
            d2 *= 1 / max(abs(d1), abs(d2))
        }
        // scalar multiply the linear powers
        d1 *= linearPower
        d2 *= linearPower
        // set up the motor powers as immutables
        val lf : Double = d1+processedRotPower
        val rf : Double = d2+processedRotPower
        val lb : Double = d2+processedRotPower
        val rb : Double = -d1+processedRotPower
        // and return them in a list, clipped to prevent errors
        return listOf(lf, rf, lb, rb).map {x -> clampDouble(x, low = -1.0, high = 1.0)}
    }
}