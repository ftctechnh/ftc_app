package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Stephen Ogden on 10/16/17.
 * FTC 6128 | 7935
 * FRC 1595
 */
@Autonomous(name = "DC Motor run", group = "Test")
@Disabled
public class motorTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        DcMotor left = hardwareMap.dcMotor.get("lf");
        DcMotor right = hardwareMap.dcMotor.get("rf");
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        //left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Status", "Done! Press play to start" );
        telemetry.update();
        waitForStart();
        ElapsedTime runtime = new ElapsedTime();
        while(opModeIsActive()) {
            left.setPower(1);
            right.setPower(1);
            if (runtime.seconds() > 1) {
                left.setPower(0);
                right.setPower(0);
                stop();
            }
            idle();
        }
    }
}
