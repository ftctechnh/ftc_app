package com.qualcomm.ftcrobotcontroller.controllers;

import com.qualcomm.ftcrobotcontroller.containers.Location;
import com.qualcomm.ftcrobotcontroller.opmodes.ARMAuto;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Controller for managing the wheels and how the robot should physically drive
 * @author micray01
 * @since 6/7/2016 7:03pm
 * @version 1
 */
public class DriveControl {

    //@TODO tune the K values for the PID loops
    private final double k2 = 0.25;
    private final double k1 = Math.sqrt(4.0*k2);
    private final double SPD = 0.4;

    private Location initLocation;
    private Location destLocation;

    /**
     * Default constructor. Initializes the motors and sets base initial/destination locations
     * @param hardwareMap map defined by FTC for usage in initializing motor variables
     */
    public DriveControl(HardwareMap hardwareMap) {
        this.initLocation = new Location();
        this.destLocation = new Location();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Drive control                                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Stop both the left and the right wheels
     */
    public void stop() {
        ARMAuto.leftMotor.setPower(0);
        ARMAuto.rightMotor.setPower(0);
    }

    /**
     * Turn until the initial location angle matches the destination location angle
     * @param currLocation Current location that the robot is at
     */
    public void turn(Location currLocation) {
        //@TODO implement the turning PID controller
        ARMAuto.leftMotor.setPower(0);
        ARMAuto.rightMotor.setPower(0);
    }

    /**
     * Drive in a straight line from the initial location to the end location<br>
     *     Attempts to keep the robot on the shortest path from initial to destination locations by
     *     minimizing error in angle and distance
     * @param currLocation Current location that the robot is at
     */
    public void driveStraight(Location currLocation) {
        double theta_b = Math.atan2(destLocation.getY()-initLocation.getY(), destLocation.getX()-initLocation.getX());
        double theta_c = Math.atan2(currLocation.getY()-initLocation.getY(), currLocation.getX()-initLocation.getX());
        double theta_d = theta_c - theta_b;
        double distance = Math.sqrt(Math.pow(currLocation.getX()-initLocation.getX(), 2) +
                                    Math.pow(currLocation.getY()-initLocation.getY(), 2));
        double theta_error = currLocation.getTheta() - theta_b;
        double distance_error = distance*Math.sin(theta_d);
        double y = k1*theta_error + (currLocation.getSpd()==0 ? 0 : (k2/currLocation.getSpd()))*distance_error;
        ARMAuto.leftMotor.setPower(SPD+y);
        ARMAuto.rightMotor.setPower(SPD-y);

        //Debug messages
        ARMAuto.debug.addData("theta_error", theta_error);
        ARMAuto.debug.addData("distance_error", distance_error);
        ARMAuto.debug.addData("error", y);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Getters and Setters                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the initial position
     * @return Location representing the initial position
     */
    public Location getInitLocation() {
        return initLocation;
    }

    /**
     * Set the initial position
     * @param initLocation Location representing the initial position
     */
    public void setInitLocation(Location initLocation) {
        this.initLocation = initLocation;
    }

    /**
     * Get the destination position
     * @return Location representing the destination location
     */
    public Location getDestLocation() {
        return destLocation;
    }

    /**
     * Set the destination position
     * @param destLocation Location representing the destination location
     */
    public void setDestLocation(Location destLocation) {
        this.destLocation = destLocation;
    }
}
