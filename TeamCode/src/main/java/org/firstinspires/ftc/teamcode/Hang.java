package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="hang")
public class Hang extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;
    private DcMotor HangPull = null;

    // Define class members
    double strafepower = 1;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        HangPull = hardwareMap.get(DcMotor.class, "up");

        HangPull.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            telemetry.addData("x stick", gamepad1.left_stick_x);
            telemetry.addData("y stick", gamepad1.left_stick_y);

            hang();
            telemetry.update();
            idle();
        }
    }

    //DRIVER CONTROL
    //MOTORs
    public void hang() {
        boolean rise = gamepad1.a;
        boolean fall = gamepad1.b;

        if (rise) {
            HangPull.setPower(.8);
        } else if (fall) {
            HangPull.setPower(-.8);
        } else {
           HangPull.setPower(0);
        }
    }

}
