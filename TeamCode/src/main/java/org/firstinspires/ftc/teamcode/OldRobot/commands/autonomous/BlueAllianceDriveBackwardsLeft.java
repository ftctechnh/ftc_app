package org.firstinspires.ftc.teamcode.OldRobot.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OldRobot.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.OldRobot.systems.DriveSystem;

/**
 * Created by Mahim on 12/9/2017.
 */
@Disabled
@Autonomous(name = "Blue Alliance: Drive backwards left", group = "Blue Alliance")
public class BlueAllianceDriveBackwardsLeft extends LinearOpMode {
    private DriveSystem driveSystem;
    private ArmSystem armSystem;
    private ElapsedTime runtime = new ElapsedTime();

    private void initialize() {
        this.driveSystem = new DriveSystem(hardwareMap, gamepad1);
        this.armSystem = new ArmSystem(hardwareMap);
        this.armSystem.init();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        runtime.reset();
        int count = 0;

        while (opModeIsActive() && (count < 1)) {
            armSystem.setDownPosition();
            sleep(100);
            knockDownRedJewel();
            driveSystem.stop();
            sleep(1000);
            this.armSystem.setInitialPosition();
            sleep(2100);
            this.driveSystem.drive(.4, 0.2); // turn left backwards
            sleep(2100);
            count++;
        }
    }

    private void knockDownRedJewel() {
        this.armSystem.enableColorSensor();
        if(armSystem.isRed()) {
            this.driveSystem.driveForward(.4);
            sleep(500);
        } else if (armSystem.isBlue()) {
            this.driveSystem.driveBackwards(.4);
            sleep(500);
        }
    }
}