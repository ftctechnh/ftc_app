package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Naman on 8/12/2016.
 */

public class BasicAuton extends AutonHelper{


    //establish run states for auton
    enum RunState{
        RESET_STATE,
        FIRST_STATE,
        FIRST_RESET,
        SECOND_STATE,
        SECOND_RESET,
        THIRD_STATE,
        LAST_STATE
    }


    private RunState rs = RunState.RESET_STATE;

    public BasicAuton() {}


    @Override
    public void loop() {

        basicTel();
        telemetry.addData("state: ", rs);
        setToEncoderMode();

        switch(rs) {
            case RESET_STATE:
            {
                if (resetEncoders()) {
                    rs = RunState.FIRST_STATE;
                }
                break;
            }
            case FIRST_STATE:
            {
                if (runStraight(-45,false)) {
                    rs = RunState.FIRST_RESET;
                }
                break;
            }
            case FIRST_RESET:
            {
                if (resetEncoders()) {
                    rs = RunState.SECOND_STATE;
                }
                break;
            }
            case SECOND_STATE:
            {
                if (turnRightToDegree(180)){
                    rs = RunState.SECOND_RESET;
                }
                break;
            }
            case SECOND_RESET:
            {
                if (resetEncoders()) {
                    rs = RunState.THIRD_STATE;
                }
                break;
            }
            case THIRD_STATE:
            {
                if (runStraight(45,false)){
                    rs = RunState.LAST_STATE;
                }
                break;
            }

            case LAST_STATE:
            {
                stop();
            }
        }
    }
}