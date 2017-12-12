package org.firstinspires.ftc.teamcode.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.DriveSystem;

/**
 * Created by Mahim on 12/9/2017.
 */
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
            sleep(1000);
            knockDownRedJewel();
            driveSystem.stop();
            sleep(1000);
            this.armSystem.setIntitialPosition();
            sleep(2000);
            this.driveSystem.drive(1.0, 0.8); // turn left backwards
            sleep(2000);
            count++;
        }
    }

    private void knockDownRedJewel() {
        this.armSystem.enableColorSensor();
        if(armSystem.isRed()) {
            this.driveSystem.driveForward(1.0);
            sleep(500);
        } else if (armSystem.isBlue()) {
            this.driveSystem.driveBackwards(1.0);
            sleep(500);
        }
    }
}