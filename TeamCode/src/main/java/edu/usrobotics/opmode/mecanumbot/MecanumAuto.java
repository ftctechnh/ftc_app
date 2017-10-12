package edu.usrobotics.opmode.mecanumbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
public abstract class MecanumAuto extends LinearOpMode {

    public enum State {

        BRING_DOWN_KNOCKER,
        READ_COLOR,
        KNOCK_FORWARD,
        KNOCK_BACKWARD;

    }

    private MecanumBotHardware robot = new MecanumBotHardware();
    private State state = State.BRING_DOWN_KNOCKER;

    private boolean isBlue;

    public MecanumAuto(boolean color){

        color = isBlue;

    }

    @Override
    public void runOpMode(){

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // Wait for the game to start
        waitForStart();

        robot.bringDownBallKnocker();

        sleep(500);

        float motorPower = 0f;

        telemetry.addData("Red", robot.colorSensor.red());
        telemetry.addData("Blue", robot.colorSensor.blue());
        telemetry.update();

        if(isBlue){

            if(robot.readingBlue()){

                motorPower = -0.5f;

            }

            else{

                motorPower = 0.5f;

            }

        }

        else{

            if(robot.readingBlue()){

                motorPower = 0.5f;

            }

            else{

                motorPower = -0.5f;

            }

        }

        robot.setPower(motorPower);

        sleep(200);

        robot.setPower(0f);

    }

}