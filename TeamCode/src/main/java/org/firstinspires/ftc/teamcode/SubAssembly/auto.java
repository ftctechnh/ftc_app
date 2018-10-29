package org.firstinspires.ftc.teamcode.SubAssembly;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;

@Autonomous(name = "Auto", group = "Drive")
public class auto extends LinearOpMode {

    /* Methods */
    DriveControl Drive = new DriveControl(this);
    LiftControl Lift = new LiftControl(this);
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
        while (!gamepad1.a && !gamepad1.b){
        }

        if (gamepad1.a)
            telemetry.addLine("Crater");
            orientation = Start.Crater;

        telemetry.addLine("Depot");
        orientation = Start.Depot;
    }

    public void resetClock() {
        //resets the clock
        lastReset = runtime.seconds();
    }

    //Makes the enum stuff work
    private State mCurrentState;
    private Start orientation;

    /* Variables go here */

    //State selection term variables
    private enum State {
        STATE_INITIAL,
        STATE_MOVE_TO_DEPOT,
        STATE_MOVE_TO_CRATER,
        STATE_STOP,
    }

    //Start position term variables
    private enum Start {
        Crater,
        Depot,
    }

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("starting runOpMode in auto.java");
        telemetry.addLine("Autonomous");

        startPosition();

        //no u
        AutoTransitioner.transitionOnStop(this, "TeleOp");

        waitForStart();

        newState(State.STATE_INITIAL);

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            //state switch
            switch (mCurrentState) {
                case STATE_INITIAL:
                    if (orientation == Start.Crater) {
                        telemetry.addLine("Moving to crater");
                        newState(State.STATE_MOVE_TO_CRATER);
                    }
                    else if (orientation == Start.Crater) {
                        telemetry.addLine("Moving to depot");
                        newState(State.STATE_MOVE_TO_DEPOT);
                    }
                    break;

                case STATE_MOVE_TO_CRATER:
                    Drive.moveForward(0.75);
                    if (now > 3) {
                        newState(State.STATE_STOP);
                    }
                    break;

                case STATE_MOVE_TO_DEPOT:
                    Drive.moveForward(0.75);
                    if (now > 5) {
                        newState(State.STATE_STOP);
                    }
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