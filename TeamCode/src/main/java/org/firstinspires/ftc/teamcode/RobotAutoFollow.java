package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by djfigs1 on 10/14/16.
 */

@Autonomous(name="Beacon Follow")
public class RobotAutoFollow extends RobotAutoBeacons {

    enum AUTO_STATE {
        FIND_BLUE_LINE,
        FOLLOW_BLUE_LINE,
        PREP_BEACON,
        PUSH_BEACON,
        END
    }

    boolean firstLaunch = false; //A variable used for initializing variables in different areas of the OpMode.
    boolean pressed;
    long startTime;
    long endTime;
    double SLOW_SPEED = 0.07;
    double QUICK_SPEED = 0.10;
    int COLOR_THRESHOLD = 5;
    double OPT_DISTANCE = 3.08;

    AUTO_STATE current_state;

    //false -- red
    //true -- blue
    boolean TEAM = true;

    @Override
    public void init() {
        super.init();
        current_state = AUTO_STATE.FIND_BLUE_LINE;
    }
    @Override
    public void loop() {

        telemetry.addData("State", current_state.toString());

        switch (current_state) {
            case FIND_BLUE_LINE:
                beaconPosition(1);
                set_drive_power(SLOW_SPEED, SLOW_SPEED);

                if (getLineFollowState(VV_LINE_COLOR.BLUE, COLOR_THRESHOLD) != ROBOT_LINE_FOLLOW_STATE.NONE){
                    current_state = AUTO_STATE.FOLLOW_BLUE_LINE;
                }
                break;

            case FOLLOW_BLUE_LINE:
                if (rangeSensor.cmOptical() >= OPT_DISTANCE || rangeSensor.cmOptical() == 0){
                    switch (getLineFollowState(VV_LINE_COLOR.BLUE, COLOR_THRESHOLD)){
                        case LEFT:
                            set_drive_power(QUICK_SPEED, 0);
                            break;
                        case RIGHT:
                            set_drive_power(0, QUICK_SPEED);
                            break;
                        case BOTH:
                            set_drive_power(SLOW_SPEED, SLOW_SPEED);
                            break;
                    }
                }else{
                    current_state = AUTO_STATE.PREP_BEACON;
                }
                break;

            case PREP_BEACON:
                beaconPosition(1);
                if (getBeaconColor() == VV_BEACON_COLOR.BLUE) {
                    if (!TEAM){
                        //red team
                        prepareForBeacon(true);
                        current_state = AUTO_STATE.END;
                        break;
                    }else{
                        //blue team
                        current_state = AUTO_STATE.END;
                        break;
                    }
                }else {
                    if (!TEAM){
                        //red team
                        current_state = AUTO_STATE.END;
                        break;
                    }else{
                        //blue team
                        prepareForBeacon(true);
                        current_state = AUTO_STATE.END;
                        break;
                    }
                }


            case PUSH_BEACON:
                if (firstLaunch){
                    //Set times.
                    startTime = System.currentTimeMillis();
                    endTime = startTime + 250;
                    firstLaunch = false;
                }

                if (pressed){
                    //Back up for roughly 1/4 a second.
                    if (System.currentTimeMillis() >= endTime){
                        stopdrive();
                        pressed = false;
                        firstLaunch = true;
                        current_state = AUTO_STATE.END;
                    }else{
                        set_drive_power(-SLOW_SPEED, -SLOW_SPEED);
                    }
                }else{
                    //Go forward for roughly 1/4 a second.
                    if (System.currentTimeMillis() >= endTime){
                        stopdrive();
                        pressed = true;
                        firstLaunch = true;
                    }else{
                        set_drive_power(SLOW_SPEED, SLOW_SPEED);
                    }
                }
                break;

            case END:
                requestOpModeStop();
                break;
        }
    }

}
