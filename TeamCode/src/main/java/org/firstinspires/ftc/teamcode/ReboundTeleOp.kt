package org.firstinspires.ftc.teamcode

import android.media.MediaPlayer
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor

@TeleOp(name = "Rebound TeleOp")
//@Disabled
class ReboundTeleOp : OpMode() {

    private lateinit var drivetrain: Rebound
    private lateinit var mediaPlayer: MediaPlayer

    override fun init() {
        // setup
        val lf: DcMotor = hardwareMap.dcMotor.get("lf")
        val lb: DcMotor = hardwareMap.dcMotor.get("lb")
        val rf: DcMotor = hardwareMap.dcMotor.get("rf")
        val rb: DcMotor = hardwareMap.dcMotor.get("rb")
        val imu: BNO055IMU = hardwareMap.get(BNO055IMU::class.java, "imu")
        drivetrain = Rebound(telemetry, hardwareMap, lf, lb, rf, rb, imu)
        drivetrain.initialize()
        telemetry.addData("Initial Orientation: ", drivetrain.getOrientation())
        mediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.up_and_away)
    }

    override fun loop() {
        val orientation = drivetrain.getOrientation()
        telemetry.addData("Orientation: ", orientation)
        when {
            gamepad1.a -> mediaPlayer.start()
            gamepad1.b -> mediaPlayer.stop()
            //gamepad1.left_trigger > 0 -> drivetrain.arcadeMecanum((-gamepad1.left_trigger).toDouble(), 0.0, 0.0)
            //gamepad1.right_trigger > 0 -> drivetrain.arcadeMecanum(gamepad1.right_trigger.toDouble(), 0.0, 0.0)
            else -> drivetrain.tankMecanum(gamepad1)
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        super.stop()
    }
}