package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by djfigs1 on 9/23/16.
 */

@Autonomous(name="Auto Blue-Inner", group="Blue")
public class RobotAutoBeacons_BlueInner extends RobotAutoBecaons {

    //OpMode stuff.
    enum auto_state {
        FIND_BLUE_LINE,
        BACK_UP,
        TURN_RIGHT,
        GET_IN_BEACON_RANGE,
        TURN_LEFT,
        FIND_WHITE_LINE,
        TURN_RIGHT_1,
        FOLLOW_WHITE_LINE,
        PREP_BEACON,
        PUSH_BEACON,
        TURN_LEFT_1
    }

    auto_state current_state = auto_state.FIND_BLUE_LINE;

    //region Variables
    boolean firstLaunch = false; //A variable used for initializing variables in different areas of the OpMode.
    boolean pressed;
    long startTime;
    long endTime;
    int startHeading;
    int endHeading;
    ROBOT_LINE_FOLLOW_STATE line_follow_state;
    VV_BEACON_COLOR beacon_color;
    //#endregion

    //region Constant Variables
    double SLOW_SPEED = 0.07;
    double QUICK_SPEED = 0.15;
    double TURNING_SPEED = 0.10;
    int BEACON_DISTANCE = 15; //cm
    int COLOR_SENSOR_THRESHOLD = 15;
    int BACK_UP_TIME = 500;
    //#endregion

