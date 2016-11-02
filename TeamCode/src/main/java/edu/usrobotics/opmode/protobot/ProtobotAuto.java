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

        robot.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        Route happyTrail = new Route();


        int encoderGoal = robot.inchesToEncoderTicks(64);
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal, null, 0.5f, 0.8f, encoderGoal, 0.2f),
                new MotorTask(robot.frontLeft, encoderGoal, null, 0.5f, 0.8f, encoderGoal, 0.2f),
                new MotorTask(robot.backRight, encoderGoal, null, 0.5f, 0.8f, encoderGoal, 0.2f),
                new MotorTask(robot.backLeft, encoderGoal, null, 0.5f, 0.8f, encoderGoal, 0.2f)
        );

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
