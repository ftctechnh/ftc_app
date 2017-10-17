package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;

public class RelicRecoveryRobot extends Robot {
    protected HDriveTrain hDriveTrain;

    public RelicRecoveryRobot(OpMode opMode) {
        super(opMode);
        this.hDriveTrain = new HDriveTrain(this);
    }
}
