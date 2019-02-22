package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.SubAssembly.Miner.MinerControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Vucam.VucamControl;
import org.firstinspires.ftc.teamcode.Utilities.UserControl;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Claimer.ClaimerControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;
import org.firstinspires.ftc.teamcode.SubAssembly.Leds.LedControl;

@Autonomous(name = "Auto5Tof", group = "Auto")
public class Auto5Tof extends LinearOpMode {

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
        Crater_to_Depot,
        Double_Sample,
        Claim,
        Depot_to_Crater,
        Start_Depot_to_Crater,
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
    private boolean noLift;
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

        if (User.getLeftRight("Left or Right Crater?")) {
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
        /*if (User.getYesNo("Remove Lift?")) {
            telemetry.addLine("No Lift");
            noLift = true;
        }else {
            telemetry.addLine("Yes Lift");
            noLift=false;
        }*/
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
        Led.init(this);

        getUserInput();

        //waits for that giant PLAY button to be pressed on RC
        telemetry.addLine(">> Press PLAY to start");
        telemetry.update();
        telemetry.setAutoClear(true);
        waitForStart();

        telemetry.update();
        telemetry.setAutoClear(false);

        newState(State.Initial);
        Drive.imu.setStartAngle();

        while (opModeIsActive() && mCurrentState != State.Stop) {

            now = runtime.seconds() - lastReset;

            Drive.imu.update();

            Drive.Tof.Telemetry();

            /*if (mCurrentState != State.Deploy && Lift.LifterButtonB.isPressed()) {
                Lift.Stop();
            }*/

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

                    //if (noLift == false){
                        telemetry.addLine("Land");
                        telemetry.update();
                        Lift.Extend();
                        Drive.TimeDelay(0.25);
                        while (!Lift.LifterButtonT.isPressed()) {
                            Lift.Extend();
                        }
                        Lift.Stop();
                    //}else {
                        telemetry.addLine("Lift Removed");
                    //}

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

                    Drive.moveForward(0.5, 0.14);
                    sleep(100);
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
                        Drive.turn2Angle(TURN_SPEED, -45);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance(0.3, 9);
                        Drive.TimeDelay(0.1);
                        Drive.turn2Angle(TURN_SPEED, 45);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance(0.3, 9);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.forwardUntilDistance4Time(0.3, 17, 6);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, 45);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance(0.3, 9);
                        Drive.TimeDelay(0.1);
                        Drive.turn2Angle(TURN_SPEED, -48);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance(0.3, 9);
                        //Turn back toward depot
                    }
                    newState(State.Claim);
                    break;

                case Sample_Crater:
                    Led.yellow();
                    telemetry.addLine("Move to crater");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turn2Angle(TURN_SPEED, -45);
                        Drive.TimeDelay(0.1);
                        Drive.moveForward(0.5, 0.7);
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.5, 0.5);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.moveForward(0.5, 0.4);
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.5, 0.3);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, 45);
                        Drive.TimeDelay(0.1);
                        Drive.moveForward(0.5, 0.7);
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.5, 0.5);
                    }
                    newState(State.Crater_to_Depot);
                    break;

                case Crater_to_Depot:
                    telemetry.addLine("Move to depot");
                    telemetry.update();
                    Drive.turn2Angle(TURN_SPEED, -80);
                    Drive.TimeDelay(0.1);
                    if(Vucam.sample == Vucam.sample.RIGHT){
                        Drive.moveForward(0.3, 3.2);
                    }
                    else {
                        Drive.moveForward(0.3, 2.8);
                    }
                    //Drive.forwardUntilDistance4Time(0.3, 30.5, 5);
                    Drive.TimeDelay(0.1);
                    Drive.turn2Angle(TURN_SPEED, -135);
                    Drive.TimeDelay(0.1);
                    Drive.forwardUntilDistance(0.3, 17); // changed for now
                    if (doubleSample) {
                        Claimer.drop();
                        newState(State.Double_Sample);
                    } else {
                        newState(State.Claim);
                    }
                    break;

                case Double_Sample:
                    telemetry.addLine("Double sample");
                    telemetry.update();
                    if (Vucam.sample == Vucam.sample.LEFT) {
                        Drive.turn2Angle(TURN_SPEED, -60);
                        Claimer.reset();
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.8, 0.45);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance4Time(0.3, 13.5, 5);
                    } else if (Vucam.sample == Vucam.sample.CENTER) {
                        Drive.turn2Angle(TURN_SPEED, -90);
                        Claimer.reset();
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.8, 0.4);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance4Time(0.3, 21,6);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, -120);
                        Claimer.reset();
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.8, 0.45);
                        Drive.TimeDelay(0.1);
                        Drive.forwardUntilDistance4Time(0.3, 13.5, 5);
                    }
                    newState(State.Claim);
                    break;

                case Claim:
                    Led.lawnGreen();
                    telemetry.addLine("Claim");
                    telemetry.update();
                    if (orientation == Start.Depot) {
                        Drive.turn2Angle(TURN_SPEED, -45.0);
                        Claimer.drop();
                        Drive.TimeDelay(0.5);
                        Claimer.reset();
                        newState(State.Start_Depot_to_Crater);
                    } else {
                        Drive.turn2Angle(TURN_SPEED, -135.0);
                        Claimer.drop();
                        Drive.TimeDelay(0.5);
                        Claimer.reset();
                        newState(State.Depot_to_Crater);
                    }
                    break;

                case Depot_to_Crater:
                    Led.darkBlue();
                    telemetry.addLine("Depot to crater");
                    telemetry.update();
                    if (!allianceCrater) {
                        Drive.turn2Angle(TURN_SPEED, -45);
                    }
                    Drive.moveBackward(0.65, 2.3); //dropped for w/ 11454
                    newState(State.Stop);
                    break;

                case Start_Depot_to_Crater:
                    Led.lawnGreen();
                    telemetry.addLine("Start at Depot, to crater");
                    telemetry.update();
                    if (!allianceCrater) {
                        Drive.turn2Angle(TURN_SPEED, 45);
                    }

                    if (Vucam.sample == Vucam.sample.LEFT && orientation == Start.Depot && doubleSample && allianceCrater) {
                        Drive.moveBackward(0.5, 1.7);
                        Drive.TimeDelay(0.1);
                        Drive.turn2Angle(TURN_SPEED, -20);
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.4, 0.6);
                        Drive.TimeDelay(0.1);
                        Drive.turn2Angle(TURN_SPEED, -50);
                        Drive.TimeDelay(0.1);
                        Drive.moveBackward(0.5, 0.9);
                    } else {
                        Drive.moveBackward(0.5, 3.2);
                    }
                    newState(State.Stop);
                    break;

                case Stop:
                    telemetry.addLine("Stop");
                    telemetry.update();
                    break;

                default:
                    break;
            }
            sleep(40);
        }

        Drive.stop();

        telemetry.setAutoClear(true);
    }
}
