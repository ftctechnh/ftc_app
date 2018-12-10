package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by  on 12/14/17.
 */

@TeleOp(name="drivee", group="Linear Opmode")

public class drivee extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor Left = null;
    private DcMotor Right = null;
    private DcMotor Pin = null;
    private DcMotor Nom = null;
    private DcMotor Elevator = null;
    private DcMotor Pulley = null;

    private Servo servo;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Left = hardwareMap.get(DcMotor.class, "left");
        Right = hardwareMap.get(DcMotor.class, "right");
        Pin = hardwareMap.get(DcMotor.class, "pinion");
        Nom = hardwareMap.get(DcMotor.class, "nom");
        Elevator = hardwareMap.get(DcMotor.class, "elevator");
        Pulley = hardwareMap.get(DcMotor.class, "pulley");

        servo = hardwareMap.get(Servo.class, "servo");

        Left.setDirection(DcMotor.Direction.REVERSE);

        Left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Pin.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Nom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Pulley.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        servo.setPosition(.55);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            telemetry.addData("x stick", gamepad1.left_stick_x);
            telemetry.addData("y stick", gamepad1.left_stick_y);

            moveRobot();
            pullUp();
            nom();
            place();
            telemetry.addData("servo", servo.getPosition());

            telemetry.update();
            idle();
        }
    }

    public void moveRobot() {
        double y = -gamepad1.left_stick_y;
        double x = -gamepad1.right_stick_x;

        //if y >.1  and x is in between certain values go forwards
        //

//        if(y > 0.1 && (previousDrive == controllerPos.DRIVE_FOWARD || previousDrive == controllerPos.ZERO)) {
//            previousDrive = controllerPos.DRIVE_FOWARD;
//            drive(y, x);
//        } else if(y < -0.1 && (previousDrive == controllerPos.DRIVE_BACK || previousDrive == controllerPos.ZERO)) {
//            previousDrive = controllerPos.DRIVE_BACK;
//            drive(y, x);
//        } else if((x<-.4 || gamepad1.dpad_right) && (previousDrive == controllerPos.STRAFE_RIGHT || previousDrive == controllerPos.ZERO)) {
//            previousDrive = controllerPos.STRAFE_RIGHT;
//        } else if((x>.4 || gamepad1.dpad_left) && (previousDrive == controllerPos.STRAFE_LEFT || previousDrive == controllerPos.ZERO)) {
//            previousDrive = controllerPos.STRAFE_LEFT;
//        }  else if(turn > 0.25 &&(previousDrive == controllerPos.TURN_RIGHT || previousDrive == controllerPos.ZERO)){
//            previousDrive = controllerPos.TURN_RIGHT;
//            turn(turn);
//        } else if(turn < -0.25 &&(previousDrive == controllerPos.TURN_LEFT || previousDrive == controllerPos.ZERO)){
//            previousDrive = controllerPos.TURN_LEFT;
//            turn(turn);
//        }

        //some slight redundancies - b/c turning and linear motion on dif sticks
        if (Math.abs(x) <= Math.abs(y)) {
            if (y > .3) {
                forwards(1);
            } else if (y < -.3) {
                forwards(-1);
            } else {
                Left.setPower(0);
                Right.setPower(0);
            }

        } else {
            if (x > .3) {
                counter(1);
            } else if (x < -.3) {
                clock(1);
            } else {
                Left.setPower(0);
                Right.setPower(0);
            }
        }

    }

    public void forwards(double drivePower) {
        Left.setPower(drivePower);
        Right.setPower(drivePower);
    }

    public void backwards(double drivePower) {
        Left.setPower(drivePower);
        Right.setPower(drivePower);
    }

    public void clock(double drivePower) {
        Left.setPower(drivePower);
        Right.setPower(-drivePower);
    }

    public void counter(double drivePower) {
        Left.setPower(-drivePower);
        Right.setPower(drivePower);
    }

    public void pullUp() {
        if (gamepad2.dpad_up) {
            Pin.setPower(-1);
        } else if (gamepad2.dpad_down) {
            Pin.setPower(1);
        } else {
            Pin.setPower(0);
        }
    }

    public void nom() {
        if (gamepad2.right_trigger > .1) {
            Nom.setPower(8);
            Elevator.setPower(1);
        } else if (gamepad2.left_trigger > .1) {
            Nom.setPower(-.8);
            Elevator.setPower(-1);
        } else {
            Nom.setPower(0);
            Elevator.setPower(0);
        }
    }

    public void place() {
        if(gamepad2.left_stick_y > .3) {
            Pulley.setPower(gamepad2.left_stick_y);
        } else if(gamepad2.left_stick_y < -.3) {
            Pulley.setPower(gamepad2.left_stick_y);
        } else {
            Pulley.setPower(0);
        }
        if(gamepad2.a) {
            servo.setPosition(.209);
        } else if(gamepad2.b) {
            servo.setPosition(.55);
        }
    }

    /**public void drive(double drivePower, double diagonalPower) {
     drivePower = readjustMotorPower(drivePower);
     drivePower = Range.clip(drivePower, -1.0, 1.0);
     diagonalPower = Range.clip(diagonalPower, -1.0, 1.0);


     // -1 -.9 -.15 0 .15 .9 1

     if (diagonalPower >= .15 && diagonalPower <= .9) {
     Le             ft.setPower(drivePower - diagonalPower);
     Right.setPower(drivePower);
     } else if (diagonalPower <= -.15 && diagonalPower >= -.9) {
     Left.setPower(drivePower);
     Right.setPower(drivePower - diagonalPower);
     } else if (diagonalPower < -.9) {
     } else if (diagonalPower > .9) {
     } else if (diagonalPower < .15 && diagonalPower > -.15) {
     Left.setPower(drivePower);
     Right.setPower(drivePower);
     }
     telemetry.addData("Motors", "drive power (%.2f)", drivePower);
     telemetry.update();
     } **/

    //KEEPS MOTORS FROM STALLING
    public double readjustMotorPower(double motorPower) {
        if (Math.abs(motorPower) >= 0.3) {
            return motorPower;
        } else {
            return 0;
        }
    }
}
