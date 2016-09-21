package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by FTCGearedUP on 3/3/2016.
 */
public class BlueStateMachine extends OpMode{
    public enum STATE { INIT(1), DRIVEFORWARDTOWARDSGOAL(2), FACEGOAL(3), LINEUPTOWALL(4),
        ROTATE(5), GOTOWHITELINE(6), DROPCLIMBERS(7), MOVEOUTOFWAY(8) ;
        private int value;
        private STATE(int value) { this.value = value; }
    };
    STATE state;

    @Override
    public void init() {
        state = STATE.INIT;
        initialization();
    }

    @Override
    public void loop() {
        switch (state) {
            case INIT:
                initialization();
                break;
            case DRIVEFORWARDTOWARDSGOAL:
                drivetogoal();
                break;
            case FACEGOAL:
                facegoal();
                break;
            case LINEUPTOWALL:
                lineuptogoal();
                break;
            case ROTATE:
                turnninetydegrees();
                break;
            case GOTOWHITELINE:
                gotowhiteline();
                break;
            case DROPCLIMBERS:
                dropclimbers();
                break;
            case MOVEOUTOFWAY:
                moveoutofway();
                break;
        }
    }

    private void initialization() {
        telemetry.addData("State", "INIT");
    }

    private void drivetogoal() {
        telemetry.addData("State", "DRIVEFORWARDSTOGOAL");

    }

    private void facegoal() {
        telemetry.addData("State", "FACEGOAL");
    }

    private void lineuptogoal() {
        telemetry.addData("State", "LINEUPTOWALL");
    }

    private void turnninetydegrees() {
        telemetry.addData("State", "ROTATE");
    }

    private void gotowhiteline() {
        telemetry.addData("State", "GOTOWHITELINE");
    }

    private void dropclimbers() {
        telemetry.addData("State", "DROPCLIMBERS");
    }

    private void moveoutofway() {
        telemetry.addData("State", "MOVEOUTOFWAY");
    }

}

