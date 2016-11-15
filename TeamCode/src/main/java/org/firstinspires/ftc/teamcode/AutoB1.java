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
        double sTime = 0;
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){tDiff = getRuntime();}
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                }
                break;
            case 1: //Shoot
                moveState = MoveState.SHOOT;
                if(getRuntime() - sTime >= 3){gameState = 2;}
                break;
            case 2: //Move to beacon A push pos.
                map.setGoal(11,6);
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
            case 3: //Move paralell to wall
                map.setGoal(11,0);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    gameState = 4;
                }
		else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 4: //Move to wall and back up and button press A
                map.setGoal(12,6);
                moveState = MoveState.RIGHT;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
		    gameState = 5;
                }
                break;
           case 5:
                if(touchRight.isPressed()){
                  moveState = MoveState.STOP;
                  if(true) { //// TODO: 10/26/2016 ADD COLOR LOGIC
                    moveState = MoveState.SERVO_PORT_L;
                  }
                  else{
                    moveState = MoveState.SERVO_PORT_R;
                  }
                  gameState = 6;
                }
		else{
		  moveState = MoveState.BACKWARD;
		}	
                break;
	    case 6:
	 	map.setGoal(11, map.getRobotY());
		moveState = MoveState.LEFT;
		if(map.distanceToGoal()<= .1){
		  moveState = MoveState.STOP;
		  gameState = 7;
		}		
		break;
	    case 7:
		map.setGoal(11, 10);
		moveState = MoveState.FORWARD;
		if(map.distanceToGoal()<=.1){
		  map.setGoal(12, 10);
		  moveState = MoveState.RIGHT;
		  if(map.distanceToGoal()<= .1){
		    moveState = MoveState.STOP;
	            gameState = 8;
	          }
		}
		break;
            case 8: //move up and button press B
                if(touchLeft.isPressed()){
		    moveState = MoveState.STOP;
		    if(true){//TODO: add color Logic
                        moveState = MoveState.SERVO_PORT_L;
		    }
		    else{
                        moveState = MoveState.SERVO_PORT_R;
		    }
                    gameState = 9;
		}
		else{    
                    moveState = MoveState.FORWARD;
		}
                break;
            case 9: //Moves to the center and knocks off cap ball
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
