package org.firstinspires.ftc.teamcode;

/**
 * Created by Rohan Mathur on 9/15/18.
 */
public abstract class AvesAblazeRobot {
	//the x and y coordinates of the robot on the field
	public double x;
	public double y;

	//the angle of the robot in relation to the vumark
	public double a;

	//diameter of the mecanum wheels
	public double diameter;

	//drives forever at "a" degrees
	public void drive(double a){

	}

	//stops all motors
	public void stop(){

	}

	//drive forward "d" inches
	public void drived(double d){

	}

	//rotate "a" degrees
	public void rotate(double a){

	}

	//Drive to coordinate (newX, newY)
	public void drivexy(double newX, double newY){

	}

	//Drive "d" inches at "a" degrees
	public void driveda(double d, double a){

	}

	//Identifies vumark and uses heading values to set the x and y coordinates
	public boolean resetCoordinates(){
		return false;
	}

	//returns the coordinates in an array of length 2
	public int[] getCoordinates(){
		return null;
	}

	//returns if the color sensor detects yellow
	public boolean isYellow(){
		return false;
	}

	//returns if the color sensor detects white
	public boolean isWhite(){
		return false;
	}

	//calibrates any sensors or motors that need it
	public void calibrate(){

	}



}
