package edu.usrobotics.opmode.protobot;

import edu.usrobotics.opmode.LoggedOp;
import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.task.ConcurrentTaskSet;
import edu.usrobotics.opmode.task.Goal;
import edu.usrobotics.opmode.task.MotorTask;

/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public abstract class ProtobotAuto extends RobotOp {

    ProtobotHardware robot = new ProtobotHardware();
    private final boolean isBlueTeam;

    public ProtobotAuto (boolean isBlueTeam) {
        this.isBlueTeam = isBlueTeam;
    }

    @Override
    public void init () {

        robot.init(hardwareMap);


        final Route happyTrail = new Route();

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

                robot.setDirection(ProtobotHardware.MovementDirection.TURN_RIGHT);
            }
        };

        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(43.6f));
        ConcurrentTaskSet crab1 = new ConcurrentTaskSet( // this is crabbing
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

        Goal<Integer> encoderGoal4 = new Goal<> (robot.inchesToEncoderTicks(17));
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

                robot.setDirection(ProtobotHardware.MovementDirection.SOUTH_WEST);
            }
        };

        // MOVE TO OTHER BUTTON
        Goal<Integer> encoderGoal5 = new Goal<> (robot.inchesToEncoderTicks(4.5f));
        final ConcurrentTaskSet move_to_other_button = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal5, null, 0.5f, 0.7f, encoderGoal5, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal5, null, 0.5f, 0.7f, encoderGoal5, 0.1f),
                new MotorTask(robot.backRight, encoderGoal5, null, 0.5f, 0.7f, encoderGoal5, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal5, null, 0.5f, 0.7f, encoderGoal5, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.SOUTH);
            }
        };

        // HIT BUTTON WEST SIDE
        Goal<Integer> encoderGoal6 = new Goal<> (robot.inchesToEncoderTicks(5));
        final ConcurrentTaskSet hit_button_west = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal6, null, 0.5f, 0.7f, encoderGoal6, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal6, null, 0.5f, 0.7f, encoderGoal6, 0.1f),
                new MotorTask(robot.backRight, encoderGoal6, null, 0.5f, 0.7f, encoderGoal6, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal6, null, 0.5f, 0.7f, encoderGoal6, 0.1f)
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

        // LEAVE BUTTON WEST SIDE
        Goal<Integer> encoderGoal8 = new Goal<> (robot.inchesToEncoderTicks(5));
        final ConcurrentTaskSet leave_button_west = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal8, null, 0.5f, 0.7f, encoderGoal8, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal8, null, 0.5f, 0.7f, encoderGoal8, 0.1f),
                new MotorTask(robot.backRight, encoderGoal8, null, 0.5f, 0.7f, encoderGoal8, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal8, null, 0.5f, 0.7f, encoderGoal8, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.EAST);
            }
        };

        // MOVE TO NEXT BEACON
        Goal<Integer> encoderGoal7 = new Goal<> (robot.inchesToEncoderTicks(48f));
        final ConcurrentTaskSet move_to_next_beacon = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal7, null, 0.5f, 0.7f, encoderGoal7, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal7, null, 0.5f, 0.7f, encoderGoal7, 0.1f),
                new MotorTask(robot.backRight, encoderGoal7, null, 0.5f, 0.7f, encoderGoal7, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal7, null, 0.5f, 0.7f, encoderGoal7, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(ProtobotHardware.MovementDirection.SOUTH);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

                if (robot.colorSensor.red() > robot.colorSensor.blue() && isBlueTeam) { // If it is red and we are blue move forward
                    happyTrail.addTask(move_to_other_button);
                }

                happyTrail.addTask(hit_button_west);
                happyTrail.addTask(leave_button_west);

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

    @Override
    public void loop(){

        telemetry.addData("R,G,B", robot.colorSensor.red() + ", " + robot.colorSensor.green() + ", " + robot.colorSensor.blue());
        telemetry.addData("Red:", robot.colorSensor.red());
        telemetry.addData("Green:", robot.colorSensor.green());
        telemetry.addData("Blue:", robot.colorSensor.blue());
        telemetry.addData("Alpha:", robot.colorSensor.alpha());

        super.loop();

    }

}