package org.firstinspires.ftc.teamcode.autonomous.controllers;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.math.Line;
import org.firstinspires.ftc.teamcode.common.math.MathUtil;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.mecanum.MecanumPowers;

public class MecanumPurePursuitController {
    public static double MOVEMENT_Y_MIN = 0.1;
    public static double MOVEMENT_X_MIN = 0.1;
    public static double MOVEMENT_TURN_MIN = 0.1;

    public static MecanumPowers goToPosition(Pose robotPose, Pose target, double movement_speed, double point_speed) {
        //get our distance away from the point
        double distanceToPoint = Line.distance(robotPose, target);
        double angleToPoint = target.minus(robotPose).atan();
        double deltaAngleToPoint = MathUtil.angleWrap(angleToPoint - (robotPose.heading - Math.toRadians(90)));

        //x and y components required to move toward the next point (with angle correction)
        double relative_x_to_point = Math.cos(deltaAngleToPoint) * distanceToPoint;
        double relative_y_to_point = Math.sin(deltaAngleToPoint) * distanceToPoint;

        double relative_abs_x = Math.abs(relative_x_to_point);
        double relative_abs_y = Math.abs(relative_y_to_point);

        //Log.d("testTag", "relative_x: " + relative_x_to_point);
        //Log.d("testTag", "relative_y: " + relative_y_to_point);

        //preserve the shape (ratios) of our intended movement direction but scale it by movement_speed
        double movement_x_power = (relative_x_to_point / (relative_abs_y+relative_abs_x)) * movement_speed;
        double movement_y_power = (relative_y_to_point / (relative_abs_y+relative_abs_x)) * movement_speed;

        //every movement has two states, the fast "gunning" section and the slow refining part. turn this var off when close to target
        /*if(state_movement_y_prof == profileStates.gunningIt) {
            if(relative_abs_y < Math.abs(SpeedOmeter.currSlipDistanceY() * 2) || relative_abs_y < 3){
                state_movement_y_prof = state_movement_y_prof.next();
            }
        }
        if(state_movement_y_prof == profileStates.slipping){
            movement_y_power = 0;
            if(Math.abs(SpeedOmeter.getSpeedY()) <  0.03){
                state_movement_y_prof = state_movement_y_prof.next();
            }
        }
        if(state_movement_y_prof == profileStates.fineAdjustment){
            movement_y_power = Range.clip(((relative_y_to_point/8.0) * 0.15),-0.15,0.15);
        }

        if(state_movement_x_prof == profileStates.gunningIt) {
            if(relative_abs_x < Math.abs(SpeedOmeter.currSlipDistanceY() * 1.2) || relative_abs_x < 3){
                state_movement_x_prof = state_movement_x_prof.next();
            }
        }
        if(state_movement_x_prof == profileStates.slipping){
            movement_x_power = 0;
            if(Math.abs(SpeedOmeter.getSpeedY()) < 0.03){
                state_movement_x_prof = state_movement_x_prof.next();
            }
        }
        if(state_movement_x_prof == profileStates.fineAdjustment){
            movement_x_power = Range.clip(((relative_x_to_point/2.5) * smallAdjustSpeed),-smallAdjustSpeed,smallAdjustSpeed);
        }*/

        double rad_to_target = MathUtil.angleWrap(target.heading-robotPose.heading);
        double turnPower = rad_to_target > 0 ? point_speed : -point_speed;

        //every movement has two states, the fast "gunning" section and the slow refining part. turn this var off when close to target
        /*if(state_turning_prof == profileStates.gunningIt) {
            turnPower = rad_to_target > 0 ? point_speed : -point_speed;
            if(Math.abs(rad_to_target) < Math.abs(SpeedOmeter.currSlipAngle() * 1.2) || Math.abs(rad_to_target) < Math.toRadians(3.0)){
                state_turning_prof = state_turning_prof.next();
            }

        }
        if(state_turning_prof == profileStates.slipping){
            if(Math.abs(SpeedOmeter.getDegPerSecond()) < 60){
                state_turning_prof = state_turning_prof.next();
            }

        }

        if(state_turning_prof == profileStates.fineAdjustment){
            //this is a var that will go from 0 to 1 in the course of 10 degrees from the target
            turnPower = (rad_to_target/Math.toRadians(10)) * smallAdjustSpeed;
            turnPower = Range.clip(turnPower,-smallAdjustSpeed,smallAdjustSpeed);
        }*/

        return new MecanumPowers(movement_x_power, movement_y_power, turnPower);
    }

    public static class movementResult{
        public double turnDelta_rad;
        public movementResult(double turnDelta_rad){
            this.turnDelta_rad = turnDelta_rad;
        }
    }

