package com.qualcomm.ftcrobotcontroller;

/**
 * Controls all map logic for autonomous
 */

public class Map {
    double goalX, goalY;
    double robotX, robotY;

    public Map(double startPos){ //pass in Team color
        robotX = startPos;
        robotY = 10.25;
    }

    public void setGoal(double x, double y){
        goalX = x;
        goalY = y;
    }

    public double getGoalX(){
        return goalX;
    }

    public double getGoalY(){
        return goalY;
    }

    public double getRobotX(){
        return ((int)(robotX*1000))/1000.0;
    }

    public double getRobotY(){
        return ((int)(robotY*1000))/1000.0;
    }

    public double angleToGoal(){
        double dX = goalX-robotX;
        double dY = goalY-robotY;
        return (((Math.atan2(dY, dX) * 180) / Math.PI) + 450) % 360; //Ask Travis
    }

    public double distanceToGoal(){
        double dX = goalX-robotX;
        double dY = goalY-robotY;
        return Math.sqrt(dX * dX + dY * dY); //return length of hypotenuse
    }

    public void moveRobot(double feet,double heading) {
        robotX += feet * Math.cos(Math.toRadians((heading + 450) % 360));
        robotY += feet * Math.sin(Math.toRadians((heading + 450) % 360));
    }

    public double angleToGoalRev() {
        return ((angleToGoal() + 180) % 360);
    }
}
