package org.firstinspires.ftc.teamcode.Dragons1;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Stephen Ogden on 11/5/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

@Autonomous(name = "6128: Just go forward lol", group = "Official")
//@Disabled
public class forwardAuto extends LinearOpMode {

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

        ElapsedTime runtime = new ElapsedTime();

        telemetry.addData("Status", "Done! Press play to start");
        telemetry.update();
        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {
            left.setPower(1);
            right.setPower(1);
            while (runtime.seconds() < 2) {
                // :P
            }
            left.setPower(0);
            right.setPower(0);
            stop();

            telemetry.addData("Status", "Done with auto!");
            telemetry.update();
        }
    }

    private static boolean isThere(DcMotor motor, int discrepancy) {
        int curentPos = motor.getCurrentPosition();
        int targetPos = motor.getTargetPosition();
        return Math.abs((targetPos - curentPos)) <= discrepancy;
    }

    private static void driveToPostion(DcMotor motor, int position, double power) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(position * 25810);
        motor.setPower(power);

    }
}
