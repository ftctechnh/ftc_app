package com.qualcomm.ftcrobotcontroller.controllers;

import com.qualcomm.ftcrobotcontroller.containers.Location;
import com.qualcomm.ftcrobotcontroller.opmodes.ARMAuto;

public class SensorModel {
    private Location currLocation;
    //@TODO find the correct resolution of the encoders
    private final double ENCODER_RES = 1440;
    //@TODO measure the wheel radius of the robot
    private final double WHEEL_RADIUS = 0.02;
    //@TODO measure the wheel base of the robot
    private final double WHEEL_BASE = 0.2;

    public SensorModel() {
        this.currLocation = new Location();
    }

    /**
     * Use the encoder data to update the current location
     */
    public void update() {

        //Retrieve the current encoder counts and time values
        int l_count = ARMAuto.leftMotor.getCurrentPosition();
        int r_count = ARMAuto.rightMotor.getCurrentPosition();
        long currTime = System.currentTimeMillis();
        double dtime = (currTime - currLocation.getTime())/1000.0;

        //Find delta encoder counts, distances and thetas
        int dr = r_count - currLocation.getRight_encoder();
        int dl = l_count - currLocation.getLeft_encoder();
        double f1 = ((2.0*Math.PI*WHEEL_RADIUS)/ENCODER_RES);
        double ds = ((dr+dl)/2.0)*f1;
        double dtheta = ((dr-dl)/WHEEL_BASE)*f1;

        //Calculate the new x, y and theta values
        double new_x = currLocation.getX()+(ds*Math.cos(currLocation.getTheta()));
        double new_y = currLocation.getY()+(ds*Math.sin(currLocation.getTheta()));
        double new_theta = currLocation.getTheta()+dtheta;
        ARMAuto.debug.addData("x", new_x);
        ARMAuto.debug.addData("y", new_y);
        ARMAuto.debug.addData("theta", new_theta);

        //Calculate the speed
        Location new_loc = new Location(new_x,new_y,new_theta);
        double spd = new_loc.distanceTo(currLocation)/dtime;

        //Update the current location
        this.currLocation.set(new_x, new_y, new_theta, l_count, r_count, currTime, spd);
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
