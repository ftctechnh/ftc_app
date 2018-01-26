package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.drivetrain.impl.HDriveTrain;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.Intake;
import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.JewelKnocker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Relic Recovery robot.
 */
public class RelicRecoveryRobot extends Robot {
    private final HDriveTrain hDriveTrain;
    private final VisionHelper visionHelper;

    private final GlyphLift glyphLift;
    private final Intake intake;
    private final JewelKnocker jewelKnocker;

    private final JSONParser parser;
    private final Map<String, Object> optionsMap;

    /**
     * Construct a new Relic Recovery robot, with an op-mode that is using this robot.
     *
     * @param opMode the op-mode that this robot is using.
     */
    public RelicRecoveryRobot(OpMode opMode) {
        super(opMode);

        this.parser = new JSONParser();

        this.optionsMap = parser.parseFile(new File(AppUtil.FIRST_FOLDER + "/options.json"));

        DcMotor.Direction left = DcMotor.Direction.FORWARD;
        DcMotor.Direction right = DcMotor.Direction.FORWARD;
        DcMotor.Direction middle = DcMotor.Direction.FORWARD;


        if(!(boolean) getOptionsMap().get("isLeftWheelDirReverse")){
            left = DcMotorSimple.Direction.REVERSE;
        }
        if(!(boolean) getOptionsMap().get("isMiddleWheelDirReverse")){
            middle = DcMotorSimple.Direction.REVERSE;
        }
        if(!(boolean) getOptionsMap().get("isRightWheelDirReverse")){
            right = DcMotorSimple.Direction.REVERSE;
        }


        this.hDriveTrain = new HDriveTrain.Builder(this)
                .setLeftMotorDirection(left)
                .setRightMotorDirection(middle)
                .setMiddleMotorDirection(right)
                .setWheelDiameterInches((double) getOptionsMap().get("wheelDiam"))
                .setInsideWheelGearingRatio((double) getOptionsMap().get("wheelRatIn"))
                .setOutsideWheelGearingRatio((double) getOptionsMap().get("wheelRatOut"))
                .build();

        this.glyphLift = new GlyphLift(this);
        this.intake = new Intake(this);
        this.visionHelper = new VisionHelper(this);
        this.jewelKnocker = new JewelKnocker(this);

        //visionHelper.initializeVuforia(VuforiaLocalizer.CameraDirection.BACK);
        //visionHelper.initializeOpenCV();
    }

    public HDriveTrain getHDriveTrain() {
        return hDriveTrain;
    }

    public GlyphLift getGlyphLift() {
        return glyphLift;
    }

    public Intake getIntake() {
        return intake;
    }

    public VisionHelper getVisionHelper() {
        return visionHelper;
    }

    public JewelKnocker getJewelKnocker() {
        return jewelKnocker;
    }

    public Map<String, Object> getOptionsMap() {
        return optionsMap;
    }

}

