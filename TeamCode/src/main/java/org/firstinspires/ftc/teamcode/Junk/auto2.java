package org.firstinspires.ftc.teamcode.Junk;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Claimer.ClaimerControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Vucam.VucamControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;
import org.firstinspires.ftc.teamcode.SubAssembly.Sensors.IMUcontrol;

@Autonomous(name = "Auto2", group = "Drive")
public class auto2 extends LinearOpMode {

    /* Subassembly Constructors */
    DriveControl Drive = new DriveControl();
    ClaimerControl Claimer = new ClaimerControl();
    //LiftControl Lift = new LiftControl(this);
    VucamControl Vucam = new VucamControl();
    private ElapsedTime runtime = new ElapsedTime();

    /* Methods */

    //State changing method
    private void newState(State newState) {
        mCurrentState = newState;
        Drive.stop();
        Drive.TimeDelay(0.1);
        resetClock();
    }

    //Start method
    public void startPosition() {
        telemetry.addLine("Is the starting position facing the crater?");
        telemetry.update();
        while (!gamepad1.a && !gamepad1.b) {
        }

        if (gamepad1.a) {
            telemetry.addLine("Crater");
            orientation = Start.Crater;
        } else {
            telemetry.addLine("Depot");
            orientation = Start.Depot;
        }
    }

    //Sample position testing method
    /*public void samplePosition() {
        telemetry.addLine("Sample position?");
        telemetry.update();
        while (!gamepad1.dpad_down && !gamepad1.dpad_right && !gamepad1.dpad_left){
        }

        if (gamepad1.dpad_down) {
            telemetry.addLine("Center");
            Vucam.sample = Vucam.sample.Center;
        }
        else if (gamepad1.dpad_right){
            telemetry.addLine("Right");
            Vucam.sample = Vucam.sample.Right;
        }
        else {
            telemetry.addLine("Left");
            Vucam.sample = Vucam.sample.Left;
        }
    }*/

    public void resetClock() {
        //resets the clock
        lastReset = runtime.seconds();
    }

    /* Variables go here */

    //State selection variable options
    private enum State {
        STATE_INITIAL,
        STATE_MOVE_TO_DEPOT,
        STATE_MOVE_TO_CRATER,
        STATE_CLAIM,
        STATE_DEPOT_TO_CRATER,
        STATE_STOP
    }

    //Start position variable options
    private enum Start {
        Crater,
        Depot
    }

    //Enum variables (creates variables of the enum variable types previously created)
    private State mCurrentState;
    private Start orientation;

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        Drive.init(this);
        Claimer.init(this);
        Vucam.init(this);
        //Sample.init();

        telemetry.addLine("Autonomous");

        startPosition();

        AutoTransitioner.transitionOnStop(this, "teleOp");

        telemetry.update();
        waitForStart();
        Vucam.setSamplePos();
        newState(State.STATE_INITIAL);

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            now = runtime.seconds() - lastReset;

            //state switch
            switch (mCurrentState) {
                case STATE_INITIAL:
                    /*Sample.init();
                    Sample.start();
                    Sample.loop();*/

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
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turnLeft(0.4, 0.15);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.55, 1.15);
                        newState(State.STATE_STOP);
                    } else if (Vucam.sample == Vucam.sample.RIGHT) {

                        Drive.turnRight(0.4, 0.15);
                        Drive.TimeDelay(0.1);
                        Drive.moveForward(0.5, 1.20);
                        newState(State.STATE_STOP);

                    } else {
                        Drive.moveForward(0.5, 1.25);
                        newState(State.STATE_STOP);
                    }
                    break;

                case STATE_MOVE_TO_DEPOT:
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turnLeft(0.4, 0.15);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.55, 1.2);
                        Drive.TimeDelay(0.15);
                        Drive.turnRight(0.4, 0.36);
                        newState(State.STATE_STOP);
                    }
                    //Center
                    else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.moveForward(0.5, 2.2);
                        newState(State.STATE_CLAIM);
                    }
                    else {
                    Drive.turnRight(0.4, 0.15);
                    Drive.moveForward(0.5, 1.2);
                    newState(State.STATE_STOP);
                    }
                    break;

                case STATE_CLAIM:
                    Drive.tankDrive(-0.4, 0, 0.48);
                    Claimer.drop();
                    Drive.TimeDelay(2.0);
                    Claimer.reset();
                    newState(State.STATE_DEPOT_TO_CRATER);
                    break;

                case STATE_DEPOT_TO_CRATER:
                    Drive.moveBackward(0.5, 3.7);
                    newState(State.STATE_STOP);
                    break;

                case STATE_STOP:
                    break;

                default:
                    break;
            }
            sleep(40);
        }
        //Sample.stop();
    }
}