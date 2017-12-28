package org.firstinspires.ftc.teamcode

import android.media.MediaPlayer
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

@TeleOp(name = "Basic Mecanum Drivetrain")
//@Disabled
class BasicMecanumTeleOp : OpMode() {

    /**
     * Sets up the motors in a 4-wheel-drive Mecanum drivetrain.
     */
    private lateinit var drivetrain: ReboundDriver
    lateinit var mediaPlayer: MediaPlayer

    override fun init() {
        // setup
        val lf : DcMotor = hardwareMap.dcMotor.get("lf")
        val lb : DcMotor = hardwareMap.dcMotor.get("lb")
        val rf : DcMotor = hardwareMap.dcMotor.get("rf")
        val rb : DcMotor = hardwareMap.dcMotor.get("rb")
        val imu : BNO055IMU = hardwareMap.get(BNO055IMU::class.java, "imu")
        drivetrain = ReboundDriver(telemetry, hardwareMap, lf, lb, rf, rb, imu)
        drivetrain.initialize()
        telemetry.addData("Initial Orientation: ", drivetrain.getRobotOrientation())
        mediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.up_and_away)
    }

    override fun loop() {
        val orientation = drivetrain.getRobotOrientation()
        telemetry.addData("Orientation: ", orientation)
        when {
            gamepad1.x -> turnTo(90)
            //gamepad1.a -> mediaPlayer.start()
            gamepad1.left_trigger > 0 -> drivetrain.simplisticMecanum((-gamepad1.left_trigger).toDouble(), 0.0, 0.0)
            gamepad1.right_trigger > 0 -> drivetrain.simplisticMecanum(gamepad1.right_trigger.toDouble(), 0.0, 0.0)
            else -> drivetrain.simplisticMecanum(Math.pow(((-gamepad1.left_stick_x).toDouble()), 3.0),
                    Math.pow(gamepad1.left_stick_y.toDouble(), 3.0), Math.pow(gamepad1.right_stick_x.toDouble(), 3.0))
        }
        telemetry.addData("Position: ", drivetrain.getEncoderPosition())
    }

    private fun turnTo(degrees: Int) {
        while (drivetrain.getRobotOrientation().toInt() < degrees) {
            drivetrain.inverseKinematicsMecanum(0.0, 0.0, 0.1)
        }
        drivetrain.inverseKinematicsMecanum(0.0, 0.0, 0.0)
    }
/*
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
    }*/
}