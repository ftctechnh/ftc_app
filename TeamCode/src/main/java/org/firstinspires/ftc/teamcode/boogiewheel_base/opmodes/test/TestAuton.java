package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractAutonNew;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;
import org.firstinspires.ftc.teamcode.framework.util.State;
import org.upacreekrobotics.dashboard.Dashboard;

@Autonomous(name = "Test Auton", group = "New")
@Disabled

public class TestAuton extends AbstractAutonNew {
    @Override
    public void RegisterStates() {

        //Previous State is the name of the state which should call this one when it is finished
        addState(new State("first", "start", () -> {
            telemetry.addData(DoubleTelemetry.LogMode.INFO, "Running state: first");
            telemetry.update();
            delay(1000);
            return true;
        }));

        addState(new State("second", "first", () -> {
            telemetry.addData(DoubleTelemetry.LogMode.INFO, "Running state: second");
            telemetry.update();
            delay(1000);
            return true;
        }));

        addState(new State("third", "second", () -> {
            telemetry.addData(DoubleTelemetry.LogMode.INFO, "Running state: third");
            telemetry.update();
            delay(1000);
            return true;
        }));
    }

    @Override
    public void Init() {

    }

    @Override
    public void Run() {

    }

    @Override
    public void Stop() {

    }
}
