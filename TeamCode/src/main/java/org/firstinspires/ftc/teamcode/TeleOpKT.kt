package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

/*** A TeleOp program designed to drive a 4WD drivetrain based on joystick input. Written in Kotlin. ***/

@TeleOp(name = "Kotlin Demo", group = "TeleOp")
class TeleOpKT : RobotController() {

	// "lateinit" means that these variables don't mean anything until later when we set them up
	lateinit var lf : DcMotor
	lateinit var lb : DcMotor
	lateinit var rf : DcMotor
	lateinit var rb : DcMotor

	override fun init() {
		/*** set up our motors */
		lf = hardwareMap.dcMotor.get("lf") // note: you don't have to worry about semicolons anymore
		lf.direction = DcMotorSimple.Direction.FORWARD
		lb = hardwareMap.dcMotor.get("lb")
		lb.direction = DcMotorSimple.Direction.FORWARD
		rf = hardwareMap.dcMotor.get("rf")
		rf.direction = DcMotorSimple.Direction.FORWARD
		rb = hardwareMap.dcMotor.get("rb")
		rb.direction = DcMotorSimple.Direction.FORWARD
		// those vars labeled "lateinit" are now ready for use
	}

	override fun loop() {
		/*** run the motors based on curved joystick input */
		val l_power = curve_input(gamepad1.left_stick_y.toDouble())
		val r_power = curve_input(gamepad1.right_stick_y.toDouble())
		lf.power = l_power
		lb.power = l_power
		rf.power = r_power
		rb.power = r_power
	}

    // note: this function outputs a double, but is really a function
    private fun curve_input(x: Double) : Double {
		/*** curve the input to make driving feel better */
		return Math.pow(x, 1.6) //note: that's a Java function, in Kotlin!
	}
}