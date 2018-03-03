package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Kaden on 2/20/18.
 */
@Autonomous(name = "testing auto glyphs stuff", group = "test")
public class AutoGlyphsTest extends LinearOpMode {
    AutoGlyphs cv;
    private AutoDrive drive;
    private JewelArm JewelArm;
    private ForkLift ForkLift;
    private Phone phone;
    private RelicRecoveryVuMark pictograph = RelicRecoveryVuMark.UNKNOWN;
    private Systems Systems;
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET"); telemetry.update();
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init(); //Calibrates gyro
        JewelArm = new JewelArm(hardwareMap, telemetry);
        ForkLift = new ForkLift(hardwareMap, telemetry);
        phone = new Phone(hardwareMap, telemetry);
        Systems = new Systems(drive, ForkLift, JewelArm, phone, hardwareMap, telemetry);
        telemetry.addLine("NOW YOU CAN PRESS PLAY"); telemetry.update();
        waitForStart();
        phone.closeVuforia();
        Systems.getMoreGlyphs(-90, CryptoboxColumn.CENTER);
        sleep(2000);
    }

}
