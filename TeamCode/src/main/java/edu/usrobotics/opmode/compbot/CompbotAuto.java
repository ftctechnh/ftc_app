package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    float buttonPressingDistance = 5f;
    float initialButtonPressingDistance = 12f;
    Goal<Integer> encoderGoal7 = new Goal<> (robot.inchesToEncoderTicks(buttonPressingDistance));
    Goal<Integer> encoderGoal8 = new Goal<> (robot.inchesToEncoderTicks(buttonPressingDistance));

    boolean onSecondBeacon = false;
    int numTimesHitBeacon = 1;

    Task checkIfCorrectColor = null;

    @Override
    public void init() {

        robot.init(hardwareMap);

        final Route happyTrail = new Route();

        robot.setDirection(CompbotHardware.MovementDirection.NORTH);

        final int maxMotorSpeed = 50000;

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

        // Forward then turn the forward as an alternative to diagonals
        Goal<Integer> forward1EncoderGoal = new Goal<> (robot.inchesToEncoderTicks(6f));
        ConcurrentTaskSet forward = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted(0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }
        };

        // Diagonal to the beacon wall
        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(100f));
        ConcurrentTaskSet crabToLineBlue = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal3, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal3, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal3, maxMotorSpeed, 0f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal3, maxMotorSpeed, 0f, 0.7f, null, 0.1f)

        ) {
            @Override
            public boolean onExecuted() {
                return robot.sensingWhite(robot.bottomRightColorSensor) || robot.sensingWhite(robot.bottomLeftColorSensor);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH_EAST);

            }

        };

        // Diagonal to the beacon wall
        ConcurrentTaskSet crabToLineRed = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal3, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal3, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal3, maxMotorSpeed, 0f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal3, maxMotorSpeed, 0f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return robot.sensingWhite(robot.bottomRightColorSensor) || robot.sensingWhite(robot.bottomLeftColorSensor);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }

        };

        Goal<Integer> encoderGoal3AndAHalf = new Goal<> (robot.inchesToEncoderTicks(3f));
        ConcurrentTaskSet calibrationBackUp = new ConcurrentTaskSet(
                new MotorTask(robot.frontRight, encoderGoal3AndAHalf, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontLeft, encoderGoal3AndAHalf, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal3AndAHalf, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal3AndAHalf, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f)
        ) {

            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1) || robot.sensingWhite(robot.bottomRightColorSensor) || robot.sensingWhite(robot.bottomLeftColorSensor);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH);

            }
        };

        // Make the first approach to the beacon
        Goal<Integer> encoderGoal6 = new Goal<> (robot.inchesToEncoderTicks(initialButtonPressingDistance));
        final ConcurrentTaskSet beaconFirstApproachBlue = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.EAST);

            }

        };

        // Make the first hit to the beacon
        ConcurrentTaskSet beaconFirstApproachRed = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal6, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.WEST);

            }

        };

        // Make the next few hits to the beacon
        final ConcurrentTaskSet beaconHitBlue = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.EAST);

            }

        };

        final ConcurrentTaskSet beaconHitRed = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal7, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.WEST);

            }

        };

        final ConcurrentTaskSet backUpBlue = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal8, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal8, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal8, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal8, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f)
        ) {

            @Override
            public boolean onExecuted() {

                return isTaskCompleted (0) || isTaskCompleted(1);

            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.WEST);

            }

        };

        final ConcurrentTaskSet backUpRed = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal7, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal7, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal7, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal7, maxMotorSpeed, 0.2f, 0.7f, null, 0.1f)
        ) {

            @Override
            public boolean onExecuted() {

                return isTaskCompleted (0) || isTaskCompleted(1);

            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.EAST);

            }

        };

        final Goal<Integer> encoderGoal9 = new Goal<>(robot.inchesToEncoderTicks(60f));
        final ConcurrentTaskSet moveToSecondBeacon = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal9, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal9, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal9, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal9, maxMotorSpeed, 0.4f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1) || robot.sensingWhite(robot.bottomRightColorSensor) || robot.sensingWhite(robot.bottomLeftColorSensor);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }

            @Override
            public void onCompleted(){

                /*happyTrail.addTask(isBlueTeam ? beaconHitBlue : beaconHitRed);
                happyTrail.addTask(checkIfCorrectColor);*/

            }

        };

        checkIfCorrectColor = new Task() {
            boolean done = false;
            long startTime;

            // Called to update the task, return true if completed.
            public boolean execute (){

                if(startTime == 0){

                    return false;

                }

                if(System.currentTimeMillis() - startTime >= 5000){

                    done = true;

                }

                if(System.currentTimeMillis() - startTime >= 2000 && isColorGood()){

                    done = true;

                }

                if(numTimesHitBeacon > 2){

                    return true;

                }

                return done;

            }

            // Returns if the task is complete. It no longer needs updates.
            public boolean isCompleted (){

                return done;

            }

            // Returns the TaskType of this task.
            // Mostly used for debugging as Task Type can be set to anything regardless of the actual task purpose.
            public TaskType getType (){

                return TaskType.WAIT;

            }

            // Event fired when the State Machine reaches this task.
            public void onReached (){

                startTime = System.currentTimeMillis();

            }

            // Event fired when the State Machine updates this task. Return true if task was completed while executing.
            public boolean onExecuted (){

                return done;

            }

            // Event fired when task completed
            public void onCompleted (){

                numTimesHitBeacon++;

                if(numTimesHitBeacon <= 2){

                    if (!isColorGood()) {

                        if (isBlueTeam) {
                            encoderGoal7 = new Goal<>(robot.inchesToEncoderTicks(buttonPressingDistance));
                            happyTrail.addTask(beaconHitBlue);
                            encoderGoal8 = new Goal<>(robot.inchesToEncoderTicks(buttonPressingDistance));
                            happyTrail.addTask(this);

                        } else {
                            encoderGoal7 = new Goal<>(robot.inchesToEncoderTicks(buttonPressingDistance));
                            happyTrail.addTask(beaconHitRed);
                            happyTrail.addTask(this);

                        }
                    }

                    else{

                        if(!onSecondBeacon){

                            encoderGoal8 = new Goal<> (robot.inchesToEncoderTicks(buttonPressingDistance * 2f));
                            happyTrail.addTask(backUpBlue);
                            happyTrail.addTask(moveToSecondBeacon);

                        }

                        onSecondBeacon = true;

                    }

                }

                else{

                    happyTrail.addTask(moveToSecondBeacon);

                }

            }

        };

        happyTrail.addTask(shoot);
        happyTrail.addTask(forward);
        happyTrail.addTask(isBlueTeam ? crabToLineBlue : crabToLineRed);
        happyTrail.addTask(calibrationBackUp);
        happyTrail.addTask(isBlueTeam ? beaconFirstApproachBlue : beaconFirstApproachRed);
        happyTrail.addTask(isBlueTeam ? backUpBlue : backUpRed);
        happyTrail.addTask(checkIfCorrectColor);

        addRoute(happyTrail);

        super.init();

    }

    private boolean isColorGood() {
        return (isBlueTeam && robot.buttonPresserColorSensor.blue() > robot.buttonPresserColorSensor.red()) || (!isBlueTeam && robot.buttonPresserColorSensor.red() > robot.buttonPresserColorSensor.blue());
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