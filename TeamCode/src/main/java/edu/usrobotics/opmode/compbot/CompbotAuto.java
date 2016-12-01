package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.usrobotics.opmode.LoggedOp;
import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.task.ConcurrentTaskSet;
import edu.usrobotics.opmode.task.Goal;
import edu.usrobotics.opmode.task.MotorTask;
import edu.usrobotics.opmode.task.Task;
import edu.usrobotics.opmode.task.TaskType;

/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public abstract class CompbotAuto extends RobotOp {

    CompbotHardware robot = new CompbotHardware();
    private final boolean isBlueTeam;

    public CompbotAuto (boolean isBlueTeam) {
        this.isBlueTeam = isBlueTeam;
    }

    @Override
    public void init () {

        robot.init(hardwareMap);


        final Route happyTrail = new Route();

        robot.setDirection(CompbotHardware.MovementDirection.NORTH);

        int maxMotorSpeed = 5000;

        Goal<Integer> encoderGoal = new Goal<> (robot.inchesToEncoderTicks(24 / 2));
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);
            }
        };


        Task shoot = new Task() {
            ElapsedTime shooterTime;
            boolean completed = false;

            @Override
            public boolean execute() {
                if (shooterTime.seconds() > 3)
                    return true;

                robot.shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.shooterRight.setPower(1);
                robot.shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.shooterLeft.setPower(1);

                robot.harvester.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                robot.harvester.setPower(0.5f);

                return false;
            }

            @Override
            public boolean isCompleted() {
                return completed;
            }

            @Override
            public TaskType getType() {
                return TaskType.MOTOR;
            }

            @Override
            public void onReached() {
                shooterTime = new ElapsedTime();
            }

            @Override
            public boolean onExecuted() {
                return false;
            }

            @Override
            public void onCompleted() {
                robot.shooterRight.setPower(0);
                robot.shooterLeft.setPower(0);
                robot.harvester.setPower(0);

                completed = true;
            }
        };

        // CRAB DIAGONAL TO BEACON
        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(43.6f));
        ConcurrentTaskSet crab1 = new ConcurrentTaskSet( // this is crabbing
                new MotorTask(robot.frontLeft, encoderGoal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH_WEST);
            }
        };

        // MOVE TO OTHER BUTTON
        Goal<Integer> encoderGoal5 = new Goal<> (robot.inchesToEncoderTicks(4.5f));
        final ConcurrentTaskSet move_to_other_button = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH);
            }
        };

        // HIT BUTTON WEST SIDE
        Goal<Integer> encoderGoal6 = new Goal<> (robot.inchesToEncoderTicks(5));
        final ConcurrentTaskSet hit_button_west = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.WEST);
            }
        };

        // LEAVE BUTTON WEST SIDE
        Goal<Integer> encoderGoal8 = new Goal<> (robot.inchesToEncoderTicks(5));
        final ConcurrentTaskSet leave_button_west = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.EAST);
            }
        };

        // MOVE TO NEXT BEACON
        Goal<Integer> encoderGoal7 = new Goal<> (robot.inchesToEncoderTicks(48f));
        final ConcurrentTaskSet move_to_next_beacon = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH);
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

        // STRAFE TO BEACON BLUE
        Goal<Integer> encoderGoal4 = new Goal<> (robot.inchesToEncoderTicks(17));
        ConcurrentTaskSet strafeBlue = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH_WEST);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

                if (robot.colorSensor.red() > robot.colorSensor.blue() && isBlueTeam) { // If it is red and we are blue move forward
                    happyTrail.addTask(move_to_other_button);
                }

                happyTrail.addTask(hit_button_west);
                happyTrail.addTask(leave_button_west);
                happyTrail.addTask(move_to_next_beacon);
            }
        };

        // STRAFE TO BEACON BLUE
        Goal<Integer> encoderGoal18 = new Goal<> (robot.inchesToEncoderTicks(17));
        ConcurrentTaskSet strafeRed = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH_WEST);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();

                if (robot.colorSensor.red() > robot.colorSensor.blue() && isBlueTeam) { // If it is red and we are blue move forward
                    happyTrail.addTask(move_to_other_button);
                }

                happyTrail.addTask(hit_button_west);
                happyTrail.addTask(leave_button_west);
                happyTrail.addTask(move_to_next_beacon);
            }
        };

        happyTrail.addTask(forward);
        happyTrail.addTask(shoot);
        happyTrail.addTask(forward);
        happyTrail.addTask(crab1);
        happyTrail.addTask(isBlueTeam ? strafeBlue : strafeRed);

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
        telemetry.addData("FR,FL,BR,BL", robot.frontRight.getCurrentPosition() + ", " + robot.frontLeft.getCurrentPosition() + ", " + robot.backRight.getCurrentPosition() + ", " + robot.backLeft.getCurrentPosition());

        super.loop();

    }

}