package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.Intake;

/**
 * This class represents the Relic Recovery robot.
 */
public class RelicRecoveryRobot extends Robot {
    protected HDriveTrain hDriveTrain;
    protected GlyphLift glyphLift;
    protected Intake intake;

    /**
     * Construct a new Relic Recovery robot, with an op-mode that is using this robot.
     *
     * @param opMode the op-mode that this robot is using.
     */
    public RelicRecoveryRobot(OpMode opMode) {
        super(opMode);

        this.hDriveTrain = new HDriveTrain(this);
        this.glyphLift = new GlyphLift(this);
        this.intake = new Intake(this);

//        this.visionHelper = new VisionHelper(this);

//        visionHelper.initializeVuforia(VuforiaLocalizer.CameraDirection.BACK);
//        visionHelper.initializeOpenCV();
    }
}
