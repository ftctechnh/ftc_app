package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Blue Corner 3", group="Blue")
public class BlueCorner3 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 3 && !gyro.isCalibrating()) {
                    gameState = 1;
                    map.setRobot(3,11.25);
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(4, 10);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 2;
                }
                break;
            case 2: // turns ...
                desiredAngle = 220;
                if(linedUpAngle()){
                    moveState = MoveState.STOP;
                    gameState = 3;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE_SLOW;
                }
                break;
            case 3: // ... and shoots
                if(!linedUpAngle(5)){
                    gameState = 2;
                }
                moveState = MoveState.SHOOT_WHEEL;
                if(getRuntime() - sTime >= 2) {
                    moveState = MoveState.SHOOT_CONVEYOR;
                }
                if(getRuntime() - sTime >= 5) {
                    moveState = MoveState.SHOOT_STOP;
                    gameState = 4;
                    sTime = getRuntime();
                }
                break;
            case 4: //MOVE TO KNOCK OFF BALL
                map.setGoal(9,9);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    gameState = 5;
                }
                break;

            case 5: //MOVE TO KNOCK OFF BALL
                map.setGoal(11,11);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    gameState = 777;
                }
                break;
            case 777:
                moveState = MoveState.FULL_STOP;
                break;
        }
    }
}
