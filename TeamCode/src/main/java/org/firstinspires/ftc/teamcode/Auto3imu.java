package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SubAssembly.Vucam.VucamControl;
import org.firstinspires.ftc.teamcode.Utilities.UserControl;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Claimer.ClaimerControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;

@Autonomous(name = "Auto3imu", group = "Auto")
public class Auto3imu extends LinearOpMode {

    /* Sub assemblies */
    UserControl User = new UserControl();
    VucamControl Vucam = new VucamControl();
    DriveControl Drive = new DriveControl();
    ClaimerControl Claimer = new ClaimerControl();
    LiftControl Lift = new LiftControl();
    private ElapsedTime runtime = new ElapsedTime();
    boolean twoSample = false;

    /* Constants */
    final double TURN_SPEED = 0.3;

    //State changing array
    private void newState(State newState) {
        mCurrentState = newState;
        Drive.stop();
        Drive.TimeDelay(0.1);
        resetClock();
    }

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
        STATE_DOUBLE_SAMPLE,
        STATE_STOP,
        STATE_LAND,
        STATE_ADJUST,
        STATE_DL,
        STATE_DR,
        STATE_DC
    }

    //D = Double / R = Right / L = Left / C = Center /
    //Start position variable options
    private enum Start {
        CRATOR,
        DEPOT
    }

    //Enum variables (creates variables of the enum variable types previously created)
    private State mCurrentState;
    private Start orientation;

    //time based variables
    double lastReset = 0;
    double now = 0;

    public void getUserInput() {
        /* Get user information */
        if (User.getYesNo("Facing the crater?")) {
            orientation = Start.CRATOR;
            telemetry.addLine("Facing crater");
        } else {
            orientation = Start.DEPOT;
            telemetry.addLine("Facing depot");
        }
        telemetry.update();

        twoSample = User.getYesNo("Double sample?");
        if (twoSample)
            telemetry.addLine("Double sample");
        else
            telemetry.addLine("Single sample");
        telemetry.update();
    }

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.setAutoClear(false);
        telemetry.addLine("Autonomous");
        telemetry.update();

        /* initialize sub-assemblies
         */
        User.init(this);
        Vucam.init(this);
        Drive.init(this);
        Claimer.init(this);
        Lift.init(this);

        getUserInput();

        AutoTransitioner.transitionOnStop(this, "teleOp");

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        Vucam.setSamplePos();
        newState(State.STATE_LAND);
        Drive.imu.setStartAngle();

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            now = runtime.seconds() - lastReset;

            Drive.imu.update();

            telemetry.addData("startAngle", Drive.imu.startAngle);
            telemetry.addData("currentAngle", Drive.imu.currentAngle);
            telemetry.addData("trueAngle", Drive.imu.trueAngle);
            telemetry.update();

            //state switch
            switch (mCurrentState) {
                case STATE_LAND:
                    telemetry.addLine("Land");
                    telemetry.update();
                  /* while(!Lift.LifterButtonT.isPressed()) {
                        Lift.Extend();
                    }
                    Lift.Stop();*/
                    sleep(1000);
                    newState(State.STATE_ADJUST);
                    break;

                case STATE_ADJUST:
                    Drive.turn2Angle(TURN_SPEED, 0);
                    newState(State.STATE_INITIAL);
                    break;

                case STATE_INITIAL:
                    telemetry.addLine("Initial");
                    telemetry.update();
                    /*Sample.init();
                    Sample.start();
                    Sample.loop();*/

                    if (orientation == Start.CRATOR) {
                        telemetry.addLine("Moving to crater");
                        telemetry.update();
                        newState(State.STATE_MOVE_TO_CRATER);
                    } else if (orientation == Start.DEPOT) {
                        telemetry.addLine("Moving to depot");
                        telemetry.update();
                        newState(State.STATE_MOVE_TO_DEPOT);
                    }
                    break;

                case STATE_MOVE_TO_CRATER:
                    telemetry.addLine("Move to crater");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turn2Angle(TURN_SPEED, -25);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.55, 1.15);
                        newState(State.STATE_STOP);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.moveForward(0.5, 1.25);
                        newState(State.STATE_STOP);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, 25);
                        Drive.TimeDelay(0.1);
                        Drive.moveForward(0.5, 1.20);
                        newState(State.STATE_STOP);
                    }
                    break;

                case STATE_MOVE_TO_DEPOT:
                    telemetry.addLine("Move to depot");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turn2Angle(TURN_SPEED, -35);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 1.0);
                        Drive.turn2Angle(TURN_SPEED, 38);
                        Drive.moveForward(0.55, 1.0);
                        newState(State.STATE_CLAIM);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.moveForward(0.5, 2.2);
                        Drive.moveBackward(0.5, 0.05);
                        newState(State.STATE_CLAIM);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, 35);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 1.3);
                        Drive.turn2Angle(TURN_SPEED, -48);
                        Drive.moveForward(0.5, 1.0);
                        newState(State.STATE_CLAIM);
                        //Turn back toward depot
                    }
                    break;

                case STATE_CLAIM:
                    telemetry.addLine("Claim");
                    telemetry.update();
                    Drive.turn2Angle(TURN_SPEED, -45.0);
                    Claimer.drop();
                  /* while (!Lift.LifterButtonB.isPressed()){
                        Lift.Retract();
                    }
                    Lift.Stop();*/
                    Drive.TimeDelay(2.0);
                    Claimer.reset();
                    if (twoSample == true) {
                        newState(State.STATE_DOUBLE_SAMPLE);
                    } else {
                        newState(State.STATE_DEPOT_TO_CRATER);
                    }
                    break;

                case STATE_DEPOT_TO_CRATER:
                    telemetry.addLine("Depot to crater");
                    telemetry.update();
                    if (Vucam.sample != Vucam.sample.LEFT) {
                        Drive.moveBackward(0.5, 3.2);
                    }
                    else {
                        Drive.moveBackward(0.5, 1.7);
                        Drive.turn2Angle(TURN_SPEED, -20);
                        Drive.moveBackward(0.4, 0.6);
                        Drive.turn2Angle(TURN_SPEED, -50);
                        Drive.moveBackward(0.5, 0.9);
                    }
                    newState(State.STATE_STOP);
                    break;

                case STATE_DOUBLE_SAMPLE:
                    telemetry.addLine("Double sample");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 90);
                    Drive.turnAngle(TURN_SPEED, 100);
                    Drive.moveForward(0.5, 2.0);
                    newState(State.STATE_STOP);
                    break;

                case STATE_DR:
                    telemetry.addLine("Double Right");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 45);
                    Drive.moveForward(0.5, 2.0);
                    Drive.turnAngle(TURN_SPEED, -90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.STATE_STOP);
                    break;

                case STATE_DC:
                    telemetry.addLine("Double Center");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 45);
                    Drive.moveForward(0.5, 1.5);
                    Drive.turnAngle(TURN_SPEED, -90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.STATE_STOP);
                    break;

                case STATE_DL:
                    telemetry.addLine("Double Left");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 45);
                    Drive.moveForward(0.5, 0.2);
                    Drive.turnAngle(TURN_SPEED, -90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.STATE_STOP);
                    break;

                case STATE_STOP:
                    telemetry.addLine("Stop");
                    telemetry.update();
                    break;

                default:
                    break;
            }
            sleep(40);
        }
        //Sample.stop();
    }
}

