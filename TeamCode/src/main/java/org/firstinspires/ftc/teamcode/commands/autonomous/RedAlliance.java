package org.firstinspires.ftc.teamcode.commands.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.DriveSystem;

/**
 * Created by Mahim on 12/8/17.
 */
@Autonomous
public class RedAlliance extends OpMode {
    private DriveSystem driveSystem;
    private ArmSystem armSystem;

    @Override
    public void init() {
        this.driveSystem = new DriveSystem(hardwareMap,gamepad1);
        this.armSystem = new ArmSystem(hardwareMap);
        this.armSystem.init();
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {


        if (armSystem.isBlue()) {
//            driveSystem.drive(-1.0, -1.0);
            driveSystem.driveByTime(getRuntime(), .5, -1.0);
        } else if (armSystem.isRed()) {
//            driveSystem.drive(1.0, 1.0);
            driveSystem.driveByTime(getRuntime(), .5, -1.0);
        }
    }

    @Override
    public void loop() {
        telemetry.addData("isBlue", armSystem.isBlue());
        telemetry.addData("isRed", armSystem.isRed());

    }

    @Override
    public void stop() {

    }
}
