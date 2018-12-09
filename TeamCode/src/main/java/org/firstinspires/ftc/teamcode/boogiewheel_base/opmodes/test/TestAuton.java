package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAutonNew;
import org.firstinspires.ftc.teamcode.framework.util.State;

@Autonomous(name = "Test Auton", group = "New")
//@Disabled

public class TestAuton extends AbstractAutonNew {
    @Override
    public void RegisterStates() {

        //Previous State is the name of the state which should call this one when it is finished
        addState(new State("first", "start", () -> {
            telemetry.addData("Running state: first");
            telemetry.update();
            return true;
        }));

        addState(new State("second", "first", () -> {
            telemetry.addData("Running state: second");
            telemetry.update();
            return true;
        }));

        addState(new State("first", "second", () -> {
            telemetry.addData("Running state: first");
            telemetry.update();
            return true;
        }));

        addState(new State("third", "second", () -> {
            telemetry.addData("Running state: third");
            telemetry.update();
            return true;
        }));
    }

    @Override
    public void Init() {

    }

    @Override
    public void Stop() {

    }
}
