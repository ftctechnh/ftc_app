package org.firstinspires.ftc.teamcode.SubAssembly;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;

@Autonomous(name = "Auto", group = "Drive")
public class auto extends LinearOpMode {

    //methods go here

    private ElapsedTime runtime = new ElapsedTime();

    private State mCurrentState;

    private void newState(State newState) {
        mCurrentState = newState;
    }

    public String startPosition() {
        telemetry.addLine("Is the starting position facing the crater?");
        do {
            if (gamepad1.a) {
                orientation = "crater";
            } else if (gamepad1.b) {
                orientation = "depot";
            }
        } while (!gamepad1.a && !gamepad1.b);
        return orientation;
    }

    public void resetClock() {
        //resets the clock
        lastReset = runtime.seconds();
    }

    //variables go here
    String orientation;

    //stuff for state selection

    private enum State {
        STATE_INITIAL,
        STATE_MOVE_TO_DEPOT,
        STATE_MOVE_TO_CRATER,
        STATE_STOP,
    }

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("starting runOpMode in auto.java");
        telemetry.update();

        //sets up subassemblies inside runOpMode
        DriveControl Drive = new DriveControl(this);
        LiftControl Lift = new LiftControl(this);

        mCurrentState = State.STATE_INITIAL;

        telemetry.addLine("Autonomous");
        telemetry.update();

        startPosition();

        //no u
        //AutoTransitioner.transitionOnStop(this, "TeleOp");

        waitForStart();

        resetClock();

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            //state switch
            switch (mCurrentState) {
                case STATE_INITIAL:
                    if (orientation.equals("crater")) {
                        telemetry.addLine("Moving to crater");
                        telemetry.update();
                        resetClock();
                        newState(State.STATE_MOVE_TO_CRATER);
                    } else if (orientation.equals("depot")) {
                        newState(State.STATE_MOVE_TO_DEPOT);
                        telemetry.addLine("Moving to depot");
                        telemetry.update();
                    }
                    break;
                case STATE_MOVE_TO_CRATER:
                    if (now < 3) {
                        Drive.moveForward(0.75);
                    } else {
                        Drive.stop();
                        newState(State.STATE_STOP);
                        resetClock();
                    }
                    break;
                case STATE_MOVE_TO_DEPOT:
                    if (now < 5) {
                        Drive.moveForward(0.75);
                    } else {
                        Drive.stop();
                        newState(State.STATE_STOP);
                        resetClock();
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