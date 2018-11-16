package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
@Disabled
@TeleOp(name="nnooo")
public class TankMove extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor elevator;
    private DcMotor nom;
    private DcMotor lift;


    // Define class members
    double strafepower = 1;

    BaseChassis.controllerPos previousDrive = BaseChassis.controllerPos.ZERO;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        nom = hardwareMap.get(DcMotor.class, "nom");
        lift = hardwareMap.get(DcMotor.class, "lift");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if(gamepad1.dpad_up) {
                leftDrive.setPower(1);
                rightDrive.setPower(-1);
            } else if(gamepad1.dpad_down) {
                leftDrive.setPower(-1);
                rightDrive.setPower(1);
            } else if(gamepad1.dpad_left) {
                leftDrive.setPower(-1);
                rightDrive.setPower(-1);
            } else if(gamepad1.dpad_right) {
                leftDrive.setPower(1);
                rightDrive.setPower(1);
            } else {
                leftDrive.setPower(0);
                rightDrive.setPower(0);
            }



            telemetry.update();
            idle();
        }
    }
}