
//Autonomous Code
//Made by Aniketh, Vedang, Vasudev

/*
MAKE SURE U CTRL + S it
To push it to github do this: (this is for Vasudev)
1. type in 'git status'
2. then wait for it to show something in red
3. then copy whatever was in red
4. type in 'git add [whatever was in red]'
5. then type in 'git status' (if it's in green, then u are good!)
6. then type in 'git commit -m "[type in ur message]"
7. type in 'git push'
8. finished
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Math;
import java.io.InterruptedIOException;


//@com.qualcomm.robotcore.eventloop.opmode.Autonomous
@Autonomous(name = "Main Auto", group = "auto")
public class Autonomous1 extends LinearOpMode {
    DcMotor lift;
    DcMotor back_right;
    DcMotor back_left;
    DcMotor front_left;
    DcMotor front_right;
    DcMotor moveIntake;
    Servo liftLockServo;
    Servo dustBinServo;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        lift = hardwareMap.dcMotor.get("lift");
        back_right = hardwareMap.dcMotor.get("back_right");
        back_left = hardwareMap.dcMotor.get("back_left");
        front_left = hardwareMap.dcMotor.get("front_left");
        front_right = hardwareMap.dcMotor.get("front_right");
        moveIntake = hardwareMap.dcMotor.get("move_intake");
        liftLockServo = hardwareMap.servo.get("lift_lock");
        dustBinServo = hardwareMap.servo.get("dust_bin");




        waitForStart();


        lift.setPower(-1);
        int turnCount = 0;
        lift.setPower(-1);
        liftLockServo.setPosition(0.96);
        lift.setPower(-1);
        liftLockServo.setPosition(0.97);
        lift.setPower(-1);
        liftLockServo.setPosition(0.98);
        lift.setPower(-1);
        liftLockServo.setPosition(0.99);
        lift.setPower(-1);
        liftLockServo.setPosition(1.0);
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            lift.setPower(0.5);
        }
        runtime.reset();
//        move right
//        while (opModeIsActive() && (runtime.seconds() < 3.4)) {
//            front_left.setPower(0.5);
//            back_left.setPower(0.5);
//            back_right.setPower(-0.5);
//            front_right.setPower(-0.5);
//        }

        runtime.reset();
        //move back
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            front_left.setPower(-0.5);
            back_left.setPower(-0.5);
            back_right.setPower(0.5);
            front_right.setPower(0.5);
        }

        while (opModeIsActive() && (runtime.seconds() < 0.35)) {
            back_right.setPower(1);
            back_left.setPower(1);
            front_left.setPower(-1);
            front_right.setPower(-1);
        }

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.25)) {
            moveIntake.setPower(-0.25);
        }
        // drops the marker on the
        //dustBinServo.setPosition(0.55);
    }

}