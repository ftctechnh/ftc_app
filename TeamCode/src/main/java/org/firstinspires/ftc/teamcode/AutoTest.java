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
        drive = new AutoDrive(hardwareMap, telemetry);
        phone = new Phone(hardwareMap,telemetry);
        phone.closeVuforia();
        forkLift = new ForkLift(hardwareMap, telemetry);
        jewelArm = new JewelArm(hardwareMap, telemetry);
        systems = new Systems(drive, forkLift, jewelArm, phone, hardwareMap, telemetry);
        telemetry.addLine("ready to start");
        telemetry.update();
        waitForStart();
        systems.glyphDetector.enable();
        sleep(1000);
        while(opModeIsActive()) {
            telemetry.addData("yPos", systems.glyphDetector.getYPos());
            telemetry.update();
        }
        sleep(2000);
    }
}
