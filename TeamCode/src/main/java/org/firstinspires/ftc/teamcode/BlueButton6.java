package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Blue Button 6", group="Blue")
public class BlueButton6 extends AutonomousBase{
    double xTime;
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 1 && !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                    map.setRobot(6,11.25);
                }
                break;
            case 1: //Shoot
//                  moveState = MoveState.SHOOT;
//                  if(getRuntime() - sTime >= 3){
//                    moveState = MoveState.SHOOT_STOP;
                    gameState = 3;
//                  }
                break;
            case 3: //Move to beacon A push pos.
                map.setGoal(10.5, 6);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_M;
                    if(getRuntime() - xTime <= 2) {
                        moveState = MoveState.SERVO_DEPLOY;
                    }else{
                        moveState = MoveState.SERVO_DEPLOY_STOP;
                    }
                    xTime = getRuntime();
                    gameState = 4;
                }
                break;
            case 4: //Move parallel to wall
                map.setGoal(10,0);
                if(linedUp()){
                    moveState = MoveState.STOP;

                    gameState = 5;
                }
                else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 5: //Move to wall 
                map.setGoal(12, 6);
                if(!touchWall.isPressed()){
                    moveState = MoveState.RIGHT;
		            gameState = 6;
                } else{
                    moveState = MoveState.STOP;
                }
                break;
            case 6: //back up and button press A
                map.setGoal(11.25,0); // I need the goal far away so moveState keeps going
                if(!touchWall.isPressed()){
                    moveState = MoveState.RIGHT_SLOW;
                    xTime = 0;
                }else {
                    if (touchRight.isPressed()) {
                        if(xTime == 0){
                            xTime = getRuntime();
                            moveState = MoveState.STOP;
                        }else if(getRuntime() - xTime > 1){
                            if (colorRight.blue() > colorRight.red() && colorLeft.blue() < colorLeft.red()) {
                                moveState = MoveState.SERVO_L;
                                gameState = 7;
                                pTime = getRuntime();
                            } else if (colorRight.blue() < colorRight.red() && colorLeft.blue() > colorLeft.red()) {
                                moveState = MoveState.SERVO_R;
                                gameState = 7;
                                pTime = getRuntime();
                            }
                        }
                    } else {
                        if (linedUp()) {
                            moveState = MoveState.BACKWARD_SLOW;
                        } else {
                            moveState = MoveState.TURN_TOWARDS_GOAL;
                        }
                    }
                }
                break;
            case 7: // moves out from wall
                if(getRuntime() - pTime > 1){
                    map.setGoal(10.5, map.getRobotY());
                    moveState = MoveState.SERVO_M;
                    moveState = MoveState.LEFT;
                    if(map.distanceToGoal()<= .1){
                        moveState = MoveState.STOP;
                        gameState = 8;
                    }
                }else{
                    map.setRobot(12,7); //Since we're positive of our position after pressing the button, we might as well use that
                }
                break;
            case 8: // moves up to push Beacon B
                map.setGoal(map.getRobotX(), 2);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 9;
                }
                break;
            case 9: //Move parallel to wall
                map.setGoal(10,0);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    gameState = 10;
                }
                else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 10: //moves to wall
                map.setGoal(12 , 2);
                moveState = MoveState.RIGHT;
                if(touchWall.isPressed()){
                    moveState = MoveState.STOP;
                    gameState = 10;
                }
                break;
            case 11: //move back  and button press B
                map.setGoal(11.25,0); // I need the goal far away so moveState keeps going
                if(!touchWall.isPressed()){
                    moveState = MoveState.RIGHT_SLOW;
                    xTime = 0;
                }else {
                    if (touchRight.isPressed()) {
                        if(xTime == 0){
                            xTime = getRuntime();
                            moveState = MoveState.STOP;
                        }else if(getRuntime() - xTime > 1){
                            if (colorRight.blue() > colorRight.red() && colorLeft.blue() < colorLeft.red()) {
                                moveState = MoveState.SERVO_L;
                                gameState = 7;
                                pTime = getRuntime();
                            } else if (colorRight.blue() < colorRight.red() && colorLeft.blue() > colorLeft.red()) {
                                moveState = MoveState.SERVO_R;
                                gameState = 7;
                                pTime = getRuntime();
                            }
                        }
                    } else {
                        if (linedUp()) {
                            moveState = MoveState.BACKWARD_SLOW;
                        } else {
                            moveState = MoveState.TURN_TOWARDS_GOAL;
                        }
                    }
                }
                break;
            case 12: //Moves to the center and knocks off cap ball
                map.setGoal(7,7);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
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
