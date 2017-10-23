package edu.usrobotics.opmode.compbot.justshoot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
public abstract class CompbotAutoJustShoot extends RobotOp {

    CompbotHardware robot = new CompbotHardware();
    private final boolean isBlueTeam;

    public CompbotAutoJustShoot(boolean isBlueTeam) {
        this.isBlueTeam = isBlueTeam;
    }

    @Override
    public void init () {

        robot.init(hardwareMap);

        final Route happyTrail = new Route();

        robot.setDirection(CompbotHardware.MovementDirection.NORTH);

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

        happyTrail.addTask(shoot);

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
        telemetry.addData("bottomRightColorSensor", robot.bottomRightColorSensor.red() + " " + robot.bottomRightColorSensor.green() + " " + robot.bottomRightColorSensor.blue());
        telemetry.addData("bottomLeftColorSensor", robot.bottomLeftColorSensor.red() + " " + robot.bottomLeftColorSensor.green() + " " + robot.bottomLeftColorSensor.blue());

        super.loop();

    }

}