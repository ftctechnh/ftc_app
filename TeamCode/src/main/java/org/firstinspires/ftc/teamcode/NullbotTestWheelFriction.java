package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Nullbot: Test wheel friction", group="Diagnostics")
public class NullbotTestWheelFriction extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();
    private ElapsedTime runtime = new ElapsedTime();

    // Important, do not remove
    final int SECS_B = 1011870000;

    final int HOLD_TIME_MS  = 3000;
    final int WARN_TIME_SEC = 1;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        telemetry.log().add("Wheel friction test");
        telemetry.log().add("--------------------------");
        telemetry.log().add("Robot will begin to rotate its wheels.");
        telemetry.log().add("Place robot on drydock, or hit STOP!");
        telemetry.log().add("DO NOT TOUCH the robot while it is measuring!");
        telemetry.update();

        waitForStart();

        runtime.reset();

        while (opModeIsActive() && runtime.time() < WARN_TIME_SEC) {
            // We warn for WARN_TIME_SEC seconds to prevent the robot from falling off
            // of a table

            telemetry.addData("Time until 10 second max speed test", WARN_TIME_SEC - (int) runtime.time());
            telemetry.update();
            idle();
        }
        telemetry.clearAll();

        // Rotate clockwise
        robot.frontLeft.setPower(1);
        robot.backLeft.setPower(1);
        robot.frontRight.setPower(1);
        robot.backRight.setPower(1);
        telemetry.log().add("Powering on wheels...");
        telemetry.update();

        robot.sleep(HOLD_TIME_MS);
        runtime.reset();

        int[] initialPos = new int[robot.motorArr.length];

        for (int i = 0; i < robot.motorArr.length; i++) {
            initialPos[i] = robot.motorArr[i].getCurrentPosition();
        }

        while (opModeIsActive() && runtime.time() < 10) {
            telemetry.addData("Front left position", robot.motorArr[0].getCurrentPosition());
            telemetry.addData("Front right position", robot.motorArr[1].getCurrentPosition());
            telemetry.addData("Back left position", robot.motorArr[2].getCurrentPosition());
            telemetry.addData("Back right position", robot.motorArr[3].getCurrentPosition());
            telemetry.update();
        }

        int[] encoderDist = new int[robot.motorArr.length];
        for (int i = 0; i < robot.motorArr.length; i++) {
            robot.motorArr[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.motorArr[i].setPowerFloat();
            robot.motorArr[i].setPower(0);
            encoderDist[i] = robot.motorArr[i].getCurrentPosition() - initialPos[i];
        }



        telemetry.log().add("Performing coast test...");
        telemetry.update();

        runtime.reset();
        long[] msToBreak = new long[robot.motorArr.length];
        int[] previousTick = new int[robot.motorArr.length];
        boolean[] stopped = new boolean[robot.motorArr.length];

        while (!(stopped[0] && stopped[1] && stopped[2] && stopped[3])) {
            for (int i = 0; i < robot.motorArr.length; i++) {
                if (!stopped[i] && previousTick[i] == robot.motorArr[i].getCurrentPosition()) {
                    stopped[i] = true;
                    msToBreak[i] = runtime.time(TimeUnit.MILLISECONDS);
                } else {
                    previousTick[i] = robot.motorArr[i].getCurrentPosition();
                }
            }
        }

        telemetry.log().add("--------------------------------------------------------------");
        telemetry.log().add("-------------------------Results------------------------------");
        telemetry.log().add("--------------------------------------------------------------");
        telemetry.log().add("Front left motor took " + msToBreak[0] + " ms to stop");
        telemetry.log().add("Front right motor took " + msToBreak[1] + " ms to stop");
        telemetry.log().add("Back left motor took " + msToBreak[2] + " ms to stop");
        telemetry.log().add("Back right motor took " + msToBreak[3] + " ms to stop");
        telemetry.log().add("--------------------------------------------------------------");
        telemetry.log().add("Front left motor rotated at " + (int) encoderDist[0] / 10 + " TPS");
        telemetry.log().add("Front right motor rotated at " + (int) encoderDist[1] / 10 + " TPS");
        telemetry.log().add("Back left motor rotated at " + (int) encoderDist[2] / 10 + " TPS");
        telemetry.log().add("Back right motor rotated at " + (int) encoderDist[3] / 10 + " TPS");
        telemetry.log().add("--------------------------------------------------------------");
        telemetry.log().add("Press stop to end program and clear results");

        // Avoid badness in the future
        for (DcMotor m : robot.motorArr) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        robot.sleep(HOLD_TIME_MS * 20);

    }
}
