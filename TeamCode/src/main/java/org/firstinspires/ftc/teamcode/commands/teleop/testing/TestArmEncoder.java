package org.firstinspires.ftc.teamcode.commands.teleop.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.DriveSystem;

/**
 * Created by Mahim on 12/19/2017.
 */

public class TestArmEncoder extends OpMode{
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
        telemetry.addLine("Motor Speeds");
        telemetry.addData("left wheel", this.driveSystem.getLeftSpeed());
        telemetry.addData("right wheel", this.driveSystem.getRightSpeed());
        telemetry.addLine("Arm Actuators");
        telemetry.addData("jewel arm position", this.armSystem.getJewelArmPosition());
        telemetry.addData("arm speed", this.armSystem.getArmMotorSpeed());
        telemetry.addData("arm position", this.armSystem.getArmEncoderPosition());
    }

    @Override
    public void stop() {
        driveSystem.stop();
    }
}
