package org.firstinspires.ftc.teamcode.SubAssembly;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.SubAssembly.DriveTrain.DriveControl;
import org.firstinspires.ftc.teamcode.SubAssembly.Lift.LiftControl;
import org.firstinspires.ftc.teamcode.Utilities.AutoTransitioner;

@Autonomous(name = "Auto")
public class Auto extends LinearOpMode {

    //methods go here

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

    //variables go here
    String orientation;

    //stuff for state selection

    private enum State {
        STATE_INITIAL,
        STATE_MOVE_TO_DEPOT,
        STATE_MOVE_TO_CRATER,
        STATE_STOP,
    }



    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("Autonomous");

        startPosition();

        AutoTransitioner.transitionOnStop(this, "TeleOp");

        waitForStart();

        while (opModeIsActive()) {

            //sets up subassemblies inside runOpMode
            DriveControl Drive = new DriveControl(this);
            LiftControl Lift = new LiftControl(this);

            mCurrentState = State.STATE_INITIAL;

            //state switch
            switch (mCurrentState) {
                case STATE_INITIAL:
                    if (orientation.equals("crater")) {
                        newState(State.STATE_MOVE_TO_CRATER);
                        telemetry.addLine("Moving to crater");
                    } else if (orientation.equals("depot")) {
                        newState(State.STATE_MOVE_TO_DEPOT);
                        telemetry.addLine("Moving to depot");
                    }
                    break;
                case STATE_MOVE_TO_CRATER:
                    break;
                case STATE_MOVE_TO_DEPOT:
                    break;
                case STATE_STOP:
                    break;
                default:
                    break;
            }
        }
        sleep (40);
    }
}