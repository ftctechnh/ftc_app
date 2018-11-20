package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.DistanceSensor2m;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

import java.util.concurrent.Callable;

@TeleOp(name="BoogieWheel Teleop", group="New")
//@Disabled

public class BoogieTeleOp extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        addEvent("lsy_change", robot.setDriveY(gamepad1.left_stick_y));

        addEvent("rsx_change", robot.setDriveZ(gamepad1.right_stick_x));

        addEvent("a_down", robot.finishIntaking());

        addEvent("b_down", robot.beginIntaking());

        addEvent("lb_down", robot.moveMineralLiftToCollectPosition());

        addEvent("rb_down", robot.moveMineralLiftToDumpPosition());
    }

    @Override
    public void UpdateEvents() {

    }

    @Override
    public void Init() {
        robot = new Robot();
    }

    @Override
    public void Loop() {
        robot.updateDrive();
    }

    @Override
    public void Stop(){
        robot.stop();
    }
}
