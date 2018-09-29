package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.IMU;

/**
 * Created by Howard on 10/15/16.
 */
public class DriveTrainTask extends TaskThread {
    DriveTrainNew dt;

    public DriveTrainTask(LinearOpMode opMode) {
        this.opMode = opMode;

        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        while (opMode.opModeIsActive() && running) {

            dt.setPowers(opMode.gamepad1.left_stick_y,opMode.gamepad1.right_stick_y);
        }
        dt.setPowers(0,0);
    }
    @Override
    public void initialize() {

        this.dt = new DriveTrainNew(this.opMode);
        dt.initMotors();
    }
}

