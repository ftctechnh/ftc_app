package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractTeleop;

@TeleOp(name = "OneDriver BoogieWheel Teleop", group = "New")
//@Disabled

public class OneDriverBoogieTeleop extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        singleGamepad();
    }

    private void singleGamepad() {

        ////////Drive////////
        // For these to work uncomment drive.update in robot.updateAll
        /* addEventHandler("1_lsy_change", () -> {
            robot.setDriveY(gamepad1.left_stick_y);
            return true;
        });

        addEventHandler("1_rsx_change", () -> {
            robot.setDriveZ(gamepad1.right_stick_x);
            return true;
        });*/

        addEventHandler("1_lsy_change", () -> {
            robot.setDriveY(-gamepad1.left_stick_y);
            return true;
        });

        addEventHandler("1_rsy_change", () -> {
            robot.setDriveZ(-gamepad1.right_stick_x);
            return true;
        });

        addEventHandler("1_lsb_down", robot.toggleDriveInvertedCallable());

        ////////Intake////////
        addEventHandler("1_a_down", robot.finishIntakingCallable());

        addEventHandler("1_b_down", robot.beginIntakingCallable());

        addEventHandler("1_x_down", robot.reverseIntakeCallable());

        addEventHandler("1_dpr_down", robot.liftIntakeCallable());

        addEventHandler("1_dpl_down", robot.lowerIntakeCallable());

        ///////Mineral Lift////////
        addEventHandler("1_rt_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("1_lt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("1_y_down", robot.toggleMineralGateCallable());

        ////////Robot Lift////////
        addEventHandler("1_dpu_down", robot.robotLiftUpCallable());

        addEventHandler("1_dpd_down", robot.robotLiftDownCallable());

        addEventHandler("1_dpu_up", robot.robotLiftStopCallable());

        addEventHandler("1_dpd_up", robot.robotLiftStopCallable());
    }

    private void twoGamepads() {
        ////////////////Gamepad 1////////////////
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

    private void twoDrivers() {
        ////////////////Gamepad 1////////////////
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
        addEventHandler("1_a_down", robot.finishIntakingCallable());

        addEventHandler("1_b_down", robot.beginIntakingCallable());

        addEventHandler("1_x_down", robot.reverseIntakeCallable());

        ////////Robot Lift////////
        addEventHandler("1_dpu_down", robot.robotLiftUpCallable());

        addEventHandler("1_dpd_down", robot.robotLiftDownCallable());

        addEventHandler("1_dpu_up", robot.robotLiftStopCallable());

        addEventHandler("1_dpd_up", robot.robotLiftStopCallable());

        ////////Util////////
        addEventHandler("1_y_down", () -> {
            ////Gamepad 1////
            //Drive
            pauseEvent("1_lsy_change");
            pauseEvent("1_rsx_change");

            //Intake
            pauseEvent("1_b_down");
            pauseEvent("1_a_down");
            pauseEvent("1_y_down");

            //Robot Lift
            pauseEvent("1_dpu_down");
            pauseEvent("1_dpd_down");
            pauseEvent("1_dpu_up");
            pauseEvent("1_dpd_up");

            //Util
            pauseEvent("1_y_down");

            ////Gamepad 2////
            //Drive
            resumeEvent("2_lsy_change");
            resumeEvent("2_rsx_change");

            //Mineral Lift
            resumeEvent("2_rt_down");
            resumeEvent("2_lt_down");
            resumeEvent("2_x_down");

            //Util
            resumeEvent("2_y_down");

            return true;
        });

        ////////////////Gamepad 2////////////////
        ////////Drive////////
        addEventHandler("2_lsy_change", () -> {
            robot.setDriveY(-gamepad1.left_stick_y);
            return true;
        });
        pauseEvent("2_lsy_change");

        addEventHandler("2_rsx_change", () -> {
            robot.setDriveZ(gamepad1.right_stick_x);
            return true;
        });
        pauseEvent("2_rsx_change");

        ///////Mineral Lift////////
        addEventHandler("2_rt_down", robot.moveMineralLiftToDumpPositionCallable());
        pauseEvent("2_rt_down");

        addEventHandler("2_lt_down", robot.moveMineralLiftToCollectPositionCallable());
        pauseEvent("2_lt_down");

        addEventHandler("2_x_down", robot.toggleMineralGateCallable());
        pauseEvent("2_x_down");

        ////////Util////////
        addEventHandler("2_y_down", () -> {
            ////Gamepad 1////
            //Drive
            resumeEvent("1_lsy_change");
            resumeEvent("1_rsx_change");

            //Intake
            resumeEvent("1_b_down");
            resumeEvent("1_a_down");
            resumeEvent("1_y_down");

            //Robot Lift
            resumeEvent("1_dpu_down");
            resumeEvent("1_dpd_down");
            resumeEvent("1_dpu_up");
            resumeEvent("1_dpd_up");

            //Util
            resumeEvent("1_y_down");

            ////Gamepad 2////
            //Drive
            pauseEvent("2_lsy_change");
            pauseEvent("2_rsx_change");

            //Mineral Lift
            pauseEvent("2_rt_down");
            pauseEvent("2_lt_down");
            pauseEvent("2_x_down");

            //Util
            pauseEvent("2_y_down");

            return true;
        });
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