    @Override
    public void loop() {
        //region Old Crap
        /*
        On Start:
            Move until detected Blue Line
            Back up for about ~0.5 seconds
            Turn 90 degrees to the right.
            Move until range sensor gets to about 25 cm.
            Turn 90 degrees to the left.
            Move until color sensors detect white line.
            Turn 90 degrees to the right.
            Follow line until beacon is reachable.
            Detect beacon color.
            If the beacon is RED - BLUE:
                Move towards the right.
                Press button
            If the beacon is BLUE - RED:
                Move towards the left.
                Press button
            Turn 90 degrees to the left.
            Keep moving until white line is detected.
            Turn 90 degrees to the right.
            Follow line until beacon is reachable.
            Detect beacon color.
            If the beacon is RED - BLUE:
                Move towards the right.
                Press button
            If the beacon is BLUE - RED:
                Move towards the left.
                Press button
        */
        //get to the blue line
//        moveUntilLine(VV_LINE_COLOR.BLUE, 0.10);
//
//        //Move backwards for half a second.
//        moveForTime(500, -0.25, -0.25);
//
//        //Turn 90 degrees to the right
//        turnDegrees(90, 0.25);
//
//        //Go to ze wall
//        forwardUntilDistance(25, 0.25);
//
//        //Turn 90 degrees to the left
//        turnDegrees(-90, 0.25);
//
//        //Wait for a white line.
//        moveUntilLine(VV_LINE_COLOR.WHITE, 0.25);
//
//        //Follow line until in beacon distance
//        forwardUntilDistance(beaconDistance, 0.25);
//
//        //get beacon color (for now assume correct color is on the left)
//        beaconAction();
//
//        //Turn left.
//        turnDegrees(-90, 0.25);
//
//        //Find the next beacon with the white line.
//        moveUntilLine(VV_LINE_COLOR.WHITE, 0.25);
//
//        //Follow line until in beacon distance
//        forwardUntilDistance(beaconDistance, 0.25);
//
//        //get beacon color
//        beaconAction();
//
//
//        //We're done. (yay)
//        stop();
    //#endregion

        telemetry.addData("State", current_state.toString());

        switch (current_state){
            case FIND_BLUE_LINE:
                //Slowly approach the blue line.
                set_drive_power(SLOW_SPEED, SLOW_SPEED);

                //Check if the color sensors are detecting any blue.
                line_follow_state = getLineFollowState(VV_LINE_COLOR.BLUE, COLOR_SENSOR_THRESHOLD);

                //If any of the color sensors are detecting blue, meaning that they are something other than NONE, then stop and set our current state to BACK_UP.
                if (line_follow_state != ROBOT_LINE_FOLLOW_STATE.NONE) {
                    stopdrive();
                    current_state = auto_state.BACK_UP;
                }

            case BACK_UP:
                if (!firstLaunch){
                    //Set the start and end times.
                    startTime = System.currentTimeMillis();
                    endTime = startTime + BACK_UP_TIME;
                    firstLaunch = true;
                }

                //Checks if we're past the stop time.
                if (System.currentTimeMillis() >= endTime) {
                    //If so, reset all time-themed variables.
                    startTime = 0;
                    endTime = 0;

                    //Stop moving.
                    stopdrive();

                    //Set the next state of the OpMode.
                    current_state = auto_state.TURN_RIGHT;
                }else{
                    //If we still need to back up, back up.
                    set_drive_power(-SLOW_SPEED, -SLOW_SPEED);
                }

            case TURN_RIGHT:
                if (firstLaunch) {
                    startHeading = gyroSensor.getHeading();
                    endHeading = startHeading + 90; //For now, assume one "heading" is one degree. Thus, end heading is 90 degrees away.
                    firstLaunch = true;
                }else{
                    //Check if we're passed the end heading.
                    if (!(gyroSensor.getHeading() >= endHeading)) {
                        set_drive_power(TURNING_SPEED, -TURNING_SPEED); //turn right
                    }else{
                        //If we're here, we should have turned a full 90 degrees, so we'll stop.
                        stopdrive();
                        current_state = auto_state.GET_IN_BEACON_RANGE;
                    }
                }

            case GET_IN_BEACON_RANGE:
                //Check if we're in range of the beacon
                if (rangeSensor.cmUltrasonic() >= BEACON_DISTANCE){
                    //If we are, stop moving.
                    stopdrive();
                }else{
                    //Since we're not in range, keep moving, but at a "quicker" speed.
                    set_drive_power(QUICK_SPEED, QUICK_SPEED);
                    current_state = auto_state.TURN_LEFT;
                }

            case TURN_LEFT:
                //Pretty much the exact same to TURN_RIGHT.
                //Because I copied + pasted it.

                if (firstLaunch) {
                    startHeading = gyroSensor.getHeading();
                    endHeading = startHeading -90; //For now, assume one "heading" is one degree. Thus, end heading is 90 degrees away.
                    firstLaunch = true;
                }else{
                    //Check if we're passed the end heading.
                    if (!(gyroSensor.getHeading() <= endHeading)) {
                        set_drive_power(-TURNING_SPEED, TURNING_SPEED); //turn left
                    }else{
                        //If we're here, we should have turned a full 90 degrees, so we'll stop.
                        stopdrive();
                        current_state = auto_state.FIND_WHITE_LINE;
                    }
                }

            case FIND_WHITE_LINE:
                //Slowly approach the whtie line.
                set_drive_power(SLOW_SPEED, SLOW_SPEED);

                //Check if the color sensors are detecting any white.
                line_follow_state = getLineFollowState(VV_LINE_COLOR.WHITE, COLOR_SENSOR_THRESHOLD);

                //If any of the color sensors are detecting white, meaning that they are something other than NONE, then stop and set our current state to TURN_RIGHT_1.
                if (line_follow_state != ROBOT_LINE_FOLLOW_STATE.NONE) {
                    stopdrive();
                    current_state = auto_state.TURN_RIGHT_1;
                }

            case TURN_RIGHT_1:
                if (firstLaunch) {
                    startHeading = gyroSensor.getHeading();
                    endHeading = startHeading + 90; //For now, assume one "heading" is one degree. Thus, end heading is 90 degrees away.
                    firstLaunch = true;
                }else{
                    //Check if we're passed the end heading.
                    if (!(gyroSensor.getHeading() >= endHeading)) {
                        set_drive_power(TURNING_SPEED, -TURNING_SPEED); //turn right
                    }else{
                        //If we're here, we should have turned a full 90 degrees, so we'll stop.
                        stopdrive();
                        current_state = auto_state.GET_IN_BEACON_RANGE;
                    }
                }

            case FOLLOW_WHITE_LINE:
                if (!(gyroSensor.getHeading() <= BEACON_DISTANCE)) {
                    line_follow_state = getLineFollowState(VV_LINE_COLOR.WHITE, COLOR_SENSOR_THRESHOLD);

                    switch (line_follow_state){
                        case LEFT:
                            set_drive_power(SLOW_SPEED, 0);
                        case RIGHT:
                            set_drive_power(0, SLOW_SPEED);
                        case BOTH:
                            set_drive_power(SLOW_SPEED, SLOW_SPEED);

                    }
                }else {
                    stopdrive();
                }

            case PREP_BEACON:
                if (firstLaunch){
                    prepareForBeacon(false);
                    beacon_color = getBeaconColor();
                }else{
                    if (beacon_color == VV_BEACON_COLOR.RED) {
                        prepareForBeacon(true);
                    }
                }

            case PUSH_BEACON:
                //Go forward and push, then back up.

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
                        current_state = auto_state.TURN_LEFT_1;
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

            case TURN_LEFT_1:
                //do something later
        }

    }

//    @Override
//    public void loop() {
//        /*
//        On Start:
//            Move until detected Blue Line
//            Back up for about ~0.5 seconds
//            Turn 90 degrees to the right.
//            Move until range sensor gets to about 25 cm.
//            Turn 90 degrees to the left.
//            Move until color sensors detect white line.
//            Turn 90 degrees to the right.
//            Follow line until beacon is reachable.
//            Detect beacon color.
//            If the beacon is RED - BLUE:
//                Move towards the right.
//                Press button
//            If the beacon is BLUE - RED:
//                Move towards the left.
//                Press button
//            Turn 90 degrees to the left.
//            Keep moving until white line is detected.
//            Turn 90 degrees to the right.
//            Follow line until beacon is reachable.
//            Detect beacon color.
//            If the beacon is RED - BLUE:
//                Move towards the right.
//                Press button
//            If the beacon is BLUE - RED:
//                Move towards the left.
//                Press button
//        */
//        //get to the blue line
//        moveUntilLine(VV_LINE_COLOR.BLUE, 0.25);
//
//        //Move backwards for half a second.
//        moveForTime(500, -0.25, -0.25);
//
//        //Turn 90 degrees to the right
//        turnDegrees(90, 0.25);
//
//        //Go to ze wall
//        forwardUntilDistance(25, 0.25);
//
//        //Turn 90 degrees to the left
//        turnDegrees(-90, 0.25);
//
//        //Wait for a white line.
//        moveUntilLine(VV_LINE_COLOR.WHITE, 0.25);
//
//        //Follow line until in beacon distance
//        forwardUntilDistance(beaconDistance, 0.25);
//
//        //get beacon color (for now assume correct color is on the left)
//        beaconAction();
//
//        //Turn left.
//        turnDegrees(-90, 0.25);
//
//        //Find the next beacon with the white line.
//        moveUntilLine(VV_LINE_COLOR.WHITE, 0.25);
//
//        //Follow line until in beacon distance
//        forwardUntilDistance(beaconDistance, 0.25);
//
//        //get beacon color
//        beaconAction();
//
//
//        //We're done. (yay)
//        stop();
//
//
//    }


    //-----------------------------------------
    //  METHODS
    //-----------------------------------------

    public void getColor() {

    }

    public void getBeacons() {

    }

    public void beaconAction() {
        RobotAutoBecaons.VV_BEACON_COLOR beacon_color = getBeaconColor();
        switch (getBeaconColor()) {
            case RED:
                //TODO idk something
                break;
            case BLUE:
                pushBeaconButton(false);
                break;
            case NONE:
                //Crossing my fingers that this method does NOT get called during competition...
                //TODO try again
                break;

        }
    }
}