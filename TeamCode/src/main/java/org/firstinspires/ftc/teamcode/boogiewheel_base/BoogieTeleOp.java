package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractTeleop;

@TeleOp(name = "BoogieWheel Teleop", group = "New")
//@Disabled

public class BoogieTeleOp extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        addEventHandler("1_lsy_change", () -> {
            robot.setDriveY(gamepad1.left_stick_y);
            return true;
        });

        addEventHandler("1_rsx_change", () -> {
            robot.setDriveZ(gamepad1.right_stick_x);
            return true;
        });

        addEventHandler("1_b_down", robot.finishIntakingCallable());

        addEventHandler("1_a_down", robot.beginIntakingCallable());

        addEventHandler("1_y_down", robot.reverseIntakeCallable());

        //addEventHandler("1_x_down", robot.moveMineralLiftToCollectPositionCallable());

        //addEventHandler("1_y_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("1_rt_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("1_lt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("1_x_down", robot.toggleMineralGateCallable());

        addEventHandler("1_dpu_down", robot.moveRobotLiftToTopCallable());

        addEventHandler("1_dpd_down", robot.moveRobotLiftToBottomCallable());

        /*addEventHandler("lt_change", ()->{
            if(gamepad1.left_trigger>0.5) robot.moveMineralLiftToCollectPosition();
            return true;
        });

        addEventHandler("rt_change", ()->{
            if(gamepad1.right_trigger>0.5) robot.moveMineralLiftToDumpPosition();
            return true;
        });*/
    }

    @Override
    public void UpdateEvents() {
        //NEVER EVER PUT BLOCKING CODE HERE!!!
        checkBooleanInput("lt", gamepad1.left_trigger > 0.5);
        checkBooleanInput("rt", gamepad1.right_trigger > 0.5);
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
    public void Stop() {
        robot.stop();
    }
}
