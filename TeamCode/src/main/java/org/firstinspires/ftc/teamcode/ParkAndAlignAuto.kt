package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor

@Autonomous(name = "Park and Align Auto")
class ParkAndAlignAuto : LinearOpMode() {

    private lateinit var drivetrain : ReboundDriver

    override fun runOpMode() {
        val lf : DcMotor = hardwareMap.dcMotor.get("lf")
        val lb : DcMotor = hardwareMap.dcMotor.get("lb")
        val rf : DcMotor = hardwareMap.dcMotor.get("rf")
        val rb : DcMotor = hardwareMap.dcMotor.get("rb")
        val imu : BNO055IMU = hardwareMap.get(BNO055IMU::class.java, "imu")
        drivetrain = ReboundDriver(telemetry, hardwareMap, lf, lb, rf, rb, imu)
        drivetrain.initialize()
        //val mediaPlayer: MediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.up_and_away)
        //mediaPlayer.start()
        waitForStart()
        while(opModeIsActive()) {
            drivetrain.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION)
            drivetrain.setMotorPowers(0.2, 0.2, 0.2, 0.2)
            drivetrain.setTargetPositions(280)
        }
        //mediaPlayer.stop()

    }


}