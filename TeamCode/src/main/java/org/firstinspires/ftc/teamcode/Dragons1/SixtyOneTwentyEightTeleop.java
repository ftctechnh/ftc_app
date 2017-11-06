package org.firstinspires.ftc.teamcode.Dragons1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Stephen Ogden on 10/30/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

// Teleop for 6128
@TeleOp(name = "6128 TeleOp", group = "Official")
//@Disabled

/*
Function which will move the box to one of 4 different positions
    Based on encoder
    Pass a value - Value corresponds to position

Another funciton to lift
    Controlled by Continuous SERVO which is connected to encoder
    Gear ratio is 12:1
        560 tics per rotation on neverest
        290 on core hex
        12 full rotations around the small to go one full around the gear
    Goes from starting posotion up by 90°

Intake
    2 Motors

Outtake
    2 Motors

    // TODO: All motors (Except drive) are Core Hex motors!!!


 */

public class SixtyOneTwentyEightTeleop extends LinearOpMode {

    private boolean armMoving = false;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        DcMotor left = hardwareMap.dcMotor.get("left");
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotor right = hardwareMap.dcMotor.get("right");
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        DcMotor lintake = hardwareMap.dcMotor.get("lintake");
        lintake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        lintake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DcMotor rintake = hardwareMap.dcMotor.get("rintake");
        rintake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rintake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        DcMotor arm = hardwareMap.dcMotor.get("arm");
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //CRServo arm = hardwareMap.crservo.get("arm");

        DcMotor box = hardwareMap.dcMotor.get("box");
        box.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //box.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        box.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        box.setDirection(DcMotorSimple.Direction.FORWARD);

        //Servo leftServo = hardwareMap.servo.get("leftServo");
        //Servo rightServo = hardwareMap.servo.get("rightServo");

        double rightPos = 1;
        double leftPos = 1;


        telemetry.addData("Status", "Done! Press play to start");
        telemetry.update();
        waitForStart();
        float slow=1.0f;

        while (opModeIsActive()) {

            // if leftPos > 1

            left.setPower(gamepad1.left_stick_y * -1);
            right.setPower(gamepad1.right_stick_y * -1);

            Out(lintake, rintake, gamepad2.left_bumper);
            In(lintake, rintake, gamepad2.right_bumper);


            /*
            if (gamepad2.x) {
                //boxPos(box, 0);
                moveBoxTo(box,12,1);
                //manualMoveBoxUp(box, gamepad2.x);

            } else if (gamepad2.a) {
                //boxPos(box, 1);
                moveBoxTo(box, 0,1);
            } else if (gamepad2.b) {
                //boxPos(box, 2);
                moveBoxTo(box, 6, 1);
            }
            */

            //if (gamepad2.dpad_down) {
                //armDown(arm);
                //moveArm(arm, 90, 1);
            //manualMoveUp(arm, gamepad2.dpad_up);
            //} else if (gamepad2.dpad_up) {
                //armUp(arm);
                //moveArm(arm, 0, 1);
            //manualMoveDown(arm, gamepad2.dpad_down);

            //}
            if(gamepad2.a) {
                slow=0.35f;
            }
            else {
                slow=1.0f;
            }

            arm.setPower(slow*gamepad2.left_stick_y); // Manual control
            box.setPower(slow*gamepad2.right_stick_y); // Manuual control


        }
        telemetry.addData("RPos", right.getCurrentPosition())
                .addData("LPos", left.getCurrentPosition());
        telemetry.update();

    }
    private void Out (DcMotor motor0, DcMotor motor1, boolean run) {
        if (run) {
            motor0.setDirection(DcMotorSimple.Direction.REVERSE);
            motor1.setDirection(DcMotorSimple.Direction.REVERSE);
            motor0.setPower(1);
            motor1.setPower(1);
        } else {
            motor0.setPower(0);
            motor1.setPower(0);
        }

    }
    private void In (DcMotor motor0, DcMotor motor1, boolean run) {
        if (run) {
            motor0.setDirection(DcMotorSimple.Direction.FORWARD);
            motor1.setDirection(DcMotorSimple.Direction.FORWARD);
            motor0.setPower(1);
            motor1.setPower(1);
        } else {
            motor0.setPower(0);
            motor1.setPower(0);
        }

    }

    // TODO: Redo box postion (Based on inches)

    private void manualMoveUp(DcMotor motor, boolean run) {
        if (run) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
            motor.setPower(1);
        } else {
            motor.setPower(0);
        }
    }

    private void manualMoveDown(DcMotor motor, boolean run){
        if (run) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setDirection(DcMotorSimple.Direction.REVERSE);
            motor.setPower(1);
        } else {
            motor.setPower(0);
        }
    }


    private void moveBoxTo(DcMotor motor, int position, double power) {
        motor.setTargetPosition((int)(290/(3.14*1.2) * position)); //290 tick/rotation * (3.14 * 1.2) inches/rotation --> 290*1/(3.14*1.2) ticks/inch
        motor.setPower(power);
    }


    /*
    private void boxPos (DcMotor motor, int position) {

        switch (position){
            case 0: {
                // Something
            }
            case 1: {
                // Something
            }
            case 2: {
                // Something
            }
            case 3: {
                // Something
            }
        }
    }
    */

    private void moveArm (DcMotor motor, int degrees, double power) {
        // 290*12*(96/360) = 729.6
        //float conversion = 3480.0f * (degrees/360.0f);
        int position = Math.round(((290*12)/360) * (degrees));

        motor.setTargetPosition(position);
        motor.setPower(power);

    }

    private static boolean isThere(DcMotor motor, int discrepancy) {
        int curentPos = motor.getCurrentPosition();
        int targetPos = motor.getTargetPosition();
        return Math.abs((targetPos - curentPos)) <= discrepancy;
    }
    /*
    private void armUp (DcMotor motor) {
        if (armMoving) {
            if (Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) <= 50) {
                motor.setPower(0);
                armMoving = false;
            }
        } else {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(730);
            motor.setPower(1);
            armMoving = true;
        }
    }

    private void armDown (DcMotor motor) {
        if (armMoving) {
            if (Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) <= 50) {
                motor.setPower(0);
                armMoving = false;
            }
        } else {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setTargetPosition(-(730));
            motor.setPower(1);
            armMoving = true;
        }
    }
    */
    /*
    private void armUp (CRServo servo) {
        if (armMoving) {
            if (Math.abs(servo.))
        }
    }
    */
}
