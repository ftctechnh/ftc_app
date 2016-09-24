package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by djfigs1 on 9/23/16.
 */

@TeleOp(name="Auto Blue-Inner", group="Auto")
public class RobotAutoBeacons_BlueInner extends RobotAutoBecaons {

    boolean trigger = false;

    @Override
    public void init() {
        //initalize color sensors
    }

    @Override
    public void loop() {
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


        while (!trigger) {
            if (leftColorSensor.blue() > 15) {
                //Hey Emmanuel, guess who you don't have in your sights? Rachel.
                trigger = true;
                stopdrive();
            } else {
                set_drive_power(0.07, 0.07);
            }
        }
        trigger = false;

        //Move backwards for half a second.
        long endTime = System.currentTimeMillis() + 500;
        while (!trigger) {
            if (System.currentTimeMillis() >= endTime) {
                trigger = true;
                stopdrive();
            } else {
                set_drive_power(-0.25, -0.25);
            }
        }
        trigger = false;
        //TODO Turn 90 degrees to the right

        //Go to ze wall
        while (!trigger) {
            if (rangeSensor.cmUltrasonic() <= 25) {
                trigger = true;
                stopdrive();
            } else {
                set_drive_power(0.25, 0.25);
            }
        }
        trigger = false;
        //TODO Turn 90 degrees to the left

        //Wait for a white line.
        while (!trigger) {
            if (leftColorSensor.blue() > 15 && leftColorSensor.red() > 15 && leftColorSensor.green() > 15) {
                trigger = true;
                stopdrive();
            } else {
                set_drive_power(0.25, 0.25);
            }
        }
        trigger = false;

        //TODO Follow line until in beacon distance

        //get beacon color
        beaconAction();

        //TODO Turn left.

        //Find the next beacon with the white line.
        while (!trigger) {
            if (leftColorSensor.blue() > 15 && leftColorSensor.red() > 15 && leftColorSensor.green() > 15) {
                trigger = true;
                stopdrive();
            } else {
                set_drive_power(0.25, 0.25);
            }
        }
        trigger = false;

        //TODO Follow line until in beacon distance

        //get beacon color
        beaconAction();


        //We're done. (yay)
        stop();
    }

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
                pushBeaconButton();
                break;
            case NONE:
                //Crossing my fingers that this method does NOT get called during competition...
                //TODO try again
                break;

        }
    }
}