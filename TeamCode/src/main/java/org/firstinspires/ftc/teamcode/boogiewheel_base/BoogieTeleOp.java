package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;
import org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.DistanceSensor2m;
import org.firstinspires.ftc.teamcode.framework.userHardware.outputs.SlewDcMotor;

import java.util.concurrent.Callable;

@TeleOp(name="boggiewheel_teleop", group="New")
//@Disabled

public class BoogieTeleOp extends AbstractTeleop {

    private Robot robot;
    private SlewDcMotor intakeMotor;

    @Override
    public void RegisterEvents() {
        addEvent("lsy_change", () -> {
            robot.setDriveY(gamepad1.left_stick_y);
            return true;
        });

        addEvent("rsx_change", () -> {
            robot.setDriveZ(gamepad1.right_stick_x);
            return true;
        });

        addEvent("a_down", () -> {
            intakeMotor.setPower(-1);
            return true;
        });

        addEvent("a_down", () -> {
            intakeMotor.setPower(0);
            return true;
        });
    }

    @Override
    public void UpdateEvents() {

    }

    @Override
    public void Init() {
        robot = new Robot();
        intakeMotor = new SlewDcMotor(hardwareMap.dcMotor.get("intake"));
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void Loop() {

    }

    @Override
    public void Stop(){
        robot.stop();
    }
}
