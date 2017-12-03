package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.systems.ArmSystem;
import org.firstinspires.ftc.teamcode.systems.DriveSystem;

/**
 * Created by Mahim on 11/4/2017.
 */

@TeleOp
public class TeleOPDrive extends OpMode {
    private DriveSystem driveSystem;
    private ArmSystem armSystem;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        this.driveSystem = new DriveSystem(hardwareMap, gamepad1);
        this.armSystem = new ArmSystem(hardwareMap);
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
        } else if (gamepad2.dpad_down) {
            this.armSystem.goDown();
        }

        telemetry.addData("left speed", driveSystem.getLeftSpeed());
        telemetry.addData("right speed", driveSystem.getRightSpeed());
        telemetry.addData("left servo position", armSystem.getleftServoPosition());
        telemetry.addData("right servo position", armSystem.getRightServoPosition());
        telemetry.addData("arm motor", armSystem.getArmMotorSpeed());
    }

    @Override
    public void stop() {
        driveSystem.stop();
    }
}