package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;

//import static org.firstinspires.ftc.teamcode.auto.State.STATE_STOP;

@Autonomous(name = "Auto1", group = "Drive")
public class auto extends LinearOpMode {

    /* Methods */
    DriveControl Drive = new DriveControl();
    //LiftControl Lift = new LiftControl(this);
    private ElapsedTime runtime = new ElapsedTime();

    /* Arrays */

    //State changing array
    private void newState(State newState) {
        mCurrentState = newState;
        Drive.stop();
        resetClock();
    }

    //Start array
    public void startPosition() {
        telemetry.addLine("Is the starting position facing the crater?");
        telemetry.update();
        while (!gamepad1.a && !gamepad1.b){
        }

        if (gamepad1.a) {
            telemetry.addLine("Crater");
            orientation = Start.Crater;
        }
        else{
            telemetry.addLine("Depot");
            orientation = Start.Depot;
        }
    }

    public void resetClock() {
        //resets the clock
        lastReset = runtime.seconds();
    }

    //Makes the enum stuff work
    private State mCurrentState;
    private Start orientation;
    private Sample sample;

    /* Variables go here */

    //State selection term variables
    private enum State {
        STATE_INITIAL,
        STATE_MOVE_TO_DEPOT,
        STATE_MOVE_TO_CRATER,
        STATE_STOP
    }

    //Start position term variables
    private enum Start {
        Crater,
        Depot
    }

    private enum Sample {
        Left,
        Right,
        Center
    }

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        Drive.init(hardwareMap);

        telemetry.addLine("Autonomous");

        startPosition();

        AutoTransitioner.transitionOnStop(this, "TeleOp");

        telemetry.update();
        waitForStart();

        newState(State.STATE_INITIAL);
        sample = Sample.Center;

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            now = runtime.seconds() - lastReset;

            //state switch
            switch (mCurrentState) {
                case STATE_INITIAL:
                    if (orientation == Start.Crater) {
                        telemetry.addLine("Moving to crater");
                        telemetry.update();
                        newState(State.STATE_MOVE_TO_CRATER);
                    } else if (orientation == Start.Depot) {
                        telemetry.addLine("Moving to depot");
                        telemetry.update();
                        newState(State.STATE_MOVE_TO_DEPOT);
                    }
                    break;

                case STATE_MOVE_TO_CRATER:
                    if (sample == Sample.Left){
                       if (now < 0.15){
                            Drive.turnLeft(0.5);
                       }
                       else if (now < 1.35) {
                           Drive.moveForward(0.75);
                       }
                       else {
                           newState(State.STATE_STOP);
                       }
                    }
                    else if (sample == Sample.Right){
                        if (now < 0.15){
                            Drive.turnRight(0.5);
                        }
                        else if (now < 1.35) {
                            Drive.moveForward(0.75);
                        }
                        else {
                            newState(State.STATE_STOP);
                        }
                    }
                    else {
                        if (now < 1){
                            Drive.moveForward(0.75);
                        }
                        else {
                            newState(State.STATE_STOP);
                        }
                    }
                    /*Drive.moveForward(0.75);
                    if (now > 1) {
                        newState(State.STATE_STOP);
                    }*/
                    break;

                case STATE_MOVE_TO_DEPOT:
                    if (sample == Sample.Left){
                        if (now < 0.15){
                            Drive.turnLeft(0.5);
                        }
                    }
                    else if (sample == Sample.Right) {
                        if (now < 0.15) {
                            Drive.turnRight(0.5);
                        } else {
                            Drive.moveForward(1.25);
                        }
                        newState(State.STATE_STOP);
                    }
                    /*Drive.moveForward(0.75);
                    if (now > 1.25) {
                        newState(State.STATE_STOP);
                    }*/
                    break;

                case STATE_STOP:
                    break;

                default:
                    break;
            }
            sleep(40);
        }
    }
}