package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Red Button 6", group="Red")
public class RedButton6 extends AutonomousBase{
    double xTime;
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 1 && !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                    map.setRobot(6,12);
                }
                break;
            case 1: //Shoot
//                  moveState = MoveState.SHOOT;
//                  if(getRuntime() - sTime >= 3){
//                    moveState = MoveState.SHOOT_STOP;
                moveState = MoveState.SERVO_DEPLOY;
                moveState = MoveState.SERVO_M;
                gameState = 3;
//                  }
                break;
            case 3: //Move to beacon A push pos.
                map.setGoal(2, 8);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    xTime = getRuntime();
                    gameState = 4;
                }
                break;
            case 4:
                desiredAngle = 150;
                if(linedUpAngle(5)){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_DEPLOY;
                    moveState = MoveState.SERVO_M;
                    gameState = 5;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE_SLOW;
                }
                break;
            case 5: //Move to wall
                map.setGoal(0, map.getRobotY());
                if (!touchWall.isPressed()) {
                    moveState = MoveState.RIGHT_SLOW;
                } else {
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_DEPLOY;
                    gameState = 6;
                }
                break;
            case 6: //back up and button press A
                map.setGoal(map.getRobotX(),12); // I need the goal far away so moveState keeps going
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
                                moveState = MoveState.SERVO_R;
                                gameState = 7;
                                pTime = getRuntime();
                            } else if (colorRight.blue() < colorRight.red() && colorLeft.blue() > colorLeft.red()) {
                                moveState = MoveState.SERVO_L;
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
                    map.setGoal(2, map.getRobotY());
                    moveState = MoveState.SERVO_M;
                    moveState = MoveState.LEFT;
                    if(map.distanceToGoal()<= .1){
                        moveState = MoveState.STOP;
                        gameState = 8;
                    }
                }/*else{
                    map.setRobot(0,7); //Since we're positive of our position after pressing the button, we might as well use that
                }*/
                break;
            case 8: //Move to beacon B push pos.
                map.setGoal(map.getRobotX(), 5.5);
                if(linedUp()){
                    moveState = MoveState.BACKWARD;
                }else{
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_M;
                    xTime = getRuntime();
                    gameState = 10;
                }
                break;
            /*case 9: //Move parallel to wall
                desiredAngle = 160;
                if(linedUpAngle(5)){
                    moveState = MoveState.STOP;
                    gameState = 10;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE_SLOW;
                }
                break;*/
            case 10: //Move to wall
                map.setGoal(0, map.getRobotY());
                if(!touchWall.isPressed()){
                    moveState = MoveState.RIGHT_SLOW;
                } else{
                    moveState = MoveState.STOP;
                    gameState = 11;
                }
                break;
            case 11: //move back  and button press B
                map.setGoal(map.getRobotX(), 12); // I need the goal far away so moveState keeps going
                if (touchRight.isPressed()) {
                    if(xTime == 0){
                        xTime = getRuntime();
                        moveState = MoveState.STOP;
                    }else if(getRuntime() - xTime > 1){
                        if (colorRight.blue() > colorRight.red() && colorLeft.blue() < colorLeft.red()) {
                            moveState = MoveState.SERVO_R;
                            gameState = 12;
                            pTime = getRuntime();
                        } else if (colorRight.blue() < colorRight.red() && colorLeft.blue() > colorLeft.red()) {
                            moveState = MoveState.SERVO_L;
                            gameState = 12;
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
                break;
            case 12: //Moves to the center and knocks off cap ball
                if(getRuntime() - pTime > 2) {
                    map.setGoal(5.5, 5.5);
                    if (linedUp()) {
                        moveState = MoveState.FORWARD;
                    } else {
                        moveState = MoveState.STRAFE_TOWARDS_GOAL;
                    }
                    if (map.distanceToGoal() <= .1) {
                        moveState = MoveState.STOP;
                    }
                    break;
                }
            case 777:
                moveState = MoveState.STOP;
                break;
        }
   }
}
