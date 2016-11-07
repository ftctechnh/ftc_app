package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.task.ConcurrentTaskSet;
import edu.usrobotics.opmode.task.Goal;
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

        robot.setDirection(ProtobotHardware.MovementDirection.NORTH);

        Route happyTrail = new Route();

        Goal<Integer> encoderGoal = new Goal<> (robot.inchesStraifingToEncoderTicks(36));
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal, null, 0.5f, 0.7f, encoderGoal, 0.1f),
                new MotorTask(robot.frontLeft, null, null, 0.5f, 0.7f, encoderGoal, 0.1f),
                new MotorTask(robot.backRight, null, null, 0.5f, 0.7f, encoderGoal, 0.1f),
                new MotorTask(robot.backLeft, null, null, 0.5f, 0.7f, encoderGoal, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }


        };

        /*Goal<Integer> encoderGoal2 = new Goal<> (robot.degreesToEncoderTicks(180));
        ConcurrentTaskSet turn1 = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal2, null, 0.5f, 0.7f, encoderGoal2, 0.1f),
                new MotorTask(robot.frontLeft, null, null, 0.5f, 0.7f, encoderGoal2, 0.1f),
                new MotorTask(robot.backRight, null, null, 0.5f, 0.7f, encoderGoal2, 0.1f),
                new MotorTask(robot.backLeft, null, null, 0.5f, 0.7f, encoderGoal2, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.SOUTH_WEST);
            }
        };

        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(5.6f));
        ConcurrentTaskSet crab1 = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal3, null, 0.5f, 0.7f, encoderGoal3, 0.1f),
                new MotorTask(robot.backRight, null, null, 0.5f, 0.7f, encoderGoal3, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.TURN_RIGHT);
            }
        };*/

        happyTrail.addTask(forward);
        //happyTrail.addTask(turn1);
        //happyTrail.addTask(crab1);

        addRoute(happyTrail);

        //addTracker(new VuforiaTracker());

        super.init();
    }

    @Override
    public void start () {

        super.start();

    }

}
