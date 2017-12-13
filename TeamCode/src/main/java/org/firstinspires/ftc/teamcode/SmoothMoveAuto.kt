package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.robotcore.external.navigation.*

@Autonomous(name = "Smooth Rotate")
class SmoothMoveAuto : LinearOpMode() {

    private lateinit var lf : DcMotorEx
    private lateinit var lb : DcMotorEx
    private lateinit var rf : DcMotorEx
    private lateinit var rb : DcMotorEx
    private fun initializeMotors() {
        lf = hardwareMap.get(DcMotorEx::class.java, "lf")
        lb = hardwareMap.get(DcMotorEx::class.java, "lb")
        rf = hardwareMap.get(DcMotorEx::class.java, "rf")
        rb = hardwareMap.get(DcMotorEx::class.java, "lb")
        // compensate for motor mounting
        lf.direction = DcMotorSimple.Direction.FORWARD
        lb.direction = DcMotorSimple.Direction.FORWARD
        rf.direction = DcMotorSimple.Direction.REVERSE
        rb.direction = DcMotorSimple.Direction.REVERSE
        // make the motors stop hard
        lf.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lb.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rf.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rb.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    private lateinit var imu: BNO055IMU
    private fun initializeImu() {
        imu = hardwareMap.get<BNO055IMU>(BNO055IMU::class.java, "imu")
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.calibrationDataFile = "BNO055IMUCalibration.json" // see the calibration sample opmode
        parameters.loggingEnabled = true
        parameters.loggingTag = "IMU"
        parameters.mode = BNO055IMU.SensorMode.GYRONLY
        imu.initialize(parameters)
    }
    private var angles : Orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES)

    // stops the robot immediately
    private fun stopRobot() {
        lf.power = 0.0
        lb.power = 0.0
        rf.power = 0.0
        rb.power = 0.0
    }
    // turns whole the degrees are
    private fun turn(degrees : Float) {
        when {
            degrees > angles.firstAngle -> {
                while (degrees > angles.firstAngle) {
                    lf.power = 0.5
                    lb.power = 0.5
                    rf.power = -0.5
                    rb.power = -0.5
                }
                stopRobot()
            }
            degrees == angles.firstAngle -> stopRobot()
            else -> {
                while (degrees < angles.firstAngle) {
                    lf.power = -0.5
                    lb.power = -0.5
                    rf.power = 0.5
                    rb.power = 0.5
                }
                stopRobot()
            }
        }
    }
    // Main OpMode method
    override fun runOpMode() {
        initializeImu()
        initializeMotors()

        waitForStart()

        imu.startAccelerationIntegration(Position(), Velocity(), 1000)
        turn(90F)
        sleep(1000)
        turn(-180F)
        sleep(1000)
        turn(-90F)
    }
}