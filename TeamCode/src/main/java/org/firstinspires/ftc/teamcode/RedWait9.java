package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Red Wait 9", group="Red")
public class RedWait9 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 15 && !gyro.isCalibrating()) {
                    gameState = 1;
                    map.setRobot(9,11.25);
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(7.5, 2.5);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 2;
                }
                break;
            case 2: // turns ...
                desiredAngle = 180;
                if(linedUpAngle()){
                    moveState = MoveState.STOP;
                    gameState = 3;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE;
                }
                break;
            case 3: // ... and shoots
                 moveState = MoveState.SHOOT_WHEEL;
                 if(getRuntime() - sTime >= 3){
                     moveState = MoveState.SHOOT_CONVEYOR;
                 }
                 if(getRuntime() - sTime >= 6) {
                     moveState = MoveState.SHOOT_STOP;
                     gameState = 4;
                 }
                break;
            case 4: // Line up with cap ball
                map.setGoal(8,10);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                }
                break;
            case 5: //MOVE TO KNOCK OFF BALL
                map.setGoal(5.5,6.5);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 5;
                }
                break;
            case 777:
                moveState = MoveState.STOP;
                break;
        } 
    }
}
