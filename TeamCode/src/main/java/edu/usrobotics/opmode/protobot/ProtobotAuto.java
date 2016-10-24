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
 * Created by dsiegler19 on 10/13/16.
 */
@Autonomous(name="Protobot Auto", group="Protobot")
public class ProtobotAuto extends RobotOp {

    ProtobotHardware robot = new ProtobotHardware();
    AutoState state = AutoState.INIT;
    ElapsedTime elapsedTime = new ElapsedTime();

    @Override
    public void init () {

        super.init();

        robot.init(hardwareMap);

        state = AutoState.FORWARD1;

        robot.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void start () {

        super.start();

    }

    @Override
    public void loop(){

        int forward1Time = 5000;
        int turn1Time = 2000;

        double time = elapsedTime.milliseconds();

        if(state == AutoState.FORWARD1){

            robot.frontRight.setPower(1);
            robot.frontLeft.setPower(1);
            robot.backRight.setPower(1);
            robot.backLeft.setPower(1);

            if(time >= forward1Time){

                this.state = AutoState.TURN1;

            }

        }

        else if(state == AutoState.TURN1){

            robot.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);

            robot.frontRight.setPower(1);
            robot.frontLeft.setPower(1);
            robot.backRight.setPower(1);
            robot.backLeft.setPower(1);

            if(time >= forward1Time + turn1Time){

                this.state = AutoState.DONE;

            }

        }

        if(state == AutoState.DONE){

            robot.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
            robot.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);

            robot.frontRight.setPower(0);
            robot.frontLeft.setPower(0);
            robot.backRight.setPower(0);
            robot.backLeft.setPower(0);

            addTracker(new VuforiaTracker());

        }

        telemetry.addData("Elapsed time: ", time);
        telemetry.addData("State: ", state);

    }

}

enum AutoState{

    INIT,
    FORWARD1,
    TURN1,
    DONE

}
