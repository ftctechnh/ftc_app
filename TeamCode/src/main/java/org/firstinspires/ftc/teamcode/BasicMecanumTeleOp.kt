package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import kotlin.math.*

@TeleOp(name = "Basic Mecanum Drivetrain")
//@Disabled
class BasicMecanumTeleOp : OpMode() {

    private lateinit var lf : DcMotor
    private lateinit var lb : DcMotor
    private lateinit var rf : DcMotor
    private lateinit var rb : DcMotor
    private lateinit var imu : BNO055IMU

    /**
     * Sets up the motors in a 4-wheel-drive Mecanum drivetrain.
     */
    private fun initializeMotors() {
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
        lf.mode = DcMotor.RunMode.RUN_USING_ENCODER
        lb.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rf.mode = DcMotor.RunMode.RUN_USING_ENCODER
        rb.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    override fun init() {
        // motor setup
        initializeMotors()
        if (lf.deviceName != null && lb.deviceName != null && rf.deviceName != null && rb.deviceName != null) {
            telemetry.addLine("Motors Initialized! Ready to drive.")
        } else {
            telemetry.addLine("ERROR: Motors not initialized successfully, shutting down.")
            telemetry.update()
            requestOpModeStop()
        }
        // define IMU config
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
        val orientation = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES)
        telemetry.addData("Initial Orientation: ", orientation.firstAngle)
    }

