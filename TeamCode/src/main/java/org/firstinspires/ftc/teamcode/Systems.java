package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 1/3/2018.
 */

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.Point;

public class Systems {
    private DriveMecanum DriveMecanum;
    private AutoDrive AutoDrive;
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private Phone phone;
    private JewelArm JewelArm;
    private Telemetry telemetry;
    private AutoGlyphs glyphDetector;

    public Systems(DriveMecanum drive, ForkLift ForkLift, RelicClaw RelicClaw) {
        this.DriveMecanum = drive;
        this.ForkLift = ForkLift;
        this.RelicClaw = RelicClaw;
    }
    public Systems(AutoDrive drive, ForkLift ForkLift, JewelArm JewelArm, Phone phone, HardwareMap hardwareMap, Telemetry telemetry) {
        this.AutoDrive = drive;
        this.ForkLift = ForkLift;
        this.JewelArm = JewelArm;
        this.phone = phone;
        this.telemetry = telemetry;
        this.glyphDetector = new AutoGlyphs(hardwareMap, telemetry);
    }
    void pushInBlock() {
        ForkLift.openClaw();
        sleep(100);
        AutoDrive.backward(AutoDrive.DRIVE_INTO_CRYPTOBOX_SPEED,4);
        ForkLift.moveUntilDown(0.75);
        ForkLift.setClawPositionPushInBlock();
        sleep(250);
        AutoDrive.forwardTime(AutoDrive.DRIVE_INTO_CRYPTOBOX_SPEED,500);
    }

    public void grabSecondGlyph() {
        ForkLift.closeClaw();
        ForkLift.moveMotor(1, 750);
        DriveMecanum.driveTranslateRotate(0, -1, 0, 550);
        ForkLift.openClaw();
        DriveMecanum.driveTranslateRotate(0, 1, 0, 500);
        ForkLift.moveUntilDown(0.75);
        DriveMecanum.driveTranslateRotate(0, -1, 0, 750);
        ForkLift.closeClaw();
        sleep(250);
        ForkLift.moveMotor(1, 250);
    }
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }

    public void stopAll() {
        AutoDrive.stopMotors();
        ForkLift.moveMotor(0);
        RelicClaw.moveMotor(0);
    }
    public void getMoreGlyphs(double returnHeading, CryptoboxColumn column) {
        setUpMultiGlyph();
        ElapsedTime findGlyphTime = new ElapsedTime();
        findGlyphTime.reset();
        double xOffSet;
        Point bestPos = new Point(100,0);
        while(findGlyphTime.seconds()<2) {
            xOffSet = glyphDetector.getXOffset();
            if((Math.abs(xOffSet) < bestPos.x) && (xOffSet != AutoGlyphs.DEFAULT_X_POS_VALUE)) {
                bestPos.x = xOffSet;
            }
        }
        telemetry.addData("xOffSet", bestPos.x);
        telemetry.update();
    }

    public void setUpMultiGlyph() {
        ForkLift.closeAllTheWay();
        phone.faceFront();
        glyphDetector.enable();
    }
}
