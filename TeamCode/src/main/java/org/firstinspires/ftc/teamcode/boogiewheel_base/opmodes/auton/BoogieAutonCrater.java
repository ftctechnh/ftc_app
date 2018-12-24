package org.firstinspires.ftc.teamcode.boogiewheel_base.opmodes.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Constants;
import org.firstinspires.ftc.teamcode.boogiewheel_base.hardware.Robot;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAuton;
import org.firstinspires.ftc.teamcode.framework.opModes.AbstractAutonNew;
import org.firstinspires.ftc.teamcode.framework.util.State;

@Autonomous(name = "BoogieWheel Auton Crater", group = "New")
//@Disabled

public class BoogieAutonCrater extends AbstractAutonNew{

    Robot robot;

    @Override
    public void RegisterStates() {
        addState(new State("auton intake sequence", "start", robot.autonIntakeSequenceCallable()));
        addState(new State("auton release wheels sequence", "start", robot.autonReleaseWheelsSequenceCallable()));
    }

    @Override
    public void Init() {
        robot = new Robot();
    }

    @Override
    public void InitLoop(){
        //Sample minerals
    }

    @Override
    public void Run() {
        robot.runDrivePaths(Constants.collectRightMineral);

        //Release wheels
        //Lower robot
        robot.moveRobotLiftToBottom();
        //Reset mineral lift
        //Pick up mineral
        robot.runDrivePaths(Constants.unlatchRobot);
        robot.runDrivePaths(Constants.collectRightMineral);
        //Drive to depot
        //Drop off team marker in depot
        //Drive to crater
    }

    @Override
    public void Stop() {

    }
}
