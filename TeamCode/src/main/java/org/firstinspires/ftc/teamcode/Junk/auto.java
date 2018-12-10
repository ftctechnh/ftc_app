package org.firstinspires.ftc.teamcode.Junk;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;

import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Claimer.ClaimerControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Vucam.VucamControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;

@Autonomous(name = "Auto1", group = "Drive")
@Disabled
public class auto extends LinearOpMode {

    /* Methods */
    DriveControl Drive = new DriveControl();
    ClaimerControl Claimer = new ClaimerControl();
    //LiftControl Lift = new LiftControl(this);
    VucamControl Vucam = new VucamControl();
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

    //Start array
    /*public void samplePosition() {
        telemetry.addLine("Sample position?");
        telemetry.update();
        while (!gamepad1.dpad_down && !gamepad1.dpad_right && !gamepad1.dpad_left){
        }

        if (gamepad1.dpad_down) {
            telemetry.addLine("Center");
            sample = PracticeSample.Center;
        }
        else if (gamepad1.dpad_right){
            telemetry.addLine("Right");
            sample = PracticeSample.Right;
        }
        else {
            telemetry.addLine("Left");
            sample = PracticeSample.Left;
        }
    }*/

    public void resetClock() {
        //resets the clock
        lastReset = runtime.seconds();
    }

    //Makes the enum stuff work
    private State mCurrentState;
    private Start orientation;
    private SamplingOrderDetector.GoldLocation Sample;

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

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        Drive.init(this);
        Claimer.init(this);
        Vucam.init(this);

        telemetry.addLine("Autonomous");

        startPosition();

        AutoTransitioner.transitionOnStop(this, "TeleOp");

        telemetry.update();
        waitForStart();
//put sample position code here
        newState(State.STATE_INITIAL);

       // Vucam.Scan();
        //Sample = Vucam.detector.getCurrentOrder();

        while (opModeIsActive() && mCurrentState != State.STATE_STOP) {

            now = runtime.seconds() - lastReset;
            telemetry.addData("Sample: ", Sample);
            telemetry.update();

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
                    if (Sample == SamplingOrderDetector.GoldLocation.LEFT) {
                        if (now < 0.15) {
                            Drive.turnLeft(0.4);
                        } else if (now < 0.3) {
                            Drive.stop();
                        }
                        //Sample left to move forward
                        else if (now < 1.45) {
                            Drive.moveForward(0.55);
                        } else {
                            newState(State.STATE_STOP);
                        }
                    } else if (Sample == SamplingOrderDetector.GoldLocation.RIGHT) {
                        if (now < 0.15) {
                            Drive.turnRight(0.4);
                        } else if (now < 0.25) {
                            Drive.stop();
                        }
                        //Sample right to move forward
                        else if (now < 1.45) {
                            Drive.moveForward(0.5);
                        } else {
                            newState(State.STATE_STOP);
                        }
                    } else {
                        if (now < 1.25) {
                            Drive.moveForward(0.5);
                        } else {
                            newState(State.STATE_STOP);
                        }
                    }
                    /*Drive.moveForward(0.75);
                    if (now > 1) {
                        newState(State.STATE_STOP);
                    }*/
                    break;

                case STATE_MOVE_TO_DEPOT:
                    if (Sample == SamplingOrderDetector.GoldLocation.LEFT) {
                        if (now < 0.15) {
                            Drive.turnLeft(0.4);
                        } else if (now < 0.3) {
                            Drive.stop();
                        }
                        //Sample left to move forward (test before adjusting)
                        else if (now < 1.5) {

                            Drive.moveForward(0.55);
                        } else if (now < 1.65) {
                            Drive.stop();
                        } else if (now < 2.1) {
                            Drive.turnRight(0.4);
                        } else {
                            newState(State.STATE_STOP);
                        }
                    } else if (Sample == SamplingOrderDetector.GoldLocation.RIGHT) {
                        if (now < 0.15) {
                            Drive.turnRight(0.4);
                        } else if (now < 0.25) {
                            Drive.stop();
                        }
                        //Sample right to move forward
                        else if (now < 1.45) {
                            Drive.moveForward(0.5);
                        } else {
                            newState(State.STATE_STOP);
                        }
                    }
                    //Center
                    else {
                        if (now < 2.3) {
                            Drive.moveForward(0.5);
                        } else if (now < 2.4) {
                            Drive.stop();
                        } else if (now < 2.8) {
                            Drive.turnLeft(0.4);
                        } else if (now < 3.8) {
                            Drive.stop();
                            Claimer.drop();
                        } else if (now < 7.5) {
                            Claimer.reset();
                            Drive.moveBackward(0.5);
                        } else {
                            newState(State.STATE_STOP);
                        }
                    }
                    /*if (sample == Sample.Left){
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
        //Sample.stop();
    }
}