    /**
     * Goes as fast as possible to a position
     * @param targetX
     * @param targetY
     * @param point_angle the base angle for following (90 is straight)
     * @param movement_speed
     * @param point_speed
     * @param slowDownTurnRadians
     * @param slowDownMovementFromTurnError
     * @param stop
     * @return
     */
    //point_angle is the relative point angle. 90 means face towards it
    /*public static movementResult gunToPosition(double targetX, double targetY,double point_angle,
                                               double movement_speed, double point_speed,
                                               double slowDownTurnRadians, double slowDownMovementFromTurnError,
                                               boolean stop) {

        //let's divide how we are going to slip into components
        double currSlipY = (SpeedOmeter.currSlipDistanceY() * Math.sin(worldAngle_rad)) +
                (SpeedOmeter.currSlipDistanceX() * Math.cos(worldAngle_rad));
        double currSlipX = (SpeedOmeter.currSlipDistanceY() * Math.cos(worldAngle_rad)) +
                (SpeedOmeter.currSlipDistanceX() * Math.sin(worldAngle_rad));

        //now we will adjust our target to incorporate how much the robot will slip
        double targetXAdjusted = targetX - currSlipX;
        double targetYAdjusted = targetY - currSlipY;

        //get our distance away from the adjusted point
        double distanceToPoint = Math.sqrt(Math.pow(targetXAdjusted-worldXPosition,2)
                + Math.pow(targetYAdjusted-worldYPosition,2));

        //arcTan gives the absolute angle from our location to the adjusted target
        double angleToPointAdjusted =
                Math.atan2(targetYAdjusted-worldYPosition,targetXAdjusted-worldXPosition);

        //we only care about the relative angle to the point, so subtract our angle
        //also subtract 90 since if we were 0 degrees (pointed at it) we use movement_y to
        //go forwards. This is a little bit counter-intuitive
        double deltaAngleToPointAdjusted = AngleWrap(angleToPointAdjusted-(worldAngle_rad-Math.toRadians(90)));

        //Relative x and y components required to move toward the next point (with angle correction)
        double relative_x_to_point = Math.cos(deltaAngleToPointAdjusted) * distanceToPoint;
        double relative_y_to_point = Math.sin(deltaAngleToPointAdjusted) * distanceToPoint;

        //just the absolute value of the relative components to the point (adjusted for slip)
        double relative_abs_x = Math.abs(relative_x_to_point);
        double relative_abs_y = Math.abs(relative_y_to_point);


        //let's initialize to a power that doesn't care how far we are away from the point
        //We do this by just calculating the ratios (shape) of the movement with respect to
        //the sum of the two components, (sum of the absolute values to preserve the sines)
        //so total magnitude should always equal 1
        double movement_x_power = (relative_x_to_point / (relative_abs_y+relative_abs_x));
        double movement_y_power = (relative_y_to_point / (relative_abs_y+relative_abs_x));



        //So we will basically not care about what movement_speed was given, we are going to
        //decelerate over the course of 30 cm anyways (100% to 0) and then clip the final values
        //to have a max of movement_speed.
        if(stop){
            movement_x_power *= relative_abs_x / 30.0;
            movement_y_power *= relative_abs_y / 30.0;
        }


        //clip the final speed to be in the range the user wants
        movement_x = Range.clip(movement_x_power,-movement_speed,movement_speed);
        movement_y = Range.clip(movement_y_power,-movement_speed,movement_speed);







        //actualRelativePointAngle is adjusted for what side of the robot the user wants pointed
        //towards the point of course we need to subtract 90, since when the user says 90, we want
        //to be pointed straight at the point (relative angle of 0)
        double actualRelativePointAngle = (point_angle-Math.toRadians(90));

        //this is the absolute angle to the point on the field
        double angleToPointRaw = Math.atan2(targetY-worldYPosition,targetX-worldXPosition);
        //now if the point is 45 degrees away from us, we then add the actualRelativePointAngle
        //(0 if point_angle 90) to figure out the world angle we should point towards
        double absolutePointAngle = angleToPointRaw+actualRelativePointAngle;




        //now that we know what absolute angle to point to, we calculate how close we are to it
        double relativePointAngle = AngleWrap(absolutePointAngle-worldAngle_rad);


        double velocityAdjustedRelativePointAngle = AngleWrap(relativePointAngle-
                SpeedOmeter.currSlipAngle());

        //change the turn deceleration based on how fast we are going
        double decelerationDistance = Math.toRadians(40);


        //Scale down the relative angle by 40 and multiply by point speed
        double turnSpeed = (velocityAdjustedRelativePointAngle/decelerationDistance)*point_speed;





        //now just clip the result to be in range
        movement_turn = Range.clip(turnSpeed,-point_speed,point_speed);
        //HOWEVER don't go frantic when right next to the point
        if(distanceToPoint < 10){
            movement_turn = 0;
        }

        //make sure the largest component doesn't fall below it's minimum power
        allComponentsMinPower();


        //add a smoothing effect at the very last 3 cm, where we should turn everything off,
        //no oscillation around here
        movement_x *= Range.clip((relative_abs_x/6.0),0,1);
        movement_y *= Range.clip((relative_abs_y/6.0),0,1);

        movement_turn *= Range.clip(Math.abs(relativePointAngle)/Math.toRadians(2),0,1);


        //slow down if our point angle is off
        double errorTurnSoScaleDownMovement = Range.clip(1.0-Math.abs(relativePointAngle/slowDownTurnRadians),1.0-slowDownMovementFromTurnError,1);
        //don't slow down if we aren't trying to turn (distanceToPoint < 10)
        if(Math.abs(movement_turn) < 0.00001){
            errorTurnSoScaleDownMovement = 1;
        }
        movement_x *= errorTurnSoScaleDownMovement;
        movement_y *= errorTurnSoScaleDownMovement;

        movementResult r = new movementResult(relativePointAngle);
        return r;
    }*/
}
