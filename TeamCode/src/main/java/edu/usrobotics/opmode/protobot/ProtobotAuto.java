package edu.usrobotics.opmode.protobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.usrobotics.opmode.LoggedOp;
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


        Route happyTrail = new Route();

        robot.setDirection(ProtobotHardware.MovementDirection.NORTH);

        Goal<Integer> encoderGoal = new Goal<> (robot.inchesToEncoderTicks(24));
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal, null, 0.5f, 0.7f, encoderGoal, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal, null, 0.5f, 0.7f, encoderGoal, 0.1f),
                new MotorTask(robot.backRight, encoderGoal, null, 0.5f, 0.7f, encoderGoal, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal, null, 0.5f, 0.7f, encoderGoal, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.NORTH);
            }
        };

        Goal<Integer> encoderGoal2 = new Goal<> (robot.degreesToEncoderTicks(180));
        ConcurrentTaskSet turn1 = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal2, null, 0.5f, 0.7f, encoderGoal2, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal2, null, 0.5f, 0.7f, encoderGoal2, 0.1f),
                new MotorTask(robot.backRight, encoderGoal2, null, 0.5f, 0.7f, encoderGoal2, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal2, null, 0.5f, 0.7f, encoderGoal2, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();
                LoggedOp.debugOut="fsk";

                robot.setDirection(ProtobotHardware.MovementDirection.TURN_RIGHT);
            }
        };

        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(43f));
        ConcurrentTaskSet crab1 = new ConcurrentTaskSet( // A ~30˚ crab
                new MotorTask(robot.frontLeft, encoderGoal3, null, 0.5f, 0.7f, encoderGoal3, 0.1f),
                new MotorTask(robot.backRight, encoderGoal3, null, 0.5f, 0.7f, encoderGoal3, 0.1f)
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

        Goal<Integer> encoderGoal4 = new Goal<> (robot.inchesToEncoderTicks(24));
        ConcurrentTaskSet straif1 = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal4, null, 0.5f, 0.7f, encoderGoal4, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal4, null, 0.5f, 0.7f, encoderGoal4, 0.1f),
                new MotorTask(robot.backRight, encoderGoal4, null, 0.5f, 0.7f, encoderGoal4, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal4, null, 0.5f, 0.7f, encoderGoal4, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.WEST);
            }
        };

        happyTrail.addTask(forward);
        happyTrail.addTask(turn1);
        happyTrail.addTask(crab1);
        happyTrail.addTask(straif1);

        addRoute(happyTrail);

        //addTracker(new VuforiaTracker());

        super.init();
    }

    @Override
    public void start () {

        super.start();

    }

}
