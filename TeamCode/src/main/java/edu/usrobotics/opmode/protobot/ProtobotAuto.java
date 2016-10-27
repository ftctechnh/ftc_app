package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.task.ConcurrentTaskSet;
import edu.usrobotics.opmode.task.MotorTask;
import edu.usrobotics.opmode.tracker.VuforiaTracker;

/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
@Autonomous(name="Protobot Auto", group="Protobot")
public class ProtobotAuto extends RobotOp {

    ProtobotHardware robot = new ProtobotHardware();

    @Override
    public void init () {

        robot.init(hardwareMap);

        robot.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        Route happyTrail = new Route();


        int encoderGoal = robot.inchesToEncoderTicks(48);
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal, null, 0.5f, 0.8f, null),//FIX DAMPING CHANGE 1f to 0.8f
                new MotorTask(robot.frontLeft, null, null, 0.5f, 0.8f, null),
                new MotorTask(robot.backRight, null, null, 0.5f, 0.8f, null),
                new MotorTask(robot.backLeft, null, null, 0.5f, 0.8f, null)

        ) {
            @Override
            public boolean onExecuted() {
                debugOut = robot.frontRight.getCurrentPosition() + " " + isTaskCompleted(0) + "" + isTaskCompleted(1) + "" + isTaskCompleted(2) + "" + isTaskCompleted(3);
                return isTaskCompleted(0);
            }

        };

        happyTrail.addTask(forward);

        addRoute(happyTrail);

        addTracker(new VuforiaTracker());

        super.init();
    }

    @Override
    public void start () {

        super.start();

    }

}
