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
import org.firstinspires.ftc.teamcode.SubAssembly.Leds.LedControl;

@Autonomous(name = "Auto4Variations", group = "Auto")
public class Auto4Variations extends LinearOpMode {

    /* Sub assemblies */
    UserControl User = new UserControl();
    VucamControl Vucam = new VucamControl();
    DriveControl Drive = new DriveControl();
    ClaimerControl Claimer = new ClaimerControl();
    LiftControl Lift = new LiftControl();
    LedControl Led = new LedControl();

    //time based variables
    private ElapsedTime runtime = new ElapsedTime();
    private double lastReset = 0;
    private double now = 0;

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
        Initial,
        Deploy,
        Lander_to_Depot,
        Sample_Crater,
        Sample_to_Depot,
        Claim,
        Double_Sample,
        Depot_to_Crater,
        Stop
    }

    //D = Double / R = Right / L = Left / C = Center /
    //Start position variable options
    private enum Start {
        Crater,
        Depot
    }

    //Enum variables (creates variables of the enum variable types previously created)
    private State mCurrentState = State.Initial;
    private Start orientation;
    private boolean doubleSample;
    private boolean allianceCrater;
    private int timeDelay = 0;



    public void getUserInput() {

        /* Get user information */

        /* Automatically switch to teleop? */
        if (User.getYesNo("Transition to TeleOp?"))
            AutoTransitioner.transitionOnStop(this, "teleop");

        if (User.getYesNo("Facing the crater?")) {
            orientation = Start.Crater;
            telemetry.addLine("Facing crater");
        } else {
            orientation = Start.Depot;
            telemetry.addLine("Facing depot");
        }
        telemetry.update();

        if (User.getYesNo("Double Sample?")) {
            doubleSample = true;
            telemetry.addLine("Double sample");
        } else {
            doubleSample = false;
            telemetry.addLine("Single sample");
        }
        telemetry.update();

        if (User.getLeftRight("Left or Right Crater?")){
            allianceCrater = true;
            telemetry.addLine("Alliance Crater");
        } else {
            allianceCrater = false;
            telemetry.addLine("Opponent Crater");
        }

        timeDelay = User.getInt("Time Delay");
        if (timeDelay < 1) {
            telemetry.addLine("No time delay");
        } else {
            telemetry.addData("Time Delay set to ", timeDelay);
        }



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
        Led.init(this);

        getUserInput();

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        telemetry.update();
        telemetry.setAutoClear(false);

        Vucam.setSamplePos();
        if (Vucam.sample == VucamControl.Sample.LEFT) {
            telemetry.addLine("Left");
        } else if (Vucam.sample == VucamControl.Sample.RIGHT) {
            telemetry.addLine("Right");
        } else if (Vucam.sample == VucamControl.Sample.CENTER) {
            telemetry.addLine("Center");
        } else {
            telemetry.addLine("Unknown");
        }
        telemetry.update();

        newState(State.Initial);
        Drive.imu.setStartAngle();

        while (opModeIsActive() && mCurrentState != State.Stop) {

            now = runtime.seconds() - lastReset;

            Drive.imu.update();

            //if (mCurrentState != State.Deploy)

//            telemetry.addData("startAngle", Drive.imu.startAngle);
//            telemetry.addData("currentAngle", Drive.imu.currentAngle);
//            telemetry.addData("trueAngle", Drive.imu.trueAngle);
//            telemetry.update();

            //state switch
            //needs rearranged so that initial is starting state, not land
            switch (mCurrentState) {
                case Initial:
                    Led.white();
                    telemetry.addLine("Initial");
                    telemetry.update();
                    if (now > timeDelay)
                        newState(State.Deploy);
                    break;

                case Deploy:
                    Led.red();
                    telemetry.addLine("Land");
                    telemetry.update();
                    while (!Lift.LifterButtonT.isPressed()) {
                        Lift.Extend();
                    }
                    Lift.Stop();
                    Drive.moveForward(0.5, 0.2);
                    sleep(1000);
                    if (orientation == Start.Crater) {
                        newState(State.Sample_Crater);
                    } else if (orientation == Start.Depot) {
                        newState(State.Lander_to_Depot);
                    }
                    break;

                case Lander_to_Depot:
                    Led.yellow();
                    telemetry.addLine("Move to depot");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turn2Angle(TURN_SPEED, -40);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 1.0);
                        Drive.TimeDelay(0.15);
                        Drive.turn2Angle(TURN_SPEED, 45);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 0.9);
                        newState(State.Claim);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.moveForward(0.5, 2.2);
                        Drive.TimeDelay(0.15);
                        Drive.moveBackward(0.5, 0.05);
                        newState(State.Claim);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, 40);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 1.0);
                        Drive.TimeDelay(0.15);
                        Drive.turn2Angle(TURN_SPEED, -45);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 0.9);
                        newState(State.Claim);
                        //Turn back toward depot
                    }
                    break;

                case Sample_Crater:
                    Led.yellow();
                    telemetry.addLine("Move to crater");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turn2Angle(TURN_SPEED, -35);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 0.9);
                        Drive.TimeDelay(0.15);
                        Drive.moveBackward(0.5, 0.9);
                        newState(State.Stop);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.moveForward(0.5, 0.6);
                        newState(State.Stop);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, 35);
                        Drive.TimeDelay(0.15);
                        Drive.moveForward(0.5, 0.9);
                        Drive.TimeDelay(0.15);
                        Drive.moveBackward(0.5, 0.9);
                        newState(State.Stop);
                    }
                    break;

                case Sample_to_Depot:

                    break;

                case Claim:
                    Led.lawnGreen();
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
                    if (doubleSample == true) {
                        newState(State.Double_Sample);
                    } else {
                        newState(State.Depot_to_Crater);
                    }
                    break;

                case Double_Sample:
                    telemetry.addLine("Double sample");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 90);
                    Drive.turnAngle(TURN_SPEED, 100);
                    Drive.moveForward(0.5, 2.0);
                    newState(State.Stop);
                    break;

                case Depot_to_Crater:
                    Led.darkBlue();
                    telemetry.addLine("Depot to crater");
                    telemetry.update();
                    if (Vucam.sample != Vucam.sample.LEFT) {
                        Drive.moveBackward(0.5, 3.2);
                    } else {
                        Drive.moveBackward(0.5, 1.7);
                        Drive.TimeDelay(0.15);
                        Drive.turn2Angle(TURN_SPEED, -20);
                        Drive.TimeDelay(0.15);
                        Drive.moveBackward(0.4, 0.6);
                        Drive.TimeDelay(0.15);
                        Drive.turn2Angle(TURN_SPEED, -50);
                        Drive.TimeDelay(0.15);
                        Drive.moveBackward(0.5, 0.9);
                    }
                    newState(State.Stop);
                    break;

                case Stop:
                    telemetry.addLine("Stop");
                    telemetry.update();
                    break;

                /*case STATE_ADJUST:
                    Led.orange();
                    telemetry.addLine("Adjust");
                    telemetry.update();
                    Drive.turn2Angle(TURN_SPEED, 0);
                    if (orientation == Start.CRATER) {
                        newState(State.Sample_to_Depot);
                    } else if (orientation == Start.DEPOT) {
                        newState(State.Lander_to_Depot);
                    }
                    break;*/

                /*case STATE_DR:
                    telemetry.addLine("Double Right");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 45);
                    Drive.moveForward(0.5, 2.0);
                    Drive.turnAngle(TURN_SPEED, -90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.Stop);
                    break;

                case STATE_DC:
                    telemetry.addLine("Double Center");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 45);
                    Drive.moveForward(0.5, 1.5);
                    Drive.turnAngle(TURN_SPEED, -90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.Stop);
                    break;

                case STATE_DL:
                    telemetry.addLine("Double Left");
                    telemetry.update();
                    Drive.turnAngle(TURN_SPEED, 45);
                    Drive.moveForward(0.5, 0.2);
                    Drive.turnAngle(TURN_SPEED, -90);
                    Drive.moveForward(0.8, 0.5);
                    newState(State.Stop);
                    break;
                    */

                default:
                    break;
            }
            sleep(40);
        }

        telemetry.setAutoClear(true);
    }
}