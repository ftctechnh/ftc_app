package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaden on 2/3/18.
 */
@Autonomous(name = "Testing cv", group = "test")
public class AutoTest extends LinearOpMode {
    AutoDrive drive;
    Phone phone;
    Systems systems;
    ForkLift forkLift;
    JewelArm jewelArm;
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET"); telemetry.update();
        drive = new AutoDrive(hardwareMap, telemetry);
        jewelArm = new JewelArm(hardwareMap, telemetry);
        forkLift = new ForkLift(hardwareMap, telemetry);
        phone = new Phone(hardwareMap, telemetry);
        systems = new Systems(drive, forkLift, jewelArm, phone, hardwareMap, telemetry);
        telemetry.addLine("NOW YOU CAN PRESS PLAY"); telemetry.update();
        waitForStart();
        systems.testFindingGlyphs();
        sleep(2000);
    }
}
