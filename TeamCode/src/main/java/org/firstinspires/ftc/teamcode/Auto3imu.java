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
import org.firstinspires.ftc.teamcode.SamplingOrderExample;
import org.firstinspires.ftc.teamcode.Sensors.IMUcontrol;
//import static org.firstinspires.ftc.teamcode.auto2.State.STATE_STOP;

@Autonomous(name = "Auto3imu", group = "Drive")
public class Auto3imu extends LinearOpMode {

    /* Methods */
    UserControl User = new UserControl();
    VucamControl Vucam = new VucamControl();
    DriveControl Drive = new DriveControl();
    //SamplingOrderExample Sample = new SamplingOrderExample();
    ClaimerControl Claimer = new ClaimerControl();
    LiftControl Lift = new LiftControl();
    private ElapsedTime runtime = new ElapsedTime();
    private double HomeAngle;
    boolean twoSample = false;
    /* Arrays */

    //State changing array
    private void newState(State newState) {
        mCurrentState = newState;
        Drive.stop();
        Drive.TimeDelay(0.1);
        resetClock();
    }

    //Start array
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
        telemetry.update();
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
        Crater,
        Depot
    }

    /* private enum doubleSample{
         //SS Figure this out

     }*/
    //Enum variables (creates variables of the enum variable types previously created)
    private State mCurrentState;
    private Start orientation;

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        User.init(this);
        Vucam.init(hardwareMap);
        Drive.init(hardwareMap);
        Claimer.init(hardwareMap);
        Lift.init(hardwareMap);
        //Sample.init();

        telemetry.addLine("Autonomous");

        startPosition();
        twoSample = User.getYesNo("Double sample?");
        //sample = PracticeSample.Center;

        if (orientation == Start.Crater)
            telemetry.addLine("Start crater");
        if (orientation == Start.Depot)
            telemetry.addLine("Start depot");
        if (twoSample)
            telemetry.addLine("Double sample");
        else
            telemetry.addLine("Single sample");
        telemetry.update();

        AutoTransitioner.transitionOnStop(this, "teleOp");

        waitForStart();
        Vucam.setSamplePos();
        newState(State.STATE_INITIAL);

        Drive.imu.IMUinit();

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            now = runtime.seconds() - lastReset;

            Drive.imu.IMUupdate();

            telemetry.addData("startAngle", Drive.imu.startAngle);
            telemetry.addData("currentAngle", Drive.imu.currentAngle);
            telemetry.addData("trueAngle", Drive.imu.trueAngle);
            telemetry.update();

            //state switch
            switch (mCurrentState) {
                case STATE_LAND:
                    telemetry.addLine("Land");
                    telemetry.update();
                    Lift.Unlock();
                   do {
                        Lift.Extend();
                    } while (!Lift.LifterButtonT.isPressed());
                    sleep(1000);
                    newState(State.STATE_ADJUST);
                    break;

                case STATE_ADJUST:
                    Drive.turn2angle(0);
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

                case STATE_INITIAL:
                    telemetry.addLine("Initial");
                    telemetry.update();
                    newState(State.STATE_LAND);
                    /*Sample.init();
                    Sample.start();
                    Sample.loop();*/
                    break;

                case STATE_MOVE_TO_CRATER:
                    telemetry.addLine("Move to crater");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.Left) {
                        Drive.turn2angle(-25);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.55, 1.15);
                        newState(State.STATE_STOP);
                    } else if (Vucam.sample == Vucam.sample.Center) {
                        Drive.moveForward(0.5, 1.25);
                        newState(State.STATE_STOP);
                    } else {
                        Drive.turn2angle(25);
                        Drive.TimeDelay(0.1);
                        Drive.moveForward(0.5, 1.20);
                        newState(State.STATE_STOP);
                    }
                    break;

                case STATE_MOVE_TO_DEPOT:
                    telemetry.addLine("Move to depot");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.Left) {
                        Drive.turn2angle(-35);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 1.0);
                        Drive.turn2angle(38);
                        Drive.moveForward(0.55, 1.0);
                        newState(State.STATE_CLAIM);
                    } else if (Vucam.sample == Vucam.sample.Center) {
                        Drive.moveForward(0.5, 1.8);
                        Drive.moveBackward(0.5, 0.15);
                        newState(State.STATE_CLAIM);
                    }
                    else {
                        Drive.turn2angle(35);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 1.3);
                        Drive.turn2angle(-48);
                        Drive.moveForward(0.55, 1.5);
                        newState(State.STATE_CLAIM);
                        //Turn back toward depot
                    }
                    break;

                case STATE_CLAIM:
                    telemetry.addLine("Claim");
                    telemetry.update();
                    Drive.turn2angle(-45.0);
                    Claimer.drop();
                    while (!Lift.LifterButtonB.isPressed()){
                        Lift.Retract();
                }
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
                    Drive.moveBackward(0.5, 3.2);
                    newState(State.STATE_STOP);
                    break;

                case STATE_DOUBLE_SAMPLE:
                    telemetry.addLine("Double sample");
                    telemetry.update();
                    Drive.turnAngle(90);
                    Drive.turnAngle(100);
                    Drive.moveForward(0.5, 2.0);
                    newState(State.STATE_STOP);
                    break;

                case STATE_DR:
                    telemetry.addLine("Double Right");
                    telemetry.update();
                    Drive.turnAngle(45);
                    Drive.moveForward(0.5, 2.0);
                    Drive.turnAngle(-90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.STATE_STOP);
                    break;
                case STATE_DC:
                    telemetry.addLine("Double Center");
                    telemetry.update();
                    Drive.turnAngle(45);
                    Drive.moveForward(0.5, 1.5);
                    Drive.turnAngle(-90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.STATE_STOP);
                    break;

                case STATE_DL:
                    telemetry.addLine("Double Left");
                    telemetry.update();
                    Drive.turnAngle(45);
                    Drive.moveForward(0.5, 0.2);
                    Drive.turnAngle(-90);
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
