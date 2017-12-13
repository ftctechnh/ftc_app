package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo

enum class ClawPositions(val position:Double) {CLOSED(0.0), OPEN(125.0)}

@Autonomous(name = "Claw Position Demo", group = "Demos")
class ClawPositionsDemo : LinearOpMode() {
    private lateinit var claw : Servo
    override fun runOpMode() {
        claw = hardwareMap.servo.get("claw")
        telemetry.addLine("Servo Initialized at position" + claw.position)
        waitForStart()
        openClaw()
        closeClaw()
    }

    private fun openClaw() {
        telemetry.addLine("Claw opening...")
        claw.position = ClawPositions.OPEN.position
        if (claw.position == ClawPositions.OPEN.position) {
            telemetry.addLine("Servo opened successfully")
        } else {requestOpModeStop()}
    }
    private fun closeClaw() {
        telemetry.addLine("Claw closing...")
        claw.position = ClawPositions.CLOSED.position
        if (claw.position == ClawPositions.CLOSED.position) {
            telemetry.addLine("Servo closed successfully")
        } else {requestOpModeStop()}
    }
}