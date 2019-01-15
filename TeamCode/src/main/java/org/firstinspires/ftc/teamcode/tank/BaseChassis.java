package org.firstinspires.ftc.teamcode.tank;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="no")
@Disabled
public class BaseChassis extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;

    // Define class members
    double strafepower = 1;

    BaseChassis.controllerPos previousDrive = BaseChassis.controllerPos.ZERO;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        FrontLeftDrive = hardwareMap.get(DcMotor.class, "front_left");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "front_right");
        BackLeftDrive = hardwareMap.get(DcMotor.class, "back_left");
        BackRightDrive = hardwareMap.get(DcMotor.class, "back_right");

        FrontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        BackRightDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.FORWARD);

        FrontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            telemetry.addData("x stick", gamepad1.left_stick_x);
            telemetry.addData("y stick", gamepad1.left_stick_y);

            moveRobot();
            telemetry.update();
            idle();
        }
    }
    public enum controllerPos {
        STRAFE_RIGHT, STRAFE_LEFT, DRIVE_FOWARD, DRIVE_BACK, TURN_RIGHT, TURN_LEFT, ZERO;
    }


    //DRIVER CONTROL
    //MOTORs

    public void moveRobot() {
        double drive = -gamepad1.left_stick_y;
        double diagonalDrive = -gamepad1.left_stick_x;
        double turn = (-gamepad1.right_stick_x/1.2);

        if(drive > 0.1 && (previousDrive == BaseChassis.controllerPos.DRIVE_FOWARD || previousDrive == BaseChassis.controllerPos.ZERO)) {
            previousDrive = BaseChassis.controllerPos.DRIVE_FOWARD;
            Drive(drive, diagonalDrive);
        } else if(drive < -0.1 && (previousDrive == BaseChassis.controllerPos.DRIVE_BACK || previousDrive == BaseChassis.controllerPos.ZERO)) {
            previousDrive = BaseChassis.controllerPos.DRIVE_BACK;
            Drive(drive, diagonalDrive);
        } else if((diagonalDrive<-.4 || gamepad1.dpad_right) && (previousDrive == BaseChassis.controllerPos.STRAFE_RIGHT || previousDrive == BaseChassis.controllerPos.ZERO)) {
            previousDrive = BaseChassis.controllerPos.STRAFE_RIGHT;
            Strafe(-1);
        } else if((diagonalDrive>.4 || gamepad1.dpad_left) && (previousDrive == BaseChassis.controllerPos.STRAFE_LEFT || previousDrive == BaseChassis.controllerPos.ZERO)) {
            previousDrive = BaseChassis.controllerPos.STRAFE_LEFT;
            Strafe(1);
        }  else if(turn > 0.25 &&(previousDrive == BaseChassis.controllerPos.TURN_RIGHT || previousDrive == BaseChassis.controllerPos.ZERO)){
            previousDrive = BaseChassis.controllerPos.TURN_RIGHT;
            turn(turn);
        } else if(turn < -0.25 &&(previousDrive == BaseChassis.controllerPos.TURN_LEFT || previousDrive == BaseChassis.controllerPos.ZERO)){
            previousDrive = BaseChassis.controllerPos.TURN_LEFT;
            turn(turn);
        }
        else {
            previousDrive = BaseChassis.controllerPos.ZERO;
            FrontLeftDrive.setPower(0);
            BackLeftDrive.setPower(0);
            FrontRightDrive.setPower(0);
            BackRightDrive.setPower(0);
        }

    }

    //STRAFING CONTROL
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

        FrontLeftDrive.setPower(FLpower);
        BackLeftDrive.setPower(BLpower);
        FrontRightDrive.setPower(FRpower);
        BackRightDrive.setPower(BRpower);

    }
    //DRIVING FOWARADS/BACKWARDS/TURNING

    public void Drive2(double y, double x){
        x = -x;
        double FL = 0;
        double FR = 0;
        double BL = 0;
        double BR = 0;

        if(y>0) {
            FL = 1;
            FR = 1;
            BL = 1;
            BR = 1;
        }
        else if(y<0){
            FL = -1;
            FR = -1;
            BL = -1;
            BR = -1;
        }

        if(y>0&&x>0){
            FR-=(2*x);
            BL-=(2*x);
        }
        else if (y>0&&x<0) {
            FL+=(2*x);
            BR+=(2*x);
        }
        else if (y<0&&x>0){
            FL+=(2*x);
            BR+=(2*x);
        }
        else if (y<0&&x<0){
            FR-=(2*x);
            BL-=(2*x);
        }

        if(y==0){
            FrontLeftDrive.setPower(x);
            FrontRightDrive.setPower(-x);
            BackLeftDrive.setPower(-x);
            BackRightDrive.setPower(x);
        }
        else {
            FrontLeftDrive.setPower(FL);
            FrontRightDrive.setPower(FR);
            BackLeftDrive.setPower(BL);
            BackRightDrive.setPower(BR);
        }
    }

    public void Drive(double drivePower, double diagonalPower) {
        drivePower = readjustMotorPower(drivePower);
        drivePower = Range.clip(drivePower, -1.0, 1.0);
        diagonalPower = Range.clip(diagonalPower, -1.0, 1.0);


        // -1 -.9 -.15 0 .15 .9 1

        if (diagonalPower >= .15 && diagonalPower <= .9) {
            FrontLeftDrive.setPower(drivePower - diagonalPower);
            BackLeftDrive.setPower(drivePower);
            FrontRightDrive.setPower(drivePower);
            BackRightDrive.setPower(drivePower - diagonalPower);
        } else if (diagonalPower <= -.15 && diagonalPower >= -.9) {
            FrontLeftDrive.setPower(drivePower);
            BackLeftDrive.setPower(drivePower - diagonalPower);
            FrontRightDrive.setPower(drivePower - diagonalPower);
            BackRightDrive.setPower(drivePower);
        } else if (diagonalPower < -.9) {
            Strafe(-1);
        } else if (diagonalPower > .9) {
            Strafe(1);
        } else if (diagonalPower < .15 && diagonalPower > -.15) {
            FrontLeftDrive.setPower(drivePower);
            BackLeftDrive.setPower(drivePower);
            FrontRightDrive.setPower(drivePower);
            BackRightDrive.setPower(drivePower); //can make this an else
        }
        telemetry.addData("Motors", "drive power (%.2f)", drivePower);
        telemetry.update();
    }



    public void turn(double turn){
        double Rpower = turn;
        double Lpower = -turn;

        Rpower = readjustMotorPower(Rpower);
        Lpower = readjustMotorPower(Lpower);

        Rpower = Range.clip(Rpower, -1.0, 1.0);
        Lpower = Range.clip(Lpower, -1.0, 1.0);

        FrontLeftDrive.setPower(Lpower);
        BackLeftDrive.setPower(Lpower);
        FrontRightDrive.setPower(Rpower);
        BackRightDrive.setPower(Rpower);
    }

    //KEEPS MOTORS FROM STALLING
    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }
}
