package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;

@TeleOp(name="BoogieWheel Teleop", group="New")
//@Disabled

public class BoogieTeleOp extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        addEventHandler("lsy_change", ()->{
            robot.setDriveY(gamepad1.left_stick_y);
            return true;
        });

        addEventHandler("rsx_change", ()->{
            robot.setDriveZ(gamepad1.right_stick_x);
            return true;
        });

        addEventHandler("b_down", robot.finishIntakingCallable());

        addEventHandler("a_down", robot.beginIntakingCallable());

        addEventHandler("y_down", robot.reverseIntakeCallable());

        //addEventHandler("x_down", robot.moveMineralLiftToCollectPositionCallable());

        //addEventHandler("y_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("rt_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("lt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("x_down", robot.toggleMineralGateCallable());

        addEventHandler("dpu_down", robot.moveRobotLiftToTopCallable());

        addEventHandler("dpd_down", robot.moveRobotLiftToBottomCallable());


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
        checkBooleanInput("lt",gamepad1.left_trigger>0.5);
        checkBooleanInput("rt",gamepad1.right_trigger>0.5);
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
