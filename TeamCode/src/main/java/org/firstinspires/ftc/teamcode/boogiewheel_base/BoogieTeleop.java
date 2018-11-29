package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractTeleop;

@TeleOp(name = "BoogieWheel Teleop", group = "New")
//@Disabled

public class BoogieTeleop extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        ////////Drive////////
        addEventHandler("1_lsy_change", () -> {
            robot.setDriveY(gamepad1.left_stick_y);
            return true;
        });

        addEventHandler("1_rsx_change", () -> {
            robot.setDriveZ(gamepad1.right_stick_x);
            return true;
        });

        ////////Intake////////
        addEventHandler("1_b_down", robot.finishIntakingCallable());

        addEventHandler("1_a_down", robot.beginIntakingCallable());

        addEventHandler("1_y_down", robot.reverseIntakeCallable());

        ///////Mineral Lift////////
        addEventHandler("1_rt_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("1_lt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("1_x_down", robot.toggleMineralGateCallable());

        ////////Robot Lift////////
        addEventHandler("1_dpu_down", robot.robotLiftUpCallable());

        addEventHandler("1_dpd_down", robot.robotLiftDownCallable());

        addEventHandler("1_dpu_up", robot.robotLiftStopCallable());

        addEventHandler("1_dpd_up", robot.robotLiftStopCallable());
    }

    @Override
    public void UpdateEvents() {
        //NEVER EVER PUT BLOCKING CODE HERE!!!
        checkBooleanInput("1_lt", gamepad1.left_trigger > 0.5);
        checkBooleanInput("1_rt", gamepad1.right_trigger > 0.5);
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
