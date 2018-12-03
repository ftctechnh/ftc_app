package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractTeleop;

@TeleOp(name = "TwoGamepad BoogieWheel Teleop Tankdrive", group = "New")
//@Disabled

public class TwoGamepadBoogieTeleopTankdrive extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        twoGamepads();
    }

    private void twoGamepads() {
        ////////////////Gamepad 1////////////////
        ////////Drive////////
        //THIS CODE HAS BEEN MODIFIED FOR TANKDRIVE
        addEventHandler("1_lsy_change", () -> {
            robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            return true;
        });

        addEventHandler("1_rsx_change", () -> {
            robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            return true;
        });

        ////////Intake////////
        addEventHandler("1_a_down", robot.finishIntakingCallable());

        addEventHandler("1_b_down", robot.beginIntakingCallable());

        addEventHandler("1_x_down", robot.reverseIntakeCallable());



        ////////////////Gamepad 2////////////////
        ///////Mineral Lift////////
        addEventHandler("2_rt_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("2_lt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("2_x_down", robot.toggleMineralGateCallable());

        ////////Robot Lift////////
        addEventHandler("2_dpu_down", robot.robotLiftUpCallable());

        addEventHandler("2_dpd_down", robot.robotLiftDownCallable());

        addEventHandler("2_dpu_up", robot.robotLiftStopCallable());

        addEventHandler("2_dpd_up", robot.robotLiftStopCallable());
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
        robot.updateAll();
    }

    @Override
    public void Stop() {
        robot.stop();
    }

}