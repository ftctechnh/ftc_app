package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.controller.TriggerType;

@TeleOp
public class TeleOpTestybois extends BaseTeleOpMode {

    Telemetry.Item lastButtonTelemetryItem;
    public ElapsedTime elapsedTime = new ElapsedTime();
    private boolean superDrive;
    private boolean slowDrive = false;

    public TeleOpTestybois()
    {
        super("TeleOpTestybois");
    }

    @Override
    public void loop() {
        controller1.handle();
        controller2.handle();

        float rx = controller1.gamepad.right_stick_x;
        float ry = controller1.gamepad.right_stick_y;
        float lx = controller1.gamepad.left_stick_x;
        float ly = controller1.gamepad.left_stick_y;

        float coefficient = slowDrive == true ? 0.5f : 1f;
        //this.driveSystem.driveGodMode(rx, ry, lx, ly, coefficient); //*****//
        this.driveSystem.mecanumDrive(rx, ry, lx, ly, false);
    }

    @Override
    public void init() {
        this.initBaseSystems();
        controller1.setTriggerValue(TriggerType.LEFT, 0.5f);
        this.superDrive = false; //config.getBoolean("superDrive");
        lastButtonTelemetryItem = telemetry.addData("LastButton", "none");
    }

    @Override
    public void initButtons() {

    }
}
