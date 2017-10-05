package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.IRobot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.IDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.impl.BNO055IMUWrapper;

public class RelicRecoveryRobot implements IRobot {
    private OpMode opMode;

    private HDriveTrain hDriveTrain;
    private BNO055IMUWrapper bno055IMU;

    public RelicRecoveryRobot(OpMode opMode) {
        this.opMode = opMode;

        this.hDriveTrain = new HDriveTrain();
        hDriveTrain.initialize(this);

        this.bno055IMU = new BNO055IMUWrapper();
        bno055IMU.initialize(this);
    }

    @Override
    public IDriveTrain getDriveTrain() {
        return hDriveTrain;
    }

    @Override
    public OpMode getCurrentOpMode() {
        return opMode;
    }
}
