package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.usrobotics.opmode.LoggedOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.ConcurrentTaskSet;
import edu.usrobotics.opmode.MotorTask;

/**
 * Created by dsiegler19 on 10/13/16.
 */
@Autonomous(name="Protobot Auto", group="Protobot")
public class ProtobotAuto extends LoggedOp {
    ProtobotHardware robot = new ProtobotHardware();

    @Override
    public void init () {
        super.init();

        robot.init(hardwareMap);
    }

    @Override
    public void start () {
        super.start();

        Route happyTrail = new Route ();

        ConcurrentTaskSet forward = new ConcurrentTaskSet (
                new MotorTask(robot.frontRight, null, null, 0.5d, 0.8f),
                new MotorTask(robot.frontLeft, null, null, 0.5d, 0.8f),
                new MotorTask(robot.backRight, null, null, 0.5d, 0.8f),
                new MotorTask(robot.backLeft, null, null, 0.5d, 0.8f)
        );

        happyTrail.addTask (forward);

        addRoute(happyTrail);
    }
}
