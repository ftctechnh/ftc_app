package com.qualcomm.ftcrobotcontroller.controllers;

import com.qualcomm.ftcrobotcontroller.containers.Location;

public class SensorModel {
    private Location currLocation;

    public SensorModel() {
        this.currLocation = new Location();
    }

    /**
     * Use the encoder data to update the current location
     */
    public void update() {
        //@TODO Update currLocation based on the encoders
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Getters and Setters                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Location getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(Location currLocation) {
        this.currLocation = currLocation;
    }
}
