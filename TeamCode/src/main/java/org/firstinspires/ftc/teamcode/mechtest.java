package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="mechh", group="Linear Opmode")
public class mechtest extends LinearOpMode {
    private DcMotor FR = null;
    private DcMotor FL = null;
    private DcMotor BL = null;
    private DcMotor BR = null;
    private DcMotor PIVOT = null;
    private DcMotor NOM = null;
    private DcMotor HOOK = null;
    private DcMotor EXTEND = null;
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
        PIVOT = hardwareMap.get(DcMotor.class, "pivot");
        NOM = hardwareMap.get(DcMotor.class, "nom");
        HOOK = hardwareMap.get(DcMotor.class, "hook");
        EXTEND = hardwareMap.get(DcMotor.class, "extend");
        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);

        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        PIVOT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        NOM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        HOOK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        while (opModeIsActive()) {
            moveRobot();
            intake();
            hook();

            idle();
        }
    }

    public void moveRobot() {
        double drive = -gamepad1.left_stick_y;
        double diagonalDrive = -gamepad1.left_stick_x;
        double turn = (-gamepad1.right_stick_x / 1.2) / turnConstant;

        if (drive > 0.1 && (previousDrive == controllerPos.DRIVE_FOWARD || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_FOWARD;
            Drive(drive, diagonalDrive);
        } else if (drive < -0.1 && (previousDrive == controllerPos.DRIVE_BACK || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.DRIVE_BACK;
            Drive(drive, diagonalDrive);
        } else if ((diagonalDrive < -.4 || gamepad1.dpad_right) && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
            previousDrive = controllerPos.STRAFE_RIGHT;
            Strafe(-1);
        } else if ((diagonalDrive > .4 || gamepad1.dpad_left) && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
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

    public void Drive(double drivePower, double diagonalPower) {
        drivePower = readjustMotorPower(drivePower);
        drivePower = Range.clip(drivePower, -1.0, 1.0);
        diagonalPower = Range.clip(diagonalPower, -1.0, 1.0);


        // -1 -.9 -.15 0 .15 .9 1

        if (diagonalPower >= .15 && diagonalPower <= .9) {
            FL.setPower(drivePower - diagonalPower);
            BL.setPower(drivePower);
            FR.setPower(drivePower);
            BR.setPower(drivePower - diagonalPower);
        } else if (diagonalPower <= -.15 && diagonalPower >= -.9) {
            FL.setPower(drivePower);
            BL.setPower(drivePower - diagonalPower);
            FR.setPower(drivePower - diagonalPower);
            BR.setPower(drivePower);
        } else if (diagonalPower < -.9) {
            Strafe(-1);
        } else if (diagonalPower > .9) {
            Strafe(1);
        } else if (diagonalPower < .15 && diagonalPower > -.15) {
            FL.setPower(drivePower);
            BL.setPower(drivePower);
            FR.setPower(drivePower);
            BR.setPower(drivePower); //can make this an else
        }
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
        double Rpower = turn;
        double Lpower = -turn;

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
            PIVOT.setPower(.6);
        } else if(gamepad1.b) {
            PIVOT.setPower(-.6);
        } else {
            PIVOT.setPower(0);
        }

        if(gamepad1.x) {
            NOM.setPower(.8);
        } else if(gamepad1.y) {
            NOM.setPower(-.8);
        } else {
            NOM.setPower(0);
        }

        if(gamepad2.dpad_up) {
            EXTEND.setPower(1);
        } else if(gamepad2.dpad_down) {
            EXTEND.setPower(-1);
        } else {
            EXTEND.setPower(0);
        }
    }

    public void hook() {
        if(gamepad1.dpad_up) {
            HOOK.setPower(1);
        } else if(gamepad1.dpad_down) {
            HOOK.setPower(-1);
        } else {
            HOOK.setPower(0);
        }
    }
}


