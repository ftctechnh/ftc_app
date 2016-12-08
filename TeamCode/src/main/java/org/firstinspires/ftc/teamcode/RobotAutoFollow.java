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
        WAIT_FOR_SERVO,
        PUSH_BEACON,
        BACK_UP,
        TURN_RIGHT,
        END
    }

    boolean firstLaunch = true; //A variable used for initializing variables in different areas of the OpMode.
    boolean pressed;
    long startTime;
    long endTime;
    int startPos;
    int endPos;
    int WAIT_TIME = 1;
    long PUSH_TIME = 750;
    long BACKUP_TIME = 1000;
    double SNAIL_SPEED = 0.03;
    double APPROACH_SPEED = 0.07;
    double FOLLOW_SPEED = 0.06;
    double QUICK_SPEED = 0.14;
    int COLOR_THRESHOLD = 5;
    double OPT_DISTANCE = 3;
    int SONIC_DISTANCE = 7;
    boolean second_time = false;

    AUTO_STATE current_state;

    //false -- red
    //true -- blue
    boolean BLUE_TEAM = true;

    @Override
    public void init() {
        super.init();
        beaconPosition(0.5);
        beaconPosition(1);
        current_state = AUTO_STATE.FIND_BLUE_LINE;
    }
    @Override
    public void loop() {

        telemetry.addData("State", current_state.toString());

        switch (current_state) {
            case FIND_BLUE_LINE:
                beaconPosition(1);
                set_drive_power(APPROACH_SPEED, APPROACH_SPEED);

                if (getLineFollowState(VV_LINE_COLOR.BLUE, COLOR_THRESHOLD) != ROBOT_LINE_FOLLOW_STATE.NONE){
                    current_state = AUTO_STATE.FOLLOW_BLUE_LINE;
                }
                break;

            case FOLLOW_BLUE_LINE:
                if (rangeSensor.cmUltrasonic() >= SONIC_DISTANCE){
                    switch (getLineFollowState(VV_LINE_COLOR.BLUE, COLOR_THRESHOLD)){
                        case LEFT:
                            set_drive_power(QUICK_SPEED, 0);
                            break;
                        case RIGHT:
                            set_drive_power(0, QUICK_SPEED);
                            break;
                        case BOTH:
                            set_drive_power(FOLLOW_SPEED, FOLLOW_SPEED);
                            break;
                    }
                }else{
                    if (rangeSensor.cmOptical() >= OPT_DISTANCE){
                        set_drive_power(SNAIL_SPEED, SNAIL_SPEED);
                    }else {
                        current_state = AUTO_STATE.PREP_BEACON;
                    }
                }
                break;

            case PREP_BEACON:
                if (getBeaconColor() == VV_BEACON_COLOR.BLUE) {
                    if (!BLUE_TEAM){
                        //red team
                        prepareForBeacon(true);
                        current_state = AUTO_STATE.WAIT_FOR_SERVO;
                    }else{
                        //blue team
                        current_state = AUTO_STATE.PUSH_BEACON;
                    }
                }else {
                    if (!BLUE_TEAM){
                        //red team
                        current_state = AUTO_STATE.PUSH_BEACON;
                    }else{
                        //blue team
                        prepareForBeacon(true);
                        current_state = AUTO_STATE.WAIT_FOR_SERVO;
                    }
                }
                break;

            case WAIT_FOR_SERVO:
                stopdrive();
                if (firstLaunch){
                    //Set times.
                    startTime = System.currentTimeMillis();
                    endTime = startTime + (WAIT_TIME * 1000);
                    firstLaunch = false;
                }

                if (System.currentTimeMillis() >= endTime){
                    current_state = AUTO_STATE.PUSH_BEACON;
                    firstLaunch = true;
                }
                break;

            case PUSH_BEACON:
                if (firstLaunch){
                    //Set times.
                    startTime = System.currentTimeMillis();
                    endTime = startTime + PUSH_TIME;
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
                        set_drive_power(-APPROACH_SPEED, -APPROACH_SPEED);
                    }
                }else{
                    //Go forward for roughly 1/4 a second.
                    if (System.currentTimeMillis() >= endTime){
                        stopdrive();
                        pressed = true;
                        firstLaunch = true;
                    }else{
                        set_drive_power(APPROACH_SPEED, APPROACH_SPEED);
                    }
                }
                break;

            case BACK_UP:
                if (firstLaunch){
                    //Set times.
                    startTime = System.currentTimeMillis();
                    endTime = startTime + BACKUP_TIME;
                    firstLaunch = false;
                }

                if (System.currentTimeMillis() >= endTime){
                    current_state = AUTO_STATE.TURN_RIGHT;
                    firstLaunch = true;
                }
                break;

            case TURN_RIGHT:
                if (second_time){
                    current_state = AUTO_STATE.END;
                }else{
                    if (firstLaunch){
                        startPos = gyroSensor.getHeading();
                        endPos = startPos + 90;
                    }
                    if (gyroSensor.getHeading() >= endPos){
                        second_time = true;
                        current_state = AUTO_STATE.FIND_BLUE_LINE;
                    }else{
                        set_drive_power(APPROACH_SPEED, -APPROACH_SPEED);
                    }

                }
                break;

            case END:
                requestOpModeStop();
                break;
        }
    }

}
