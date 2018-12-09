package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.teleop;

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
        /*addEventHandler("1_lsy_change", () -> {
            robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            return true;
        });

        addEventHandler("1_rsx_change", () -> {
            robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            return true;
        });*/

        ////////////////Gamepad 2////////////////
        ////////Intake////////
        addEventHandler("2_a_down", robot.finishIntakingCallable());

        addEventHandler("2_b_down", robot.beginIntakingCallable());

        addEventHandler("2_x_down", robot.reverseIntakeCallable());

        addEventHandler("2_dpr_down", robot.liftIntakeCallable());

        addEventHandler("2_dpl_down", robot.lowerIntakeCallable());

        ///////Mineral Lift////////
        addEventHandler("2_rt_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("2_lt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("2_y_down", robot.toggleMineralGateCallable());

        ////////Robot Lift////////
        addEventHandler("2_dpu_down", robot.robotLiftUpCallable());

        addEventHandler("2_dpd_down", robot.robotLiftDownCallable());

        addEventHandler("2_dpu_up", robot.robotLiftStopCallable());

        addEventHandler("2_dpd_up", robot.robotLiftStopCallable());
    }

    @Override
    public void UpdateEvents() {
        //NEVER EVER PUT BLOCKING CODE HERE!!!
        checkBooleanInput("2_lt", gamepad2.left_trigger > 0.5);
        checkBooleanInput("2_rt", gamepad2.right_trigger > 0.5);
    }

    @Override
    public void Init() {
        robot = new Robot();
    }

    @Override
    public void Loop() {
        robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        robot.updateAll();
        telemetry.update();
    }

    @Override
    public void Stop() {
        robot.stop();
    }

}