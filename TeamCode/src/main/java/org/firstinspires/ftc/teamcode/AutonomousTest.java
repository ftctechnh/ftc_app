package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by USER on 10/26/2016.
 */
@Autonomous(name="Autonomous Tester", group="Test")
public class AutonomousTest extends AutonomousBase{
    public void gameState() {
        super.gameState();

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
            map.setGoal(6,8);
            moveState = MoveState.STRAFE_TOWARDS_GOAL;
            if(map.distanceToGoal() < DISTANCE_TOLERANCE){
              moveState = MoveState.STOP;
              gameState = 4;
            }
          break;
        }
    }
}
