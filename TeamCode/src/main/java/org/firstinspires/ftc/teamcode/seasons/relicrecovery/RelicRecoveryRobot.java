package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.impl.BNO055IMUWrapper;

public class RelicRecoveryRobot extends Robot {
    private HDriveTrain hDriveTrain;
    private BNO055IMUWrapper bno055IMU;

    public RelicRecoveryRobot(OpMode opMode) {
        super(opMode);

        this.hDriveTrain = new HDriveTrain(this);
        this.bno055IMU = new BNO055IMUWrapper(this);
    }
}