    override fun loop() {
        val orientation = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES)
        if (gamepad1.x) {
            if (orientation.firstAngle != 90F) {
                inverseKinematicsMecanum(0.0, 0.0, 0.5)
            }
        } else {
            val mecanumPowers : List<Double> = inverseKinematicsMecanum((-gamepad1.left_stick_x).toDouble(),
                    gamepad1.left_stick_y.toDouble(), gamepad1.right_stick_x.toDouble())
            telemetry.addData("LF: ", mecanumPowers[0])
            lf.power = mecanumPowers[0]
            telemetry.addData("RF: ", mecanumPowers[1])
            rf.power = mecanumPowers[1]
            telemetry.addData("LB: ", mecanumPowers[2])
            lb.power = mecanumPowers[2]
            telemetry.addData("RB: ", mecanumPowers[3])
            rb.power = mecanumPowers[3]
        }
        telemetry.addData("Angle: ", orientation.firstAngle)
    }

    /**
     * Creates the motor power numbers for a Mecanum drivetrain using vector adding.
     * @param x1 generally the x axis of the left joystick
     * @param y1 generally the y axis of the left joystick
     * @param x2 generally the x axis of the right joystick
     * @param tuner a tuning constant for the rotation of the robot. DO NOT SET HIGHER THAN 1.
     * @return a Double List [lf, rf, lb, rb] containing powers for the four motors.
     */
    @Deprecated(level = DeprecationLevel.ERROR, message = "Not working")
    private fun mecanumDrive(x1: Float, y1: Float, x2: Float, tuner: Double) : List<Double> {
        // set up the inputs for generating motor powers
        val forward : Double = -(y1.toDouble())
        val right : Double = x1.toDouble()
        val clockwise : Double = tuner * x2.toDouble()

        // generate motor powers - this is where the problems are coming up
        val lf : Double = -(forward + clockwise + right)
        val rf : Double = -(forward - clockwise - right)
        val lb : Double = -(forward + clockwise - right)
        val rb : Double = forward - clockwise + right
        val powers : List<Double> = listOf(lf, rf, lb, rb)
        // clip the motor values, beacause in some cases they do go over 1 or under -1
        // note: this is NOT a problem with the algorithm - it's just how vector adding works

        return powers.map {x -> max(a = -1.0, b = min(x, 1.0)) } // finally, return the numbers in a nice list
    }

    /**
     * Generates numbers for mecanum drive from the joysticks on a Gamepad. Based on this paper from WPI Think Tank,
     * and code from pmtischler/ftc-app.
     * @param gamepad a Gamepad
     * @return a list of Doubles that are the motor powers in the order: lf, rf, lb, rb
     * @see <a href="https://github.com/pmtischler/ftc_app">pmtischler/ftc-app</a>
     * @see <a href="http://thinktank.wpi.edu/resources/346/ControllingMecanumDrive.pdf">and this WPI paper on controlling Mecanum wheels</a>
     */
    @Deprecated(message = "Not in use")
    private fun simplisticMecanum(gamepad: Gamepad) : List<Double> {
        val vD : Double = Math.sqrt(Math.pow(gamepad.left_stick_x.toDouble(), 2.0)
                + Math.pow(gamepad.left_stick_y.toDouble(), 2.0))
        val thetaD : Double = Math.atan2(-gamepad.left_stick_x.toDouble(), gamepad.left_stick_y.toDouble())
        val vTheta : Double = gamepad.right_stick_x.toDouble()

        val lf : Double = vD * sin(-thetaD + Math.PI / 4) - vTheta
        val rf : Double = vD * cos(-thetaD + Math.PI / 4) + vTheta
        val lb : Double = vD * cos(-thetaD + Math.PI / 4) - vTheta
        val rb : Double = -(vD * sin(-thetaD + Math.PI / 4) + vTheta)
        Range.scale(lf, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rf, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(lb, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rb, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        return listOf(lf, rf, lb, rb)
    }
    @Deprecated(message = "Not in use")
    private fun simplisticMecanum(left_x: Float, left_y: Float, right_x: Float) : List<Double> {
        val vD : Double = Math.sqrt(Math.pow(left_x.toDouble(), 2.0)
                + Math.pow(left_y.toDouble(), 2.0))
        val thetaD : Double = Math.atan2(-left_x.toDouble(), left_y.toDouble())
        val vTheta : Double = right_x.toDouble()

        val lf : Double = vD * sin(-thetaD + Math.PI / 4) - vTheta
        val rf : Double = vD * cos(-thetaD + Math.PI / 4) + vTheta
        val lb : Double = vD * cos(-thetaD + Math.PI / 4) - vTheta
        val rb : Double = -(vD * sin(-thetaD + Math.PI / 4) + vTheta)
        Range.scale(lf, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rf, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(lb, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        Range.scale(rb, (-2).toDouble(), 2.0, (-1).toDouble(), 1.0)
        return listOf(lf, rf, lb, rb)
    }
    /*
    private fun inverseKinematicsMecanum(gamepad: Gamepad) : List<Double> {
        val vtX : Double = -gamepad.left_stick_x.toDouble()
        val vtY : Double = gamepad.left_stick_y.toDouble()
        val vR : Double =  gamepad.right_stick_x.toDouble()
        // inverse kinematics to find wheel powers
        val rf : Double = vtY - vtX + vR
        telemetry.addData("Desired RF: ", rf)
        val lf : Double = vtY + vtX - vR
        telemetry.addData("Desired LF: ", lf)
        val lb : Double = vtY - vtX - vR
        telemetry.addData("Desired LB: ", lb)
        val rb : Double = -(vtY + vtX + vR)
        telemetry.addData("Desired RB: ", rb)
        // find maximum for scale factor
        // val maxWheelSpeed : Double = max(rf, max(lf, max(lb, rb)))
        // return a scaled list
        return listOf(lf, rf, lb, rb) //.map {x -> maxWheelSpeed/x}
    }*/

    private fun inverseKinematicsMecanum(vtX: Double, vtY: Double, vR: Double): List<Double> {
        val rf : Double = vtY - vtX + vR
        telemetry.addData("Desired RF: ", rf)
        val lf : Double = vtY + vtX - vR
        telemetry.addData("Desired LF: ", lf)
        val lb : Double = vtY - vtX - vR
        telemetry.addData("Desired LB: ", lb)
        val rb : Double = -(vtY + vtX + vR)
        telemetry.addData("Desired RB: ", rb)
        // find maximum for scale factor
        // val maxWheelSpeed : Double = max(rf, max(lf, max(lb, rb)))
        // return a scaled list
        return listOf(lf, rf, lb, rb) //.map {x -> maxWheelSpeed/x}
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Not working")
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
        return listOf(lf, rf, lb, rb).map {x -> max(a = -1.0, b = min(x, 1.0)) }
    }
}