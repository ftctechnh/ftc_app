package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */

public class AutonomousRed2 extends LinearOpMode {

    private ZoidbergHardware robot;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.getBackLeftDrive().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot = new ZoidbergHardware(hardwareMap);

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && robot.getGyroSensor().isCalibrating()) {
            sleep(50);
            telemetry.addData(">", "Calibrating Gyro");
            telemetry.update();
            idle();
        }

        telemetry.addData(">", "Gyro Calibrated");
        telemetry.update();

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            telemetry.addData(">", "Integrated Z value = %d", robot.getGyroSensor().getIntegratedZValue());
            telemetry.update();
            idle();
        }
    }
}
