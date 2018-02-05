package org.firstinspires.ftc.teamcode.OldRobot.commands.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OldRobot.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.OldRobot.systems.DriveSystem;

/**
 * Created by Mahim on 11/4/2017.
 */
@Disabled
@TeleOp(name = "TeleOP Drive: Umayer", group = "old robot")
public class RCDrive extends OpMode {
    private DriveSystem driveSystem;
    private ArmSystem armSystem;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        this.driveSystem = new DriveSystem(hardwareMap, gamepad1);
        this.armSystem = new ArmSystem(hardwareMap);
        this.armSystem.init();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() { runtime.reset(); }

    @Override
    public void loop() {
        this.driveSystem.rcCarDrive();
        this.armSystem.setClaw(gamepad1.right_trigger);
        if (gamepad1.dpad_up) {
            this.armSystem.goUp();
        } else {
            armSystem.stopArm();
        }
        if (gamepad1.dpad_down) {
            this.armSystem.goDown();
        } else {
            this.armSystem.stopArm();
        }
        telemetry();
    }

    public void telemetry() {
        telemetry.addData("left wheel", this.driveSystem.getLeftSpeed());
        telemetry.addData("right wheel", this.driveSystem.getRightSpeed());
        telemetry.addData("arm speed", this.armSystem.getArmMotorSpeed());
        telemetry.addData("angle", this.driveSystem.getAngle());
        telemetry.addData("dpad up", gamepad1.dpad_up);
        telemetry.addData("dpad down", gamepad1.dpad_down);
    }

    @Override
    public void stop() {
        driveSystem.stop();
    }
}