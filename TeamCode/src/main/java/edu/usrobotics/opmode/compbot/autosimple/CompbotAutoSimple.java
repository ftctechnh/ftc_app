package edu.usrobotics.opmode.compbot.autosimple;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.usrobotics.opmode.LoggedOp;
import edu.usrobotics.opmode.RobotOp;
import edu.usrobotics.opmode.Route;
import edu.usrobotics.opmode.compbot.CompbotHardware;
import edu.usrobotics.opmode.task.ConcurrentTaskSet;
import edu.usrobotics.opmode.task.Goal;
import edu.usrobotics.opmode.task.MotorTask;
import edu.usrobotics.opmode.task.Task;
import edu.usrobotics.opmode.task.TaskType;

/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public abstract class CompbotAutoSimple extends RobotOp {

    CompbotHardware robot = new CompbotHardware();
    private final boolean isBlueTeam;

    public CompbotAutoSimple(boolean isBlueTeam) {
        this.isBlueTeam = isBlueTeam;
    }

    @Override
    public void init () {

        robot.init(hardwareMap);

        final Route happyTrail = new Route();

        robot.setDirection(CompbotHardware.MovementDirection.NORTH);

        int maxMotorSpeed = 5000000;

        /*Goal<Integer> encoderGoal = new Goal<> (robot.inchesToEncoderTicks(12));
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                LoggedOp.debugOut = robot.frontLeft.getCurrentPosition()  + ", " + robot.frontRight.getCurrentPosition()  + ", " + isTaskCompleted (1) + ", " + isTaskCompleted (0) + " " + System.currentTimeMillis();
                return isTaskCompleted (0) || isTaskCompleted (1) || isTaskCompleted (2) || isTaskCompleted (3);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);
            }

            @Override
            public void onCompleted () {
                super.onCompleted();
                //LoggedOp.debugOut = "cpleted";
            }
        };*/

        // CRAB DIAGONAL TO BEACON
        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(60f));
        ConcurrentTaskSet crab1Blue = new ConcurrentTaskSet( // this is crabbing

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

                if(isBlueTeam){

                    robot.setDirection(CompbotHardware.MovementDirection.NORTH_EAST);

                }

                else{

                    robot.setDirection(CompbotHardware.MovementDirection.NORTH_WEST);

                }

            }
        };

        // CRAB DIAGONAL TO BEACON
        ConcurrentTaskSet crab1Red = new ConcurrentTaskSet( // this is crabbing

                new MotorTask(robot.frontRight, encoderGoal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                if(isBlueTeam){

                    robot.setDirection(CompbotHardware.MovementDirection.NORTH_EAST);

                }

                else{

                    robot.setDirection(CompbotHardware.MovementDirection.NORTH_WEST);

                }

            }
        };

        Goal<Integer> better_encoder_goal3 = new Goal<> (robot.inchesToEncoderTicks(53));
        ConcurrentTaskSet back = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, better_encoder_goal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, better_encoder_goal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, better_encoder_goal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, better_encoder_goal3, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
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
            public void onCompleted () {
                super.onCompleted();
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

        Goal<Integer> better_encoder_goal4 = new Goal<> (robot.inchesToEncoderTicks(53));
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, better_encoder_goal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, better_encoder_goal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, better_encoder_goal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, better_encoder_goal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
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

            @Override
            public void onCompleted () {
                super.onCompleted();
            }
        };

        happyTrail.addTask(isBlueTeam ? crab1Blue : crab1Red);
        happyTrail.addTask(back);
        happyTrail.addTask(shoot);
        happyTrail.addTask(forward);

        addRoute(happyTrail);

        //addTracker(new VuforiaTracker());

        super.init();

    }

    @Override
    public void start () {

        robot.start();

        super.start();

    }

    @Override
    public void loop(){

        telemetry.addData("buttonPresserColorSensor", robot.buttonPresserColorSensor.red() + " " + robot.buttonPresserColorSensor.green() + " " + robot.buttonPresserColorSensor.blue());

        telemetry.addData("bottomFrontColorSensor", robot.bottomFrontColorSensor.red() + " " + robot.bottomFrontColorSensor.green() + " " + robot.bottomFrontColorSensor.blue());
        telemetry.addData("bottomBackColorSensor", robot.bottomBackColorSensor.red() + " " + robot.bottomBackColorSensor.green() + " " + robot.bottomBackColorSensor.blue());
        telemetry.addData("bottomRightColorSensor", robot.bottomRightColorSensor.red() + " " + robot.bottomRightColorSensor.green() + " " + robot.bottomRightColorSensor.blue());
        telemetry.addData("bottomLeftColorSensor", robot.bottomLeftColorSensor.red() + " " + robot.bottomLeftColorSensor.green() + " " + robot.bottomLeftColorSensor.blue());

        super.loop();

    }

}