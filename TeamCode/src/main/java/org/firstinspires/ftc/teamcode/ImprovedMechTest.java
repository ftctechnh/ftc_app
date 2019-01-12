package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="mech2h", group="Linear Opmode")
public class ImprovedMechTest extends LinearOpMode {
    private DcMotor FR = null;
    private DcMotor FL = null;
    private DcMotor BL = null;
    private DcMotor BR = null;
    private DcMotor pivot = null;
    private DcMotor nom = null;
    private DcMotor hook = null;
    private DcMotor extend = null;
    controllerPos previousDrive = controllerPos.ZERO;

    static double turnConstant = 1;

    // Define class members
    double strafepower = 1;

    @Override
    public void runOpMode() {
        FL = hardwareMap.get(DcMotor.class, "fl");
        FR = hardwareMap.get(DcMotor.class, "fr");
        BL = hardwareMap.get(DcMotor.class, "bl");
        BR = hardwareMap.get(DcMotor.class, "br");
        pivot = hardwareMap.get(DcMotor.class, "pivot");
        //nom = hardwareMap.get(DcMotor.class, "nom");
        hook = hardwareMap.get(DcMotor.class, "hook");
        extend = hardwareMap.get(DcMotor.class, "extend");
        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);

        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //nom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        while (opModeIsActive()) {
            moveRobot();
            intake();
            hook();

            telemetry.update();
            idle();
        }
    }

    public void moveRobot() {
        double drive = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn = (-gamepad1.right_stick_x / 1.2) / turnConstant;

        if (drive > 0.1 && (previousDrive == controllerPos.DRIVE_FOWARD || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_FOWARD;
            Drive(drive);
        } else if (drive < -0.1 && (previousDrive == controllerPos.DRIVE_BACK || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_BACK;
            Drive(drive);
        } else if ((strafe < -.4 || gamepad1.dpad_right) && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_RIGHT;
            Strafe(-1);
        } else if ((strafe > .4 || gamepad1.dpad_left) && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_LEFT;
            Strafe(1);
        } else if (turn > 0.25 && (previousDrive == controllerPos.TURN_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.TURN_RIGHT;
            turn(turn);
        } else if (turn < -0.25 && (previousDrive == controllerPos.TURN_LEFT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.TURN_LEFT;
            turn(turn);
        } else {
            previousDrive = controllerPos.ZERO;
            FL.setPower(0);
            BL.setPower(0);
            FR.setPower(0);
            BR.setPower(0);
        }


    }
    public void Strafe(int strafedirection) {

        double FRpower = strafedirection * strafepower;
        double BLpower = strafedirection * strafepower;
        double BRpower = -1 * strafedirection * strafepower;
        double FLpower = -1 * strafedirection * strafepower ;

        FLpower = Range.clip(FLpower, -1.0, 1.0) ;
        BRpower = Range.clip(BRpower, -1.0, 1.0) ;
        BLpower = Range.clip(BLpower, -1.0, 1.0) ;
        FRpower = Range.clip(FRpower, -1.0, 1.0) ;

        readjustMotorPower(FRpower);
        readjustMotorPower(BRpower);
        readjustMotorPower(FLpower);
        readjustMotorPower(BLpower);

        FL.setPower(FLpower);
        BL.setPower(BLpower);
        FR.setPower(FRpower);
        BR.setPower(BRpower);

    }

    public void Drive(double drivePower) {
        drivePower = readjustMotorPower(drivePower);
        drivePower = Range.clip(drivePower, -1.0, 1.0);
        FL.setPower(drivePower);
        BL.setPower(-drivePower);
        FR.setPower(drivePower);
        BR.setPower(-drivePower); //can make this an else
        telemetry.addData("Motors", "drive power (%.2f)", drivePower);
        telemetry.update();
    }

    public enum controllerPos {
        STRAFE_RIGHT, STRAFE_LEFT, DRIVE_FOWARD, DRIVE_BACK, TURN_RIGHT, TURN_LEFT, ZERO;
    }

    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }

    public void turn(double turn) {
        double Rpower = -turn;
        double Lpower = turn;

        Rpower = readjustMotorPower(Rpower);
        Lpower = readjustMotorPower(Lpower);

        Rpower = Range.clip(Rpower, -1.0, 1.0);
        Lpower = Range.clip(Lpower, -1.0, 1.0);

        FL.setPower(Lpower);
        BL.setPower(Lpower);
        FR.setPower(Rpower);
        BR.setPower(Rpower);
    }

    public void intake() {
        if(gamepad1.a) {
            pivot.setPower(.6);
        } else if(gamepad1.b) {
            pivot.setPower(-.6);
        } else {
            pivot.setPower(0);
        }


//        if(gamepad1.x) {
//            nom.setPower(.8);
//        } else if(gamepad1.y) {
//            nom.setPower(-.8);
//        } else {
//            nom.setPower(0);
//        }

        if(gamepad2.dpad_up) {
            extend.setPower(1);
        } else if(gamepad2.dpad_down) {
            extend.setPower(-1);
        } else {
            extend.setPower(0);
        }
    }

    public void hook() {
        if(gamepad1.dpad_up) {
            hook.setPower(1);
        } else if(gamepad1.dpad_down) {
            hook.setPower(-1);
        } else {
            hook.setPower(0);
        }
    }
}


