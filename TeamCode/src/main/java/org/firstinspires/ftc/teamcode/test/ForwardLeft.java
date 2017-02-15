package org.firstinspires.ftc.teamcode.test;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 2/2/2017.
 */

@Autonomous
public class ForwardLeft extends LinearOpMode {
    HolonomicRobot robot = new HolonomicRobot();
    private static final double WHITE = 2.63; //TODO

    private double lSpeed;
    private double rSpeed;
    private double speed = 0;

    private PIDTest controller;

    private ElapsedTime forwardTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        controller = new PIDTest(hardwareMap, robot.backLeft, robot.frontLeft);

        int alliance = 0;
        String allianceColor;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(hardwareMap.appContext);
        alliance = Integer.parseInt(sharedPreferences.getString("mk_color", "0"));
        allianceColor = alliance==0 ? "Blue" : "Red";

        lSpeed = Double.parseDouble(sharedPreferences.getString("mk_kp", ".25"));
        rSpeed = Double.parseDouble(sharedPreferences.getString("mk_ki", ".25"));

        waitForStart();

        //robot.gyro.calibrate();

        robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sleep(500);

        controller = new PIDTest(hardwareMap, robot.backLeft, robot.frontLeft);
        controller.setTuning(0.00025, 0, 0, 0);
        controller.resetPID();

        double correction = 0;

        forwardTimer.reset();

        /*
        while(speed < lSpeed && opModeIsActive())
        {
            Holonomic.move(-speed, -speed, speed, speed, robot);
            speed += 0.0005;
        }

        Holonomic.stop(robot);

        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(500);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sleep(500);
        controller.resetPID();
        forwardTimer.reset();*/

        robot.move(0, 0, lSpeed, lSpeed);
        sleep(50);
        robot.move(-lSpeed, -lSpeed, lSpeed, lSpeed);

        while(forwardTimer.time() < 5000 && opModeIsActive()) {
            robot.move(-lSpeed - correction, -lSpeed - correction, lSpeed - correction, lSpeed - correction);
            correction = controller.getCorrection();

            telemetry.addData("Encoders: ", "%4d %4d %4d %4d", robot.frontLeft.getCurrentPosition(), robot.frontRight.getCurrentPosition(), robot.backLeft.getCurrentPosition(), robot.backRight.getCurrentPosition());
            telemetry.addData("Correction: ", correction);
            telemetry.update();
        }
        robot.stop();

        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(500);
    }
}
