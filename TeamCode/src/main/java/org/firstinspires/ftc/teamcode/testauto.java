package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Go Forward", group="Linear Auto")

public class testauto extends LinearOpMode {
    final static double PULSES_PER_INCH = (280 / (4 * Math.PI));
    private ElapsedTime runtime = new ElapsedTime();
    Hardware750 robot = new Hardware750();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encode(1000, 1);
    }

    public void encode(double distance, double speed) {
        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        if(opModeIsActive()) {
            robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            targetFL = robot.flDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetFR = robot.frDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRL = robot.rlDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRR = robot.rrDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);

            robot.flDrive.setTargetPosition(targetFL);
            robot.frDrive.setTargetPosition(targetFR);
            robot.rlDrive.setTargetPosition(targetRL);
            robot.rrDrive.setTargetPosition(targetRR);

            runtime.reset();
            robot.flDrive.setPower(Math.abs(speed));
            robot.frDrive.setPower(Math.abs(speed));
            robot.rlDrive.setPower(Math.abs(speed));
            robot.rrDrive.setPower(Math.abs(speed));

            while (opModeIsActive() && (robot.flDrive.isBusy() && robot.frDrive.isBusy() && robot.rlDrive.isBusy() && robot.rrDrive.isBusy())) {
                int i = 0;
                telemetry.addData("Current fl: ", robot.flDrive.getCurrentPosition());
                telemetry.addData("Current fr: ", robot.frDrive.getCurrentPosition());
                telemetry.addData("Current rl: ", robot.rlDrive.getCurrentPosition());
                telemetry.addData("Current rr: ", robot.rrDrive.getCurrentPosition());
                telemetry.addData("fl: ", targetFL);
                telemetry.addData("fr: ", targetFR);
                telemetry.addData("rl: ", targetRL);
                telemetry.addData("rr: ", targetRR);
                telemetry.addData("cool number: ", i);
                i++;
                telemetry.update();
            }
            robot.setAllMotors(0);
            robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

