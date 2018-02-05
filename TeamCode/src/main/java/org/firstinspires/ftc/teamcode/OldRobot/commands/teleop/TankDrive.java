package org.firstinspires.ftc.teamcode.OldRobot.commands.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OldRobot.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.OldRobot.systems.DriveSystem;

/**
 * Created by Mahim on 12/9/2017.
 */
@Disabled
@TeleOp(name = "TeleOP Drive: Urid", group = "old robot")
public class TankDrive extends OpMode {
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
        this.driveSystem.tankDrive();
        this.armSystem.setClaw(gamepad2.right_trigger);
        if (gamepad2.dpad_up) {
            this.armSystem.goUp();
        } else {
            armSystem.stopArm();
        }
        if (gamepad2.dpad_down) {
            this.armSystem.goDown();
        } else {
            this.armSystem.stopArm();
        }
        telemetry.addData("left wheel", this.driveSystem.getLeftSpeed());
        telemetry.addData("right wheel", this.driveSystem.getRightSpeed());
        telemetry.addData("angle", this.driveSystem.getAngle());
        telemetry.addLine("acceleration: " + this.driveSystem.getAcceleration());
    }

    @Override
    public void stop() {
        driveSystem.stop();
    }
}
