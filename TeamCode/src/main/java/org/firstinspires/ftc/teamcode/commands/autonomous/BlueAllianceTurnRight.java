package org.firstinspires.ftc.teamcode.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.DriveSystem;

/**
 * Created by Mahim on 12/9/2017.
 */
@Autonomous(name = "Blue Alliance: drive forward right", group = "Blue Alliance")
public class BlueAllianceTurnRight extends LinearOpMode {
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

        while (opModeIsActive() & (count < 1)) {
            armSystem.setDownPosition();
            sleep(1000);
            knockDownRedJewel();
            driveSystem.stop();
            sleep(1000);
            this.armSystem.setInitialPosition();
            sleep(2000);
            this.driveSystem.drive(-0.9, -1.0); // turn right
            sleep(1000);
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