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
    private lateinit var mediaPlayer: MediaPlayer // dedicated to Elliot Kahn of 8148 Aleph Bots
    private var tankStyleControl : Boolean = false

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
        telemetry.addData("Jordan Drive ", if(tankStyleControl) "Enabled." else "Disabled.")
        when {
            gamepad1.x -> mediaPlayer.start()
            gamepad1.y -> mediaPlayer.stop()
            gamepad1.a -> tankStyleControl = !tankStyleControl
            else -> if(tankStyleControl) {drivetrain.tankMecanum(gamepad1)} else {drivetrain.arcadeMecanum(gamepad1)}
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        super.stop()
    }
}