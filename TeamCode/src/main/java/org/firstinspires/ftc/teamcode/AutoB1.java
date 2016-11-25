package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Blue 6", group="Blue")
public class AutoB1 extends AutonomousBase{
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
//                  moveState = MoveState.SHOOT;
//                  if(getRuntime() - sTime >= 3){
//                    moveState = MoveState.SHOOT_STOP;
                    gameState = 2;
//                  }
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
                map.setGoal(9,6);
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
                map.setGoal(9,0);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    gameState = 5;
                }
		else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 5: //Move to wall 
                map.setGoal(11,map.getRobotY());
                heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
                moveState = MoveState.RIGHT;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
		    gameState = 100;
                }
                break;
           case 100:
               map.setGoal(10.7,map.getRobotY());
               heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
               moveState = MoveState.LEFT;
               if(map.distanceToGoal()<=.1){
                   moveState = MoveState.STOP;
                   gameState = 6;
                }
               break;
           case 6: //back up and button press A
                map.setGoal(12,0); // I need the goal far away so moveState keeps going
                if(touchRight.isPressed()){
                  if(colorLeft1.blue() > colorLeft2.blue()) { 
                    moveState = MoveState.SERVO_L;
                  }
                  else{
                    moveState = MoveState.SERVO_R;
                  }
                  gameState = 7;
                  pTime = getRuntime();
                  map.setRobot(12,7); //Since we're positive of our position after pressing the button, we might as well use that
                }
                else{
                  if(linedUp()){
		      moveState = MoveState.BACKWARD_SLOW;
                  }else{
                      moveState = MoveState.TURN_TOWARDS_GOAL;
                  }
		}
                break;
	    case 7: // moves out from wall
                if(getRuntime() - pTime > 3){
	 	     map.setGoal(11.5, map.getRobotY());
                     heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
        	     moveState = MoveState.LEFT;
	  	     if(map.distanceToGoal()<= .1){
		       moveState = MoveState.STOP;
		       gameState = 8;
		     }
                }else{
                    if(map.getRobotX() < 12.5);{
                        heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
                        moveState = MoveState.RIGHT;
                   }
                }
	        break;
	    case 8: // moves up to push Beacon B
		map.setGoal(map.getRobotX(), 2);
		moveState = MoveState.FORWARD;
		if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 9;
                }
                break;
	    case 9: //moves to wall
                map.setGoal(12.5, map.getRobotY());
                heading = (heading + 270) % 360;
                moveState = MoveState.RIGHT;
                if(map.distanceToGoal()<= .1){
		    moveState = MoveState.STOP;
	            gameState = 101;
	          }
		break;
           case 101:
               map.setGoal(11.7,map.getRobotY());
               heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
               moveState = MoveState.LEFT;
               if(map.distanceToGoal()<=.1){
                   moveState = MoveState.STOP;
                   gameState = 10;
                }
               break;
            case 10: //move back  and button press B
                map.setGoal(12,0); // I need the goal far away so moveState keeps going
                if(touchRight.isPressed()){
                  if(colorLeft1.blue() > colorLeft2.blue()) { 
                    moveState = MoveState.SERVO_L;
                  }
                  else{
                    moveState = MoveState.SERVO_R;
                  }
                  gameState = 11;
                  map.setRobot(12,3);
                  pTime = getRuntime();
                }
                else{
                    if(linedUp()){
		        moveState = MoveState.BACKWARD_SLOW;
                    }else{
                        moveState = MoveState.TURN_TOWARDS_GOAL;
                    }
                }
                break;
	    case 11: // moves out from wall
                if(getRuntime() - pTime > 3){
	 	     map.setGoal(11, map.getRobotY());
                     heading = (heading + 270) % 360; // We're moving sideways, so we line up oddly
        	     moveState = MoveState.LEFT;
	  	     if(map.distanceToGoal()<= .1){
		       moveState = MoveState.STOP;
		       gameState = 12;
		     }
                }else{
                    moveState = MoveState.STOP;
                }
	        break;
            case 12: //Moves to the center and knocks off cap ball
                map.setGoal(7,7);
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
