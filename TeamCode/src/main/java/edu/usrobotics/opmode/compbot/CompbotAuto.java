package edu.usrobotics.opmode.compbot;

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
import edu.usrobotics.opmode.task.WaitTask;

/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public abstract class CompbotAuto extends RobotOp {

    CompbotHardware robot = new CompbotHardware();
    private final boolean isBlueTeam;

    public CompbotAuto (boolean isBlueTeam) {
        this.isBlueTeam = isBlueTeam;
    }

    Goal<Integer> encoderGoal7 = new Goal<> (robot.inchesToEncoderTicks(4f));

    int numTimesHitBeacon = 1;

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
        Goal<Integer> forward1EncoderGoal = new Goal<> (robot.inchesToEncoderTicks(35f));
        ConcurrentTaskSet forward1 = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, forward1EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }
        };

        Goal<Integer> turnEncoderGoal = new Goal<> (robot.degreesToEncoderTicks(90));
        ConcurrentTaskSet turnBlue = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.TURN_RIGHT);

            }
        };

        ConcurrentTaskSet turnRed = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, turnEncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.TURN_LEFT);

            }
        };

        Goal<Integer> forward2EncoderGoal = new Goal<> (robot.inchesToEncoderTicks(35f));
        ConcurrentTaskSet forward2 = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, forward2EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, forward2EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, forward2EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, forward2EncoderGoal, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }
        }; // another turn

        // Diagonal to the beacon wall
        Goal<Integer> encoderGoal3 = new Goal<> (robot.inchesStraifingToEncoderTicks(50f));
        ConcurrentTaskSet crabToBeaconWallBlue = new ConcurrentTaskSet(
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

                robot.setDirection(CompbotHardware.MovementDirection.NORTH_EAST);

            }
        };

        // Diagonal to the beacon wall
        ConcurrentTaskSet crabToBeaconWallRed = new ConcurrentTaskSet(
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

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }
        };

        // Find the white line with the front cs
        Goal<Integer> encoderGoal4 = new Goal<> (robot.inchesToEncoderTicks(100f));
        ConcurrentTaskSet searchForLineWithFrontCs = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal4, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return robot.sensingWhite(robot.bottomFrontColorSensor);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH_EAST);

            }

        };

        // Find the white line with the middle color sensors
        Goal<Integer> encoderGoal5 = new Goal<> (robot.inchesToEncoderTicks(100f));
        ConcurrentTaskSet searchForLineWithMiddleCs = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal5, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return (robot.sensingWhite(robot.bottomRightColorSensor)) || robot.sensingWhite(robot.bottomLeftColorSensor);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH_EAST);

            }

        };

        // Make the first approach to the beacon
        Goal<Integer> encoderGoal6 = new Goal<> (robot.inchesToEncoderTicks(10f));
        ConcurrentTaskSet beaconFirstApproachBlue = new ConcurrentTaskSet(
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

        // Turns used to align with the line
        ConcurrentTaskSet backUpAndCheckIfCorrectColor = new ConcurrentTaskSet(
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

                robot.setDirection(CompbotHardware.MovementDirection.SOUTH);

            }

            @Override
            public void onCompleted(){

                numTimesHitBeacon++;

                if(numTimesHitBeacon <= 3){

                    happyTrail.addTask(new WaitTask(3000));

                    // Check if the color on the beacon is what it should be
                    if(isBlueTeam && !(robot.buttonPresserColorSensor.blue() > 3)){

                        encoderGoal7 = new Goal<>(robot.inchesToEncoderTicks(4f));
                        happyTrail.addTask(beaconHitBlue);
                        happyTrail.addTask(this);

                    }

                    else if(!isBlueTeam && !(robot.buttonPresserColorSensor.red() > 3)){

                        encoderGoal7 = new Goal<>(robot.inchesToEncoderTicks(4f));
                        happyTrail.addTask(beaconHitRed);
                        happyTrail.addTask(this);

                    }

                }

            }

        };

        final Goal<Integer> encoderGoal8 = new Goal<>(robot.inchesToEncoderTicks(4f));
        final ConcurrentTaskSet moveToSecondBeacon = new ConcurrentTaskSet(
                new MotorTask(robot.frontLeft, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.frontRight, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backRight, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f),
                new MotorTask(robot.backLeft, encoderGoal8, maxMotorSpeed, 0.5f, 0.7f, null, 0.1f)
        ) {
            @Override
            public boolean onExecuted() {
                return isTaskCompleted (0) || isTaskCompleted(1);
            }

            @Override
            public void onReached() {
                super.onReached();

                robot.setDirection(CompbotHardware.MovementDirection.NORTH);

            }

        };

        happyTrail.addTask(shoot);
        happyTrail.addTask(isBlueTeam ? crabToBeaconWallBlue : crabToBeaconWallRed);
        //happyTrail.addTask(forward1);
        //happyTrail.addTask(isBlueTeam ? turnBlue : turnRed);
        //happyTrail.addTask(forward2);
        //another turn
        happyTrail.addTask(searchForLineWithFrontCs);
        happyTrail.addTask(searchForLineWithMiddleCs);
        happyTrail.addTask(isBlueTeam ? beaconFirstApproachBlue : beaconFirstApproachRed);
        happyTrail.addTask(backUpAndCheckIfCorrectColor);
        happyTrail.addTask(moveToSecondBeacon);
        encoderGoal7 = new Goal<>(robot.inchesToEncoderTicks(4f));
        happyTrail.addTask(isBlueTeam ? beaconHitBlue : beaconHitRed);
        numTimesHitBeacon = 1;
        happyTrail.addTask(backUpAndCheckIfCorrectColor);

        addRoute(happyTrail);

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