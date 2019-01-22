
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Gamepad", group="Linear Opmode")
//@Disabled
public class Gamepad extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //private DcMotor leftDrive = null;
    //private DcMotor rightDrive = null;

    DcMotor motorA1;
    DcMotor motorA2;
    DcMotor motorB1;
    DcMotor motorB2;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        motorA1 = hardwareMap.dcMotor.get("Motor1");
        motorA2 = hardwareMap.dcMotor.get("Motor2");
        motorB1 = hardwareMap.dcMotor.get("Motor3");
        motorB2 = hardwareMap.dcMotor.get("Motor4");
        motorA2.setDirection(DcMotorSimple.Direction.REVERSE);
        motorB2.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addData("Status", "Runtime: "+ runtime.toString());
            telemetry.update();
            motorA1.setPower(-gamepad1.left_stick_y);
            motorA2.setPower(-gamepad1.left_stick_y);
            motorB1.setPower(-gamepad1.right_stick_y);
            motorB2.setPower(-gamepad1.right_stick_y);

        }
    }
}
