package com.qualcomm.ftcrobotcontroller.controllers;

import com.qualcomm.ftcrobotcontroller.containers.Location;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Controller for managing the wheels and how the robot should physically drive
 * @author micray01
 * @since 6/7/2016 7:03pm
 * @version 1
 */
public class DriveControl {

    private final double k1 = 0.01;
    private final double k2 = 0.2;
    private final double SPD = 0.5;

    private Location initLocation;
    private Location destLocation;
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    public DriveControl(HardwareMap hardwareMap) {
        this.initLocation = new Location();
        this.destLocation = new Location();
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  Drive control                                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Stop both the left and the right wheels
     */
    public void stop() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    /**
     * Turn until the initial location angle matches the destination location angle
     * @param currLocation Current location that the robot is at
     */
    public void turn(Location currLocation) {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
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
        double y = k1*theta_error + (k2/currLocation.getSpd())*distance_error;
        leftMotor.setPower(SPD+y);
        rightMotor.setPower(SPD-y);
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
