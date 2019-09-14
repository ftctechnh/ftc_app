package org.firstinspires.ftc.teamcode.autonomous.controllers;

import org.firstinspires.ftc.teamcode.common.math.Point;

/**
 * CurvePoint is a class used with the followCurve function in MovementEssentials.
 */
public class CurvePoint {
    public double x, y;
    public double moveSpeed, turnSpeed;
    public double followDistance;
    public double slowDownTurnRadians, slowDownTurnAmount;
    public double pointLength;

    public CurvePoint(double x, double y, double moveSpeed, double turnSpeed,
                      double followDistance, double slowDownTurnRadians, double slowDownTurnAmount) {
        this.x = x;
        this.y = y;
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
        this.followDistance = followDistance;
        this.pointLength = followDistance;
        this.slowDownTurnRadians = slowDownTurnRadians;
        this.slowDownTurnAmount = slowDownTurnAmount;
    }

    public CurvePoint(double x, double y, double moveSpeed, double turnSpeed, double followDistance,
                      double pointLength, double slowDownTurnRadians, double slowDownTurnAmount){
        this.x = x;
        this.y = y;
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
        this.followDistance = followDistance;
        this.pointLength = pointLength;
        this.slowDownTurnRadians = slowDownTurnRadians;
        this.slowDownTurnAmount = slowDownTurnAmount;
    }

    public CurvePoint(CurvePoint nextPoint) {
        x = nextPoint.x;
        y = nextPoint.y;
        moveSpeed = nextPoint.moveSpeed;
        turnSpeed = nextPoint.turnSpeed;
        followDistance = nextPoint.followDistance;
        slowDownTurnRadians = nextPoint.slowDownTurnRadians;
        slowDownTurnAmount = nextPoint.slowDownTurnAmount;
        pointLength = nextPoint.pointLength;

    }

    public Point toPoint(){
        return new Point(x,y);
    }
    public void setPoint(Point p){
        x = p.x;
        y = p.y;
    }
}

