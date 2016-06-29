package com.qualcomm.ftcrobotcontroller.controllers;

import com.qualcomm.ftcrobotcontroller.containers.Location;
import com.qualcomm.ftcrobotcontroller.opmodes.ARMAuto;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Controller for managing the current state of the robot
 * @author micray01
 * @since 6/16/2016 7:03pm
 * @version 1
 */
public class StateControl {

    //Constants
    private static double FWD_ERR = 0.1;
    private static double TURN_ERR = 0.2;

    public enum State { FWD, TURN, STOP}

    //Lookup table to identify the state-to-state interactions for the robot
    //  This lookup table will drive in a square
    private static State[] state_array = {
            State.FWD,
            State.TURN,
            State.FWD,
            State.TURN,
            State.FWD,
            State.TURN,
            State.FWD,
            State.STOP};

    //Lookup table that provides the details for the state lookup table
    //  This table should be the same size as the state lookup table
    private static Location[] condition_array = {
            new Location(0.75,0,0),
            new Location(0,0,-Math.PI/2),
            new Location(0.75,0,0),
            new Location(0,0,-Math.PI/2),
            new Location(0.75,0,0),
            new Location(0,0,-Math.PI/2),
            new Location(0,0,0)};

    private int current_state;

    /**
     * Default constructor
     */
    public StateControl() {
        current_state = 0;
    }

    /**
     * Update the state if necessary based on the current location
     * @param currLocation Current location of the robot
     */
    public void update_state(Location currLocation) {
        //Base check to prevent array size exceptions
        if (current_state>=state_array.length) return;

        //FWD case
        if (state_array[current_state]==State.FWD) {
            ARMAuto.debug.addData("State", "FWD");
            double err = currLocation.distanceTo(condition_array[current_state]);
            ARMAuto.debug.addData("FWD ERR", err);
            //@TODO convert err to a FWD PD control loop for better accuracy
            if (err<=FWD_ERR) {
                currLocation.setX(0);
                currLocation.setY(0);
                currLocation.setTheta(0);
                current_state++;
            }

        //TURN case
        } else if (state_array[current_state]==State.TURN) {
            ARMAuto.debug.addData("State", "TURN");
            double err = currLocation.angleTo(condition_array[current_state]);
            //@TODO convert err to a TURN PD control loop for better accuracy
            if (err <= TURN_ERR) {
                currLocation.setX(0);
                currLocation.setY(0);
                currLocation.setTheta(0);
                current_state++;
            }

        //STOP case
        } else if (state_array[current_state]==State.STOP) {
            ARMAuto.debug.addData("State", "STOP");
            current_state++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Getters and Setters                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the current state
     * @return State representing the current state
     */
    public State getCurrent_state() {
        if (current_state>=state_array.length) return State.STOP;
        return state_array[current_state];
    }

    /**
     * Get the end condition
     * @return boolean representing if the end condition has been met
     */
    public boolean isEndCondition() {
        return current_state>=state_array.length;
    }
}
