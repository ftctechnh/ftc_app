package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Forward", group="Linear Opmode")
//@Disabled
public class TurnLeft extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //private DcMotor leftDrive = null;
    //private DcMotor rightDrive = null;

    DcMotor leftMotor;
    DcMotor rightMotor;

    double power = 0.5;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("Left_Motor");
        rightMotor = hardwareMap.dcMotor.get("Right_Motor");

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        runtime.reset();

        leftMotor.setPower(-0.5);
        rightMotor.setPower(power);

        sleep(2000);

        power = 0.0;

        leftMotor.setPower(power);
        rightMotor.setPower(power);

    }
}
