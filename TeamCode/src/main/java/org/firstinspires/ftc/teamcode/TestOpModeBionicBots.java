package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="TestOpModeBionicBots")
public class TestOpModeBionicBots extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
private DcMotor rightDrive = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status","Initialized");
        telemetry.update();

        double powerLeft = 0;
        double powerRight = 0;

        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        while (opModeIsActive()){
            powerLeft = this.gamepad1.left_stick_y;
            powerRight = this.gamepad1.right_stick_y;

            telemetry.addData("PowerLeft:",powerLeft);
            telemetry.addData("PowerRight:",powerRight);

            telemetry.update();

            leftDrive.setPower(-0.8 * powerLeft);
            rightDrive.setPower(0.8 * powerRight);
        }
    }
}
