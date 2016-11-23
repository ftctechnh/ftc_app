package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by USER on 10/26/2016.
 */
@Autonomous(name="Autonomous Tester", group="Test")
public class AutonomousTest extends AutonomousBase{
    public void gameState() {
        super.gameState();

        String whichIsBlue ="";
        if (colorLeft1.blue() > colorLeft2.blue()){
            whichIsBlue = "left";
        }
        else{
            whichIsBlue = "right";
        }

        switch(gameState){
          case 0: 
            if(!gyro.isCalibrating()){
              gameState = 1;
            }
          break;
          case 1:
            map.setGoal(6,9);
            if(linedUp()){
              moveState = MoveState.FORWARD;
            }else{
              moveState = MoveState.TURN_TOWARDS_GOAL;
            }
            if(map.distanceToGoal() < DISTANCE_TOLERANCE){
              gameState = 2;
            }
          break;
          case 2:
            map.setGoal(5,9);
            heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
            if(linedUp()){
              moveState = MoveState.LEFT;
            }else{
              moveState = MoveState.TURN_TOWARDS_GOAL;
            }
            if(map.distanceToGoal() < DISTANCE_TOLERANCE){
              gameState = 3;
            }
          break;
          case 3:
            map.setGoal(5,8);
            if(linedUp()){
              moveState = MoveState.STOP;
            }else{
              moveState = MoveState.TURN_TOWARDS_GOAL;
            }
            if(map.distanceToGoal() < DISTANCE_TOLERANCE){
              gameState = 3;
            }
          break;
        }
    }
}
