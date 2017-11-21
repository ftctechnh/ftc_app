package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto Red", group="Linear Auto")

public class Redtestauto extends LinearOpMode {
    public enum MoveType {
        STRAIGHT, LATERALLY, ROT
    }

    final static double PULSES_PER_INCH = (280 / (4 * Math.PI));
    private ElapsedTime runtime = new ElapsedTime();
    Hardware750 robot = new Hardware750();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encode(200, 0.5, MoveType.STRAIGHT);
    }

    //Negative speed means:
    //Counterclockwise for MoveType.ROT
    //Left for MoveType.LATERALLY
    //Backwards for MoveType.STRAIGHT
    public void encode(double distance, double speed, MoveType move) {
        int multFL = 1;
        int multFR = 1;
        int multRL = 1;
        int multRR = 1;

        int targetFL;
        int targetFR;
        int targetRL;
        int targetRR;

        if (move == MoveType.ROT) {
            if (speed > 0) {
                multFR *= -1;
                multRR *= -1;
            } else {
                multFL *= -1;
                multRL *= -1;
            }
        } else if (move == MoveType.LATERALLY) {
            if (speed > 0) {
                multFR *= -1;
                multRL *= -1;
            } else {
                multFL *= -1;
                multRR *= -1;
            }
        } else if (move == MoveType.STRAIGHT) {
            if (speed < 0) {
                multFL *= -1;
                multFR *= -1;
                multRL *= -1;
                multRR *= -1;

            }
        }

        robot.rlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(opModeIsActive()) {
            robot.flDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            targetFL = multFL * (robot.flDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));
            targetFR = multFR * (robot.frDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));
            targetRL = multRL * (robot.rlDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));
            targetRR = multRR * (robot.rrDrive.getCurrentPosition() + (int) (distance * PULSES_PER_INCH));

            robot.flDrive.setTargetPosition(targetFL);
            robot.frDrive.setTargetPosition(targetFR);
            robot.rlDrive.setTargetPosition(targetRL);
            robot.rrDrive.setTargetPosition(targetRR);

            runtime.reset();
            robot.flDrive.setPower(speed * multFL);
            robot.frDrive.setPower(speed * multFR);
            robot.rlDrive.setPower(speed * multRL);
            robot.rrDrive.setPower(speed * multRR);

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