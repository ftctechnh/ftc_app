package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto Blue", group="Linear Auto")

public class Bluetestauto extends LinearOpMode {
    final static double PULSES_PER_INCH = (280 / (4 * Math.PI));
    private ElapsedTime runtime = new ElapsedTime();
    Hardware750 robot = new Hardware750();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encodeStraight(3, -0.3);
        encodeLat(3, 0.3);
        encodeStraight(10, 0.5);
    }

    //rotates the robot, 100 units is about 90 degrees
    public void encoderot(double speed, double distance) {
        speed = Math.abs(speed);
        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (opModeIsActive()) {
            targetFL = robot.flDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetFR = robot.frDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRL = robot.rlDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRR = robot.rrDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);

            if (speed > 1) {
                targetFR *= -1;
                targetRR *= -1;
            } else {
                targetFL *= -1;
                targetRL *= -1;
            }

            robot.flDrive.setTargetPosition(targetFL);
            robot.frDrive.setTargetPosition(targetFR);
            robot.rlDrive.setTargetPosition(targetRL);
            robot.rrDrive.setTargetPosition(targetRR);

            if (speed > 0) {
                robot.flDrive.setPower(speed);
                robot.frDrive.setPower(speed * -1);
                robot.rlDrive.setPower(speed);
                robot.rrDrive.setPower(speed * -1);
            } else {
                robot.flDrive.setPower(speed * -1);
                robot.frDrive.setPower(speed);
                robot.rlDrive.setPower(speed * -1);
                robot.rrDrive.setPower(speed);
            }

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
            robot.setAllDriveMotors(0);
            robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    //uses arm and color sensor to knock ball off, goes towards red
    public void encodeLat(double distance, double speed) {
        robot.arm.setPosition(50);
        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (opModeIsActive()) {

            targetFL = robot.flDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetFR = robot.frDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRL = robot.rlDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRR = robot.rrDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);

            if (robot.color.red() > 5) {
                targetFR *= -1;
                targetRL *= -1;
            } else {
                targetFL *= -1;
                targetRR *= -1;
            }

            robot.flDrive.setTargetPosition(targetFL);
            robot.frDrive.setTargetPosition(targetFR);
            robot.rlDrive.setTargetPosition(targetRL);
            robot.rrDrive.setTargetPosition(targetRR);

            if (robot.color.red() > 5) {
                robot.flDrive.setPower(speed);
                robot.frDrive.setPower(-1 * speed);
                robot.rlDrive.setPower(-1 * speed);
                robot.rrDrive.setPower(speed);
            } else {
                robot.flDrive.setPower(-1 * speed);
                robot.frDrive.setPower(speed);
                robot.rlDrive.setPower(speed);
                robot.rrDrive.setPower(-1 * speed);
            }
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
            robot.setAllDriveMotors(0);
            robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    //goes straight, negative speed goes back
    public void encodeStraight(double distance, double speed) {
        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(opModeIsActive()) {
            robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            targetFL = robot.flDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetFR = robot.frDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRL = robot.rlDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);
            targetRR = robot.rrDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH);

            if (speed < 0) {
                targetFL *= -1;
                targetFR *= -1;
                targetRL *= -1;
                targetRR *= -1;
            }

            robot.flDrive.setTargetPosition(targetFL);
            robot.frDrive.setTargetPosition(targetFR);
            robot.rlDrive.setTargetPosition(targetRL);
            robot.rrDrive.setTargetPosition(targetRR);

            runtime.reset();
            robot.flDrive.setPower(speed);
            robot.frDrive.setPower(speed);
            robot.rlDrive.setPower(speed);
            robot.rrDrive.setPower(speed);

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
            robot.setAllDriveMotors(0);
            robot.flDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

