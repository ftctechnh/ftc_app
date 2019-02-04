
//Autonomous Code

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
    Servo liftLockServo;
    Servo dustBinServo;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        lift = hardwareMap.dcMotor.get("lift");
        liftLockServo = hardwareMap.servo.get("lift_lock");
        back_right = hardwareMap.dcMotor.get("back_right");
        back_left = hardwareMap.dcMotor.get("back_left");
        front_left = hardwareMap.dcMotor.get("front_left");
        front_right = hardwareMap.dcMotor.get("front_right");
        dustBinServo = hardwareMap.servo.get("dust_bin");

        waitForStart();

        //
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
        //move right
        while (opModeIsActive() && (runtime.seconds() < 1.7)) {
            front_left.setPower(0.5);
            back_left.setPower(-0.5);
            back_right.setPower(0.5);
            front_right.setPower(-0.5);
        }

        runtime.reset();
        //move back
        while (opModeIsActive() && (runtime.seconds() < 1.7)) {
            front_left.setPower(-0.5);
            back_left.setPower(-0.5);
            back_right.setPower(-0.5);
            front_right.setPower(-0.5);
        }
        runtime.reset();
        //spin 180 degrees so that the robo front is facing depot/crater
        while (opModeIsActive() && (runtime.seconds() < 1.7)) {
            back_right.setPower(-0.5);
            back_left.setPower(0.5);
            front_left.setPower(0.5);
            front_right.setPower(-0.5);
        }
        runtime.reset();
        //moves forward
        while (opModeIsActive() && (runtime.seconds() < 1.7)) {
            back_right.setPower(0.5);
            back_left.setPower(0.5);
            front_left.setPower(0.5);
            front_right.setPower(0.5);
        }
        // drops the marker on the
        //dustBinServo.setPosition(0.55);
    }

}
