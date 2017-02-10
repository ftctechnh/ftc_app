package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Blue Button 6", group="Blue")
public class BlueButton6 extends AutonomousBase{
    double xTime;
    int i;
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
            case 1:
                    moveState = MoveState.SERVO_DEPLOY;
                    moveState = MoveState.SERVO_M;
                    gameState = 3;
                break;
            case 3: //Move to beacon A push pos.
                map.setGoal(9, 7);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_DEPLOY;
                    moveState = MoveState.SERVO_M;
                    xTime = getRuntime();
                    gameState = 4;
                }
                break;
            case 4: //Move parallel to wall
                map.setGoal(map.getRobotX(),0);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_M;
                    moveState = MoveState.SERVO_DEPLOY;
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
                    moveState = MoveState.SERVO_DEPLOY;
                }
                break;
            case 6: //back up and button press A
                map.setGoal(11.5,0); // I need the goal far away so moveState keeps going
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
                    map.setGoal(11, map.getRobotY());
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
            case 8: //Move to beacon B push pos.
                map.setGoal(map.getRobotX(), 5);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_M;
                    xTime = getRuntime();
                    gameState = 9;
                }
                break;
            case 9: //Move parallel to wall
                map.setGoal(11,1);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SERVO_M;
                    gameState = 10;
                }
                else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 10: //Move to wall
                map.setGoal(12, 2.5);
                if(!touchWall.isPressed()){
                    moveState = MoveState.RIGHT;
                    gameState = 11;
                } else{
                    moveState = MoveState.STOP;
                }
                break;
            case 11: //move back  and button press B
                map.setGoal(map.getRobotX(),0); // I need the goal far away so moveState keeps going
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
                                gameState = 113;
                                pTime = getRuntime();
                            } else if (colorRight.blue() < colorRight.red() && colorLeft.blue() > colorLeft.red()) {
                                moveState = MoveState.SERVO_R;
                                gameState = 113;
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
            case 113: //move to shooter post
                if(getRuntime() - pTime > 1) {
                    map.setGoal(9, 5);
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                    if (map.distanceToGoal() <= .1) {
                        moveState = MoveState.STOP;
                        gameState = 110;
                    }
                }
                break;
            case 110: //Shoot
                desiredAngle = 45;
                if(linedUpAngle(5)){
                    moveState = MoveState.STOP;
                    gameState = 111;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE_SLOW;
                }
                break;
            case 111: // ... and shoots
                if(i < 5){
                    if(!linedUpAngle(5)) {
                        gameState = 110;
                        i++;
                    }
                }
                moveState = MoveState.SHOOT_WHEEL;
                if(getRuntime() - sTime >= 2){
                    moveState = MoveState.SHOOT_CONVEYOR;
                }
                if(getRuntime() - sTime >= 4) {
                    moveState = MoveState.SHOOT_STOP;
                    gameState = 12;
                }
                break;
            case 12: //Moves to the center and knocks off cap ball
                map.setGoal(6.5, 6.5);
                if (linedUp()) {
                    moveState = MoveState.FORWARD;
                } else {
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                }
                if (map.distanceToGoal() <= .1) {
                    moveState = MoveState.STOP;
                }
                break;
            case 777:
                    moveState = MoveState.STOP;
                    break;
            }
   }
}
