package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Patrick.Brady on 11/5/2015.
 */
public class MC {
    final int DEFAULT_SPEED = 50;

    public void moveForward(int distance, int speed){
        //move for distance at speed
    }

    public void moveBackward(int distance, int speed){
        //move backward for distance at speed
    }

    public void turnRight(int distance, int speed){
        //turn right for distance at speed
    }

    public void turnLeft(int distance, int speed){
        //turn left for distance at speed
    }

    public void moveForward(int distance){
        moveForward(distance, DEFAULT_SPEED);
    }
    public void moveBackward(int distance){
        moveBackward(distance, DEFAULT_SPEED);
    }
    public void turnRight(int distance){
        turnRight(distance, DEFAULT_SPEED);
    }
    public void turnLeft(int distance){
        turnLeft(distance, DEFAULT_SPEED);
    }
}
