package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.userHardware.DoubleTelemetry;

@Autonomous(name = "BoogieWheel Auton Routes", group = "New")
//@Disabled

public class BoogieAutonRoutes extends AbstractAuton {

    Robot robot;

    @Override
    public void Init() {
        telemetry.setLogMode(DoubleTelemetry.LogMode.TRACE);
        robot = new Robot();
    }

    @Override
    public void Run() {

        robot.driveTo(48,1);

        robot.turnTo(90,1,5,100);

    }
}
