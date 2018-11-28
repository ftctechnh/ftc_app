package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractTeleop;

@TeleOp(name = "Framework Test Teleop", group = "New")
//@Disabled

public class FrameworkTest extends AbstractTeleop {

    ElapsedTime runTime;
    Robot robot;

    @Override
    public void RegisterEvents() {
        addEventHandler("even_down", () -> {
            telemetry.addData("even");
            //telemetry.update();
            return true;
        });

        addEventHandler("odd_down", () -> {
            telemetry.addData("odd");
            //telemetry.update();
            return true;
        });

        addEventHandler("a_down", () -> {
            telemetry.addData("a_down");
            telemetry.update();
            return true;
        });

        addEventHandler("a_up", () -> {
            telemetry.addData("a_up");
            telemetry.update();
            return true;
        });

        addEventHandler("b_down", () -> {
            telemetry.addData("b_down");
            telemetry.update();
            return true;
        });

        addEventHandler("b_up", () -> {
            telemetry.addData("b_up");
            telemetry.update();
            return true;
        });
    }

    @Override
    public void UpdateEvents() {
        boolean even = (((int) runTime.seconds()) % 2 == 0);
        //checkBooleanInput("even",even);
        checkBooleanInput("odd", !even);
        checkBooleanInput("even_down", even, 100);
    }

    @Override
    public void Init() {
        runTime = new ElapsedTime();
        runTime.reset();
        //robot = new Robot();
    }

    @Override
    public void Loop() {
    }

    @Override
    public void Stop() {

    }
}
