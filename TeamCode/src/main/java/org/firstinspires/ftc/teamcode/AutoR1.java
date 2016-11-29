package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Red 6", group="Red")
public class AutoR1 extends AutonomousBase{
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){tDiff = getRuntime();}
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                }
                break;
            case 1: //Shoot
//                moveState = MoveState.SHOOT;
//                if(getRuntime() - sTime >= 3){
                    gameState = 2;
//                }
                break;
            case 2:
                map.setGoal(6,9);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 3;
                }
                break;
            case 3: //Move to beacon A push pos.
                map.setGoal(3,7.5);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 4;
                }
                break;
            case 4: //Move paralell to wall
                map.setGoal(3,12);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    gameState = 5;
                }
		else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 5: //Move to wall and back up and button press A
                map.setGoal(0,map.getRobotY());
                heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
                moveState = MoveState.RIGHT;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
		    gameState = 6;
                }
                break;
           case 6:
                map.setGoal(0,0);
                if(touchRight.isPressed()){
                  if(colorLeft1.blue() < colorLeft2.blue()) { 
                    moveState = MoveState.SERVO_L;
                  }
                  else{
                    moveState = MoveState.SERVO_R;
                  }
                  gameState = 7;
                  pTime = getRuntime();
                  map.setRobot(.75,3);
                }
                else{
		  moveState = MoveState.BACKWARD_SLOW;
		}
                break;
	    case 7: // moves out from wall
                if(getRuntime() - pTime > 3){
	 	     map.setGoal(1.5, map.getRobotY());
                     heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
        	     moveState = MoveState.LEFT;
	  	     if(map.distanceToGoal()<= .1){
		       moveState = MoveState.STOP;
		       gameState = 8;
		     }
                }else{
                    moveState = MoveState.STOP;
                }
	        break;
	    case 8: // moves up to push Beacon B
		map.setGoal(map.getRobotX(), 5);
		moveState = MoveState.BACKWARD;
		if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 9;
                }
                break;
	    case 9: //moves to wall
                map.setGoal(-.5, 4);
                heading = (heading + 270) % 360;
                moveState = MoveState.RIGHT;
                if(map.distanceToGoal()<= .1){
		    moveState = MoveState.STOP;
	            gameState = 10;
	          }
		break;
            case 10: //move back  and button press B
                if(touchRight.isPressed()){
                  if(colorLeft1.blue() > colorLeft2.blue()) { 
                    moveState = MoveState.SERVO_L;
                  }
                  else{
                    moveState = MoveState.SERVO_R;
                  }
                  gameState = 11;
                  pTime = getRuntime();
                }
                else{
                  moveState = MoveState.BACKWARD_SLOW;
		}
            case 11: //Moves to the center and knocks off cap ball
                map.setGoal(6.8,5.5);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1) {
                    moveState = MoveState.STOP;
                }
                break;
            case 777:
                moveState = MoveState.STOP;
                break;
        }
   }
}
