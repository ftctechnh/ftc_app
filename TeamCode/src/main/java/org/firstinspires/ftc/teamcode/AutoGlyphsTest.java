package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Kaden on 2/20/18.
 */
@Autonomous(name = "testing auto glyphs stuff", group = "test")
public class AutoGlyphsTest extends LinearOpMode {
    private AutoDrive drive;
    private JewelArm JewelArm;
    private ForkLift ForkLift;
    private Phone phone;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Systems Systems;
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET"); telemetry.update();
        drive = new AutoDrive(this);
        //drive.calibrateGyro(); //Calibrates gyro
        JewelArm = new JewelArm(this);
        ForkLift = new ForkLift(this);
        phone = new Phone(this);
        Systems = new Systems(drive, ForkLift, JewelArm, phone, hardwareMap, telemetry);
        phone.closeVuforia();
        telemetry.addLine("NOW YOU CAN PRESS PLAY"); telemetry.update();
        waitForStart();
        Systems.getMoreGlyphs(-90, CryptoboxColumn.CENTER);
        Systems.glyphDetector.disable();
        sleep(2000);
    }
}
