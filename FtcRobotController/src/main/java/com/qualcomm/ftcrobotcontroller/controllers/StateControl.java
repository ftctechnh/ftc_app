package com.qualcomm.ftcrobotcontroller.controllers;

/**
 * Controller for managing the current state of the robot
 * @author micray01
 * @since 6/7/2016 7:03pm
 * @version 1
 */
public class StateControl {

    public enum State { FWD, TURN, STOP}
    private State current_state;
    private boolean endCondition;

    public StateControl() {
        this.current_state = State.STOP;
        endCondition = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Getters and Setters                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the current state
     * @return State representing the current state
     */
    public State getCurrent_state() {
        return current_state;
    }

    /**
     * Set the current state
     * @param current_state representing the current state
     */
    public void setCurrent_state(State current_state) {
        this.current_state = current_state;
    }

    /**
     * Get the end condition
     * @return boolean representing if the end condition has been met
     */
    public boolean isEndCondition() {
        return endCondition;
    }

    /**
     * Set the end condition
     * @param endCondition representing if the end condition has been met
     */
    public void setEndCondition(boolean endCondition) {
        this.endCondition = endCondition;
    }
}
