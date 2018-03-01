package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaden on 2/3/18.
 */
@Autonomous(name = "Testing multi-glyph", group = "test")
public class AutoTest extends LinearOpMode {
    AutoDrive drive;
    Phone phone;
    Systems systems;
    ForkLift forkLift;
    JewelArm jewelArm;
    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(hardwareMap, telemetry);
        phone = new Phone(hardwareMap,telemetry);
        forkLift = new ForkLift(hardwareMap, telemetry);
        jewelArm = new JewelArm(hardwareMap, telemetry);
        systems = new Systems(drive, forkLift, jewelArm, phone, hardwareMap, telemetry);
        drive.init();
        telemetry.addLine("ready to start");
        telemetry.update();
        waitForStart();
        phone.getMark();
        systems.getMoreGlyphs(-90, CryptoboxColumn.CENTER);
        sleep(2000);
    }
}
