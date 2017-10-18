package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;
/**
 * This class represents the Relic Recovery robot.
 */
public class RelicRecoveryRobot extends Robot {
    protected HDriveTrain hDriveTrain;

    /**
     * Construct a new Relic Recovery robot, with an op-mode that is using this robot.
     *
     * @param opMode the op-mode that this robot is using.
     */
    public RelicRecoveryRobot(OpMode opMode) {
        super(opMode);
        this.hDriveTrain = new HDriveTrain(this);
    }
}
