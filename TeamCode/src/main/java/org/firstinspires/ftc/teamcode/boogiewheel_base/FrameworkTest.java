package org.firstinspires.ftc.teamcode.boogiewheel_base;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.framework.AbstractTeleop;

@TeleOp(name="test_teleop", group="New")
//@Disabled

public class FrameworkTest extends AbstractTeleop{

    ElapsedTime runTime;

    @Override
    public void RegisterEvents() {
        addEvent("even_down", () -> {
            telemetry.addData("even");
            telemetry.update();
            return true;
        });

        addEvent("odd_down", () -> {
            telemetry.addData("odd");
            telemetry.update();
            return true;
        });
    }

    @Override
    public void UpdateEvents() {
        boolean even = (((int)runTime.milliseconds())%2==0);
        telemetry.update();
        checkBooleanInput("even",even);
        checkBooleanInput("odd",!even);
    }

    @Override
    public void Init() {
        runTime = new ElapsedTime();
        runTime.reset();
    }

    @Override
    public void Loop() {

    }
